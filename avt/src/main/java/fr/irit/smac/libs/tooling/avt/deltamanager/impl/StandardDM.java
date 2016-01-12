/*
 * #%L
 * avt
 * %%
 * Copyright (C) 2014 - 2015 IRIT - SMAC Team
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package fr.irit.smac.libs.tooling.avt.deltamanager.impl;

import java.math.BigDecimal;
import java.math.MathContext;

import fr.irit.smac.libs.tooling.avt.EMessageException;
import fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM;
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDE;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision.EDecision;
import fr.irit.smac.libs.tooling.avt.range.IRange;

/**
 * The Class StandardDM.
 */
public class StandardDM implements IAdvancedDM {

    /** The delta evolution. */
    private final IGeometricDE deltaEvolution;

    /** The dm decision. */
    private final IDMDecision  dmDecision;

    /** The range. */
    private final IRange       range;

    /** The delta. */
    private double             delta;

    /** The delta min. */
    private double             deltaMin;

    /** The delta max. */
    private double             deltaMax;

    /** The nb geometric steps. */
    private int                nbGeometricSteps;

    /**
     * Instantiates a new standard dm.
     *
     * @param deltaMin
     *            the delta min
     * @param range
     *            the range
     * @param deltaEvolution
     *            the delta evolution
     * @param dmDecision
     *            the dm decision
     * @throws IllegalArgumentException
     *             if deltaEvolution.getIncreaseFactor() <= 1.;
     */
    public StandardDM(double deltaMin, IRange range, IGeometricDE deltaEvolution, IDMDecision dmDecision) {

        checkDeltaMin(deltaMin);
        checkRange(range);
        checkDeltaEvolution(deltaEvolution);

        this.range = range;
        this.deltaMin = deltaMin;
        this.deltaEvolution = deltaEvolution;
        this.dmDecision = dmDecision;
        this.reconfigure(deltaMin);
        this.resetState();
    }

    /**
     * Instantiates a new standard dm.
     *
     * @param deltaMin
     *            the delta min
     * @param deltaMax
     *            the delta max
     * @param range
     *            the range
     * @param deltaEvolution
     *            the delta evolution
     * @param dmDecision
     *            the dm decision
     * @throws IllegalArgumentException
     *             if deltaEvolution.getIncreaseFactor() <= 1.
     */
    public StandardDM(double deltaMin, double deltaMax, IRange range, IGeometricDE deltaEvolution,
        IDMDecision dmDecision) {

        checkDeltaMin(deltaMin);
        checkDeltaMax(deltaMax);
        checkDeltaMinAndMax(deltaMin, deltaMax);
        checkRange(range);
        checkDeltaEvolution(deltaEvolution);

        this.range = range;
        this.deltaMin = deltaMin;
        this.deltaMax = deltaMax;
        this.nbGeometricSteps = StandardDM.computeNbGeometricStepsFromDeltaMax(deltaMin, deltaMax,
            deltaEvolution.getIncreaseFactor());
        this.dmDecision = dmDecision;
        this.deltaEvolution = deltaEvolution;
        this.resetState();
    }

    /**
     * Check range.
     *
     * @param range the range
     */
    private static void checkRange(IRange range) {

        if (range == null) {
            throw new IllegalArgumentException(EMessageException.RANGE_NULL.toString());
        }
    }

    /**
     * Check delta min.
     *
     * @param deltaMin the delta min
     */
    private static void checkDeltaMin(double deltaMin) {

        if (Double.isNaN(deltaMin)) {
            throw new IllegalArgumentException(EMessageException.DELTA_MIN_NAN.toString());
        }

        if (deltaMin <= 0) {
            throw new IllegalArgumentException(EMessageException.DELTA_MIN_LE_0.toString());
        }
    }

    /**
     * Check delta max.
     *
     * @param deltaMax the delta max
     */
    private static void checkDeltaMax(double deltaMax) {

        if (Double.isNaN(deltaMax)) {
            throw new IllegalArgumentException(EMessageException.DELTA_MAX_NAN.toString());
        }
    }

    /**
     * Check delta min and max.
     *
     * @param deltaMin the delta min
     * @param deltaMax the delta max
     */
    private static void checkDeltaMinAndMax(double deltaMin, double deltaMax) {

        if (deltaMax < deltaMin) {
            throw new IllegalArgumentException(EMessageException.DELTA_MAX_LT_DELTA_MIN.toString());
        }
    }

    /**
     * Check delta evolution.
     *
     * @param deltaEvolution the delta evolution
     */
    private static void checkDeltaEvolution(IGeometricDE deltaEvolution) {

        if (deltaEvolution == null) {
            throw new IllegalArgumentException(EMessageException.DELTA_EVOLUTION_NULL.toString());
        }

        if (deltaEvolution.getIncreaseFactor() <= 1.) {
            throw new IllegalArgumentException(EMessageException.DELTA_EVOLUTION_INCREASE_FACTOR_LE_1.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#resetState()
     */
    @Override
    public void resetState() {
        // 1. reset current delta to the mid delta value
        if (this.range.hasInfiniteSize()) {
            // if the range is infinite the deltaMax value is not a good and
            // informed one so we set current delta to
            // this.deltaMin * Math.pow(this.deltaEvolution.increaseFactor,
            // Min(Math.floor((double)
            // this.nbGeometricSteps / 2.), 10))
            // so that it is a delta value equivalent to ten successive
            // accelerations
            // from deltaMin
            this.delta = this.deltaMin * Math.pow(this.deltaEvolution.getIncreaseFactor(),
                Math.min(Math.floor((double) this.nbGeometricSteps / 2.), // nbGeomSetp
                                                                          // / 2
                                                                          // because
                                                                          // this
                                                                          // is
                                                                          // a
                                                                          // good
                                                                          // value
                                                                          // to
                                                                          // not
                                                                          // overcome
                    10));

        }
        else {
            this.delta = this.deltaMin * Math.pow(this.deltaEvolution.getIncreaseFactor(),
                Math.floor((double) this.nbGeometricSteps / 2.));
        }
        // 2. reset the decision state
        this.dmDecision.resetState();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager#adjustDelta(
     * fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection)
     */
    @Override
    public void adjustDelta(EDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException(EMessageException.DIRECTION_NULL.toString());
        }

        EDecision decision = this.dmDecision.getNextDecision(direction);
        if (decision == EDecision.INCREASE_DELTA) {
            this.delta = this.getIncreasedDelta();
        }
        else if (decision == EDecision.DECREASE_DELTA) {
            this.delta = this.getDecreasedDelta();
        }
    }

    /**
     * Gets the increased delta.
     *
     * @return the increased delta
     */
    protected double getIncreasedDelta() {
        double increasedDelta = this.deltaEvolution.getIncreasedDelta(this.delta);
        if (increasedDelta < this.deltaMin) {
            // acceleration right from the deltaMin value
            increasedDelta = this.deltaMin;
        }
        else if (increasedDelta > this.deltaMax) {
            // do not exceed the deltaMax value
            increasedDelta = this.deltaMax;
        }
        return increasedDelta;
    }

    /**
     * Gets the decreased delta.
     *
     * @return the decreased delta
     */
    protected double getDecreasedDelta() {
        return this.deltaEvolution.getDecreasedDelta(this.delta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager#getDelta()
     */
    @Override
    public double getDelta() {
        return this.delta;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager#getAdvancedDM()
     */
    @Override
    public IAdvancedDM getAdvancedDM() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#getDeltaMin()
     */
    @Override
    public double getDeltaMin() {
        return this.deltaMin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#getDeltaMax()
     */
    @Override
    public double getDeltaMax() {
        return this.deltaMax;
    }

    /**
     * Sets the deltaMin value.
     * <p>
     * <strong>STATE CONSISTENCY WARNING:</strong> this method doesn't guaranty
     * the state consistency of the DeltaManager
     * </p>
     * 
     * @param deltaMin
     *            the deltaMin to set
     * @throws IllegalArgumentException
     *             if <code>deltaMin > this.getDeltaMax()</code>
     */
    @Override
    public void setDeltaMin(double deltaMin) {
        if (Double.isNaN(deltaMin)) {
            throw new IllegalArgumentException(EMessageException.DELTA_MIN_NAN.toString());
        }

        if (deltaMin > this.deltaMax) {
            throw new IllegalArgumentException(EMessageException.DELTA_MIN_GT_DELTA_MAX.toString());
        }
        this.deltaMin = deltaMin;
    }

    /**
     * Sets the deltaMax value.
     * <p>
     * <strong>STATE CONSISTENCY WARNING:</strong> this method doesn't guaranty
     * the state consistency of the DeltaManager
     * </p>
     * 
     * @param deltaMax
     *            the deltaMax to set
     * @throws IllegalArgumentException
     *             if <code>deltaMax < this.getDeltaMin()</code>
     */
    @Override
    public void setDeltaMax(double deltaMax) {
        if (Double.isNaN(deltaMax)) {
            throw new IllegalArgumentException(EMessageException.DELTA_MAX_NAN.toString());
        }

        if (deltaMax < this.deltaMin) {
            throw new IllegalArgumentException(EMessageException.DELTA_MAX_LT_THIS_DELTA_MIN.toString());
        }
        this.deltaMax = deltaMax;
    }

    /**
     * Sets the delta value.
     * <p>
     * <strong>STATE CONSISTENCY WARNING:</strong> this method doesn't guaranty
     * the state consistency of the DeltaManager
     * </p>
     * 
     * @param delta
     *            the value to set
     */
    @Override
    public void setDelta(double delta) {
        if (Double.isNaN(delta)) {
            throw new IllegalArgumentException(EMessageException.DELTA_NAN.toString());
        }

        this.delta = delta;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#getGeometricStepNumber
     * ()
     */
    @Override
    public int getGeometricStepNumber() {
        return (int) Math.ceil((Math.log(Math.max(this.delta, this.deltaMin)) - Math.log(this.deltaMin))
            / Math.log(this.deltaEvolution.getIncreaseFactor())) + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#getNbGeometricSteps
     * ()
     */
    @Override
    public int getNbGeometricSteps() {
        return this.nbGeometricSteps;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#reconfigure(double
     * )
     */
    @Override
    /**
     * Reconfigure the values of delta min, delta max and nbGeometricSteps with
     * the given new delta min and the range of the avt
     * 
     * @param deltaMin
     *            minimal delta value (or tuning step)
     */
    public void reconfigure(double deltaMin) {
        this.reconfigure(deltaMin, this.range.computeRangeSize());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#reconfigure(double
     * , java.math.BigDecimal)
     */
    @Override
    /**
     * @throws IllegalArgumentException
     *             if deltaMin <= 0
     * @throws IllegalArgumentException
     *             if range == null
     * @throws IllegalArgumentException
     *             if deltaMin => range
     * @throws IllegalArgumentException
     *             if range > if Double.MAX_VALUE * 2
     */
    public void reconfigure(double deltaMin, BigDecimal range) {

        checkDeltaMin(deltaMin);
        
        if (range == null) {
            throw new IllegalArgumentException(EMessageException.RANGE_NULL.toString());
        }

        if (range.compareTo(BigDecimal.valueOf(deltaMin)) <= 0) {
            throw new IllegalArgumentException(EMessageException.DELTA_MIN_GE_RANGE.toString());
        }
        if (range.compareTo(BigDecimal.valueOf(Double.MAX_VALUE).multiply(BigDecimal.valueOf(2.))) > 0) {
            throw new IllegalArgumentException(EMessageException.RANGE_GT_MAX_VALUE_TIME_2.toString());
        }

        this.deltaMin = deltaMin;
        this.nbGeometricSteps = StandardDM.computeNbGeometricStepsFromRange(deltaMin, range,
            this.deltaEvolution.getIncreaseFactor());
        this.deltaMax = this.deltaMin
            * Math.pow(this.deltaEvolution.getIncreaseFactor(), (double) this.nbGeometricSteps - 1);

        // ensure consistency of current delta
        if (this.delta > this.deltaMax) {
            this.delta = this.deltaMax;
        }
    }

    /**
     * Compute nb geometric steps from range.
     *
     * @param deltaMin
     *            the delta min
     * @param range
     *            the range
     * @param geometricFactor
     *            the geometric factor
     * @return the int
     */
    public static int computeNbGeometricStepsFromRange(double deltaMin, BigDecimal range, double geometricFactor) {

        if (Double.isNaN(deltaMin)) {
            throw new IllegalArgumentException(EMessageException.DELTA_MIN_NAN.toString());
        }

        int n; // search for a n value : the number of geometric step

        BigDecimal rangeDivDelta = range.divide(BigDecimal.valueOf(deltaMin), MathContext.DECIMAL128);

        n = 1;
        BigDecimal expr = BigDecimal.valueOf(geometricFactor + n)
            .multiply(BigDecimal.valueOf(geometricFactor).pow(n - 1)).subtract(BigDecimal.valueOf(1));

        while (expr.compareTo(rangeDivDelta) < 0) {
            n = n + 1;
            expr = BigDecimal.valueOf(geometricFactor + n).multiply(BigDecimal.valueOf(geometricFactor).pow(n - 1))
                .subtract(BigDecimal.valueOf(1));
        }

        if (n == 1) {
            return 1;
        }

        // Choice of the integer value that makes (geometricFactor + n) *
        // Math.pow(geometricFactor, n-1) - 1
        // most close to range / deltaMin
        BigDecimal k = BigDecimal.valueOf(geometricFactor + n - 1)
            .multiply(BigDecimal.valueOf(geometricFactor).pow(n - 2)).subtract(BigDecimal.valueOf(1))
            .subtract(rangeDivDelta).abs();

        BigDecimal l = BigDecimal.valueOf(geometricFactor + n).multiply(BigDecimal.valueOf(geometricFactor).pow(n - 1))
            .subtract(BigDecimal.valueOf(1)).subtract(rangeDivDelta).abs();
        if (k.compareTo(l) < 0) {
            return n - 1;
        }
        else {
            return n;
        }
    }

    /**
     * Compute nb geometric steps from delta max.
     *
     * @param deltaMin
     *            the delta min
     * @param deltaMax
     *            the delta max
     * @param geometricFactor
     *            the geometric factor
     * @return the int
     */
    public static int computeNbGeometricStepsFromDeltaMax(double deltaMin, double deltaMax, double geometricFactor) {
        if (Double.isNaN(deltaMin)) {
            throw new IllegalArgumentException(EMessageException.LOWER_BOUND_NAN.toString());
        }

        if (Double.isNaN(deltaMax)) {
            throw new IllegalArgumentException(EMessageException.UPPER_BOUND_NAN.toString());
        }

        if (Double.isNaN(geometricFactor)) {
            throw new IllegalArgumentException(EMessageException.GEOMETRIC_FACTOR_BOUND_NAN.toString());
        }

        return (int) Math.ceil((Math.log(deltaMax) - Math.log(deltaMin)) / Math.log(geometricFactor)) + 1;
    }

    /**
     * Sets the geometric step number.
     *
     * @param geometricStepNumber
     *            the new geometric step number
     * @throws IllegalArgumentException
     *             if geometricStepNumber < 1 or geometricStepNumber >
     *             this.getNbGeometricSteps()
     */
    @Override
    public void setGeometricStepNumber(int geometricStepNumber) {
        if (geometricStepNumber < 1 || geometricStepNumber > this.getNbGeometricSteps()) {
            throw new IllegalArgumentException(
                EMessageException.GEOMETRIC_STEP_NUMBER_BOUND.toString());
        }

        this.delta = this.deltaMin
            * Math.pow(this.deltaEvolution.getIncreaseFactor(), (double) geometricStepNumber - 1);

        this.dmDecision.resetState(); // in order to forget the history
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#getDeltaIf(fr.
     * irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection)
     */
    @Override
    public double getDeltaIf(EDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException(EMessageException.DIRECTION_NULL.toString());
        }
        double deltaIf = this.delta;
        EDecision decision = this.dmDecision.getNextDecisionIf(direction);
        if (decision == EDecision.INCREASE_DELTA) {
            deltaIf = this.getIncreasedDelta();
        }
        else if (decision == EDecision.DECREASE_DELTA) {
            deltaIf = this.getDecreasedDelta();
        }

        return deltaIf;
    }
}
