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
package fr.irit.smac.libs.tooling.avt.impl;

import fr.irit.smac.libs.tooling.avt.EFeedback;
import fr.irit.smac.libs.tooling.avt.EMessageException;
import fr.irit.smac.libs.tooling.avt.IAdvancedAVT;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory;
import fr.irit.smac.libs.tooling.avt.range.IMutableRange;
import fr.irit.smac.libs.tooling.avt.range.IRange;
import fr.irit.smac.libs.tooling.avt.range.impl.MutableRangeImpl;

/**
 * The Class StandardAVT.
 */
public class StandardAVT implements IAdvancedAVT {

    /** The range. */
    protected IMutableRange       range;

    /** The value. */
    protected double              value;

    /**
     * The effective lower bound. Bounds that are effectively used when updating
     * value (cause lowerBound and upperBound can be Double.(POSITIVE or
     * NEGATIVE)_INFINITY)
     */
    protected double              effectiveLowerBound;

    /** The effective upper bound. */
    protected double              effectiveUpperBound;

    /** The delta manager. */
    protected final IDeltaManager deltaManager;

    /**
     * Constructs a StandardAVT.
     *
     * @param lowerBound
     *            the lower bound
     * @param upperBound
     *            the upper bound
     * @param startValue
     *            the start value
     * @param deltaManagerFactory
     *            the delta manager factory
     */
    public StandardAVT(double lowerBound, double upperBound, double startValue,
        IDeltaManagerFactory<?> deltaManagerFactory) {
        super();

        if (Double.isNaN(lowerBound)) {
            throw new IllegalArgumentException(EMessageException.LOWER_BOUND_NAN.toString());
        }

        if (Double.isNaN(upperBound)) {
            throw new IllegalArgumentException(EMessageException.UPPER_BOUND_NAN.toString());
        }

        if (Double.isNaN(startValue)) {
            throw new IllegalArgumentException(EMessageException.START_VALUE_NAN.toString());
        }

        if (deltaManagerFactory == null) {
            throw new IllegalArgumentException(EMessageException.DELTA_MANAGER_FACTORY_NULL.toString());
        }

        this.range = new MutableRangeImpl(lowerBound, upperBound);
        this.effectiveLowerBound = getNonInfiniteEquivalOf(lowerBound);
        this.effectiveUpperBound = getNonInfiniteEquivalOf(upperBound);
        this.value = startValue;
        this.deltaManager = deltaManagerFactory.createInstance(this.range);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.IAVT#getValue()
     */
    @Override
    public double getValue() {
        return this.value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.IAdvancedAVT#getRange()
     */
    @Override
    public IRange getRange() {
        return this.range;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.IAdvancedAVT#setLowerBound(double)
     */
    @Override
    public void setLowerBound(double lowerBound) {

        this.range.setLowerBound(lowerBound);
        this.effectiveLowerBound = getNonInfiniteEquivalOf(this.getRange().getLowerBound());
        this.ensureValueBoundsConsistency();
        this.updateDMFromBounds();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.IAdvancedAVT#setUpperBound(double)
     */
    @Override
    public void setUpperBound(double upperBound) {

        this.range.setUpperBound(upperBound);
        this.effectiveUpperBound = getNonInfiniteEquivalOf(this.range.getUpperBound());
        this.ensureValueBoundsConsistency();
        this.updateDMFromBounds();
    }

    /**
     * Ensure value bounds consistency.
     */
    private void ensureValueBoundsConsistency() {
        if (this.value > this.effectiveUpperBound) {
            this.setValue(this.effectiveUpperBound);
        }
        else if (this.value < this.effectiveLowerBound) {
            this.setValue(this.effectiveLowerBound);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.IAdvancedAVT#setValue(double)
     */
    @Override
    public void setValue(double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(EMessageException.START_VALUE_NAN.toString());
        }

        if (!this.range.isInsideRange(value)) {
            throw new IllegalArgumentException("the value to set \"" + value + "\" is outside " + this.range);
        }

        this.value = value;

        // the value has been changed and then the lasts decisions are
        // not related to this new value
        this.deltaManager.getAdvancedDM().resetState();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.IAdvancedAVT#getDeltaManager()
     */
    @Override
    public IDeltaManager getDeltaManager() {
        return this.deltaManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.IAVT#getAdvancedAVT()
     */
    @Override
    public IAdvancedAVT getAdvancedAVT() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.IAVT#getCriticity()
     */
    @Override
    public double getCriticity() {
        double criticity = 0;
        // if there is only one geometric step, then the AVT is never critical
        if (this.deltaManager.getAdvancedDM().getNbGeometricSteps() > 1) {
            criticity = ((double) this.deltaManager.getAdvancedDM().getGeometricStepNumber() - 1)
                / ((double) this.deltaManager.getAdvancedDM().getNbGeometricSteps() - 1);
        }

        return criticity;
    }

    /**
     * Sets the criticity.
     *
     * @param criticity
     *            the new criticity
     * @throws IllegalArgumentException
     *             if value > 1 or value < 0
     */
    @Override
    public void setCriticity(double criticity) {
        if (Double.isNaN(criticity)) {
            throw new IllegalArgumentException(EMessageException.CRITICITY_NAN.toString());
        }

        if (value < 0) {
            throw new IllegalArgumentException(EMessageException.CRITICITY_LT_0.toString());
        }
        else if (value > 1) {
            throw new IllegalArgumentException(EMessageException.CRITICITY_GT_1.toString());
        }

        int geometricStepNumber = (int) Math.round(criticity * this.deltaManager.getAdvancedDM().getNbGeometricSteps());
        this.deltaManager.getAdvancedDM().setGeometricStepNumber(geometricStepNumber);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.IAVT#adjustValue(fr.irit.smac.libs.tooling
     * .avt.EFeedback)
     */
    @Override
    public void adjustValue(EFeedback feedback) {
        if (feedback == null) {
            throw new IllegalArgumentException(EMessageException.FEEDBACK_NULL.toString());
        }

        // 1 - If the avt will stay at a bound then the real feedback is good
        EFeedback newFeedback = this.willStayAtBounds(feedback) ? EFeedback.GOOD : feedback;

        // 2 - Updates the delta value
        this.updateDelta(newFeedback);

        // 3 - Adjust the current value
        if (newFeedback != EFeedback.GOOD) {
            this.value = Math.min(this.effectiveUpperBound, Math.max(this.effectiveLowerBound,
                this.value + this.deltaManager.getDelta() * (newFeedback == EFeedback.GREATER ? 1 : -1)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.IAdvancedAVT#getValueIf(fr.irit.smac.libs
     * .tooling.avt.EFeedback)
     */
    @Override
    public double getValueIf(EFeedback feedback) {
        if (feedback == null) {
            throw new IllegalArgumentException(EMessageException.FEEDBACK_NULL.toString());
        }

        double valueIf = this.value;

        // 1 - If the avt will stay at a bound then the real feedback is good
        EFeedback newFeedback = this.willStayAtBounds(feedback) ? EFeedback.GOOD : feedback;

        // 2 - Updates the delta value
        double delta = this.deltaManager.getAdvancedDM().getDeltaIf(this.getDirectionFromFreedback(newFeedback));

        // 3 - Adjust the current value
        if (newFeedback != EFeedback.GOOD) {
            valueIf = Math.min(this.effectiveUpperBound,
                Math.max(this.effectiveLowerBound, valueIf + delta * (newFeedback == EFeedback.GREATER ? 1 : -1)));
        }

        return valueIf;
    }

    /**
     * Will stay at bounds.
     *
     * @param feedback
     *            the feedback
     * @return true, if successful
     */
    private boolean willStayAtBounds(EFeedback feedback) {

        return (this.value <= this.effectiveLowerBound && feedback == EFeedback.LOWER)
            || (this.value >= this.effectiveUpperBound && feedback == EFeedback.GREATER);
    }

    /**
     * Update delta.
     *
     * @param feedback
     *            the feedback
     */
    protected void updateDelta(EFeedback feedback) {
        if (feedback == null) {
            throw new IllegalArgumentException(EMessageException.FEEDBACK_NULL.toString());
        }

        this.deltaManager.adjustDelta(this.getDirectionFromFreedback(feedback));
    }

    /**
     * Gets the direction from freedback.
     *
     * @param feedback
     *            the feedback
     * @return the direction from freedback
     */
    protected EDirection getDirectionFromFreedback(EFeedback feedback) {
        if (feedback == EFeedback.GREATER) {
            return EDirection.DIRECT;
        }
        else if (feedback == EFeedback.LOWER) {
            return EDirection.INDIRECT;
        }
        else {
            return EDirection.NONE;
        }
    }

    /**
     * Update dm from bounds.
     */
    protected void updateDMFromBounds() {
        // WARNING : an exception is thrown if the new range is lower to
        // deltaMin
        // a possible solution may be to set a deltaMin that is equal to
        // to the minimum between range and deltaMin itself
        // but it brakes the contract given at initialization of the
        // AVT
        this.deltaManager.getAdvancedDM().reconfigure(this.deltaManager.getAdvancedDM().getDeltaMin());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AVT [v=" + value + " delta=" + this.deltaManager.getDelta() + "]";
    }

    /**
     * Gets the non infinite equival of.
     *
     * @param value
     *            the value
     * @return the non infinite equival of
     */
    private static double getNonInfiniteEquivalOf(double value) {
        double finiteVal;

        if (value <= Double.NEGATIVE_INFINITY) {
            finiteVal = -Double.MAX_VALUE;
        }
        else if (value >= Double.POSITIVE_INFINITY) {
            finiteVal = Double.MAX_VALUE;
        }
        else {
            finiteVal = value;
        }

        return finiteVal;
    }

}
