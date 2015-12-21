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

import fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM;
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDE;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision.EDecision;
import fr.irit.smac.libs.tooling.avt.range.IRange;

public class StandardDM implements IAdvancedDM {

    private final IGeometricDE  deltaEvolution;
    private final IDMDecision   dmDecision;
    private final IRange         range;

    private double              delta;
    private double              deltaMin;
    private double              deltaMax;
    private int                 nbGeometricSteps;

    private static final String DELTA_MIN_NAN                        = "deltaMin isNaN";
    private static final String RANGE_NULL                           = "range is null";
    private static final String DELTA_MIN_LE_0                       = "deltaMin <= 0";
    private static final String DELTA_EVOLUTION_NULL                 = "deltaEvolution == null";
    private static final String DELTA_EVOLUTION_INCREASE_FACTOR_LE_1 = "deltaEvolution.getIncreaseFactor() <= 1.";
    private static final String DELTA_MAX_NAN                        = "deltaMax isNaN";
    private static final String DELTA_MAX_LT_DELTA_MIN               = "deltaMax < deltaMin";
    private static final String DELTA_MIN_GT_DELTA_MAX               = "deltaMin > this.deltaMax";
    private static final String DIRECTION_NULL                       = "direction is null";
    private static final String DELTA_NULL                           = "delta isNaN";
    private static final String DELTA_MAX_LT_THIS_DELTA_MIN          = "deltaMax < this.deltaMin";
    private static final String LOWER_BOUND_NAN                      = "lowerBound isNaN";
    private static final String UPPER_BOUND_NAN                      = "upperBound isNaN";
    private static final String GEOMETRIC_FACTOR_BOUND_NAN           = "geometricFactor isNaN";
    private static final String DELTA_MIN_GE_RANGE                   = "deltaMin => range";
    private static final String RANGE_GT_MAX_VALUE_TIME_2            = "range > Double.MAX_VALUE * 2";
    private static final String GEOMETRIC_STEP_NUMBER_BOUND          = "if geometricStepNumber < 1 or geometricStepNumber > this.getNbGeometricSteps()";

    /**
     * 
     * 
     * @param deltaMin
     * @param range
     * @param deltaEvolution
     * @throws IllegalArgumentException
     *             if deltaMin <= 0
     * @throws IllegalArgumentException
     *             if range == null
     * @throws IllegalArgumentException
     *             if deltaEvolution == null
     * @throws IllegalArgumentException
     *             if deltaEvolution.getIncreaseFactor() <= 1.;
     */
    public StandardDM(double deltaMin, IRange range, IGeometricDE deltaEvolution, IDMDecision dmDecision) {

        if (Double.isNaN(deltaMin)) {
            throw new IllegalArgumentException(DELTA_MIN_NAN);
        }

        if (range == null) {
            throw new IllegalArgumentException(RANGE_NULL);
        }

        // Check for invalid arguments
        if (deltaMin <= 0) {
            throw new IllegalArgumentException(DELTA_MIN_LE_0);
        }

        if (deltaEvolution == null) {
            throw new IllegalArgumentException(DELTA_EVOLUTION_NULL);
        }
        if (deltaEvolution.getIncreaseFactor() <= 1.) {
            throw new IllegalArgumentException(DELTA_EVOLUTION_INCREASE_FACTOR_LE_1);
        }

        this.range = range;
        this.deltaMin = deltaMin;
        this.deltaEvolution = deltaEvolution;
        this.dmDecision = dmDecision;
        this.reconfigure(deltaMin);
        this.resetState();
    }

    /**
     * 
     * @param deltaMin
     * @param deltaMax
     * @param avt
     * @param deltaEvolution
     * @throws IllegalArgumentException
     *             if deltaMin <= 0
     * @throws IllegalArgumentException
     *             if avt == null
     * @throws IllegalArgumentException
     *             if deltaEvolution == null
     * @throws IllegalArgumentException
     *             if deltaMax < deltaMin
     * @throws IllegalArgumentException
     *             if deltaEvolution.getIncreaseFactor() <= 1.
     */
    public StandardDM(double deltaMin, double deltaMax, IRange range, IGeometricDE deltaEvolution,
        IDMDecision dmDecision) {

        if (Double.isNaN(deltaMin)) {
            throw new IllegalArgumentException(DELTA_MIN_NAN);
        }

        if (Double.isNaN(deltaMax)) {
            throw new IllegalArgumentException(DELTA_MAX_NAN);
        }

        if (range == null) {
            throw new IllegalArgumentException(RANGE_NULL);
        }

        // Check for invalid arguments
        if (deltaMin <= 0) {
            throw new IllegalArgumentException(DELTA_MIN_LE_0);
        }
        if (deltaEvolution == null) {
            throw new IllegalArgumentException(DELTA_EVOLUTION_NULL);
        }
        if (deltaMax < deltaMin) {
            throw new IllegalArgumentException(DELTA_MAX_LT_DELTA_MIN);
        }
        if (deltaEvolution.getIncreaseFactor() <= 1.) {
            throw new IllegalArgumentException(DELTA_EVOLUTION_INCREASE_FACTOR_LE_1);
        }

        this.range = range;
        this.deltaMin = deltaMin;
        this.deltaMax = deltaMax;
        this.nbGeometricSteps = StandardDM.computeNbGeometricStepsFromDeltaMax(deltaMin, deltaMax,
            deltaEvolution.getIncreaseFactor());
        this.dmDecision = dmDecision;
        this.deltaEvolution = deltaEvolution;
        this.resetState();
    }

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

    @Override
    public void adjustDelta(EDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException(DIRECTION_NULL);
        }

        EDecision decision = this.dmDecision.getNextDecision(direction);
        if (decision == EDecision.INCREASE_DELTA) {
            this.delta = this.getIncreasedDelta();
        }
        else if (decision == EDecision.DECREASE_DELTA) {
            this.delta = this.getDecreasedDelta();
        }
    }

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

    protected double getDecreasedDelta() {
        return this.deltaEvolution.getDecreasedDelta(this.delta);
    }

    @Override
    public double getDelta() {
        return this.delta;
    }

    @Override
    public IAdvancedDM getAdvancedDM() {
        return this;
    }

    @Override
    public double getDeltaMin() {
        return this.deltaMin;
    }

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
            throw new IllegalArgumentException(DELTA_MIN_NAN);
        }

        if (deltaMin > this.deltaMax) {
            throw new IllegalArgumentException(DELTA_MIN_GT_DELTA_MAX);
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
            throw new IllegalArgumentException(DELTA_MAX_NAN);
        }

        if (deltaMax < this.deltaMin) {
            throw new IllegalArgumentException(DELTA_MAX_LT_THIS_DELTA_MIN);
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
            throw new IllegalArgumentException(DELTA_NULL);
        }

        this.delta = delta;
    }

    @Override
    public int getGeometricStepNumber() {
        return (int) Math.ceil((Math.log(Math.max(this.delta, this.deltaMin)) - Math.log(this.deltaMin))
            / Math.log(this.deltaEvolution.getIncreaseFactor())) + 1;
    }

    @Override
    public int getNbGeometricSteps() {
        return this.nbGeometricSteps;
    }

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

        if (Double.isNaN(deltaMin)) {
            throw new IllegalArgumentException(DELTA_MIN_NAN);
        }

        if (deltaMin <= 0) {
            throw new IllegalArgumentException(DELTA_MIN_LE_0);
        }
        if (range == null) {
            throw new IllegalArgumentException(RANGE_NULL);
        }

        if (range.compareTo(BigDecimal.valueOf(deltaMin)) <= 0) {
            throw new IllegalArgumentException(DELTA_MIN_GE_RANGE);
        }
        if (range.compareTo(BigDecimal.valueOf(Double.MAX_VALUE).multiply(BigDecimal.valueOf(2.))) > 0) {
            throw new IllegalArgumentException(RANGE_GT_MAX_VALUE_TIME_2);
        }

        this.deltaMin = deltaMin;
        this.nbGeometricSteps = StandardDM.computeNbGeometricStepsFromRange(deltaMin, range,
            this.deltaEvolution.getIncreaseFactor());
        this.deltaMax = this.deltaMin * Math.pow(this.deltaEvolution.getIncreaseFactor(), this.nbGeometricSteps - 1);

        // ensure consistency of current delta
        if (this.delta > this.deltaMax) {
            this.delta = this.deltaMax;
        }
    }

    public static int computeNbGeometricStepsFromRange(double deltaMin, BigDecimal range, double geometricFactor) {

        if (Double.isNaN(deltaMin)) {
            throw new IllegalArgumentException(DELTA_MIN_NAN);
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

    public static int computeNbGeometricStepsFromDeltaMax(double deltaMin, double deltaMax, double geometricFactor) {
        if (Double.isNaN(deltaMin)) {
            throw new IllegalArgumentException(LOWER_BOUND_NAN);
        }

        if (Double.isNaN(deltaMax)) {
            throw new IllegalArgumentException(UPPER_BOUND_NAN);
        }

        if (Double.isNaN(geometricFactor)) {
            throw new IllegalArgumentException(GEOMETRIC_FACTOR_BOUND_NAN);
        }

        return (int) Math.ceil((Math.log(deltaMax) - Math.log(deltaMin)) / Math.log(geometricFactor)) + 1;
    }

    /**
     * @throws IllegalArgumentException
     *             if geometricStepNumber < 1 or geometricStepNumber >
     *             this.getNbGeometricSteps()
     */
    @Override
    public void setGeometricStepNumber(int geometricStepNumber) {
        if (geometricStepNumber < 1 || geometricStepNumber > this.getNbGeometricSteps()) {
            throw new IllegalArgumentException(
                GEOMETRIC_STEP_NUMBER_BOUND);
        }

        this.delta = this.deltaMin * Math.pow(this.deltaEvolution.getIncreaseFactor(), (double) geometricStepNumber - 1);

        this.dmDecision.resetState(); // in order to forget the history
    }

    @Override
    public double getDeltaIf(EDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException(DIRECTION_NULL);
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
