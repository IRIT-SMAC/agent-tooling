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

import fr.irit.smac.libs.tooling.avt.IAdvancedAVT;
import fr.irit.smac.libs.tooling.avt.EFeedback;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory;
import fr.irit.smac.libs.tooling.avt.range.IMutableRange;
import fr.irit.smac.libs.tooling.avt.range.IRange;
import fr.irit.smac.libs.tooling.avt.range.impl.MutableRangeImpl;

public class StandardAVT implements IAdvancedAVT {

    protected IMutableRange        range;
    protected double              value;

    // bounds that are effectively used when updating
    // value (cause lowerBound and upperBound can be
    // Double.(POSITIVE or NEGATIVE)_INFINITY
    protected double              effectiveLowerBound;
    protected double              effectiveUpperBound;

    protected final IDeltaManager deltaManager;

    private static final String   LOWER_BOUND_NAN            = "lowerBound isNaN";
    private static final String   UPPER_BOUND_NAN            = "upperBound isNaN";
    private static final String   START_VALUE_NAN            = "startValue isNaN";
    private static final String   DELTA_MANAGER_FACTORY_NULL = "deltaManagerFactory == null";
    private static final String   VALUE_NAN                  = "value isNaN";
    private static final String   FEEDBACK_NULL              = "feedback is null";
    private static final String   CRITICITY_NAN              = "criticity isNaN";
    private static final String   CRITICITY_LT_0             = "criticity < 0";
    private static final String   CRITICITY_GT_1             = "criticity > 1";

    /**
     * Constructs a StandardAVT
     * 
     * @param lowerBound
     * @param upperBound
     * @param startValue
     * @param deltaManagerFactory
     */
    public StandardAVT(double lowerBound, double upperBound, double startValue,
        IDeltaManagerFactory<?> deltaManagerFactory) {
        super();

        if (Double.isNaN(lowerBound)) {
            throw new IllegalArgumentException(LOWER_BOUND_NAN);
        }

        if (Double.isNaN(upperBound)) {
            throw new IllegalArgumentException(UPPER_BOUND_NAN);
        }

        if (Double.isNaN(startValue)) {
            throw new IllegalArgumentException(START_VALUE_NAN);
        }

        if (deltaManagerFactory == null) {
            throw new IllegalArgumentException(DELTA_MANAGER_FACTORY_NULL);
        }

        this.range = new MutableRangeImpl(lowerBound, upperBound);
        this.effectiveLowerBound = getNonInfiniteEquivalOf(lowerBound);
        this.effectiveUpperBound = getNonInfiniteEquivalOf(upperBound);
        this.value = startValue;
        this.deltaManager = deltaManagerFactory.createInstance(this.range);
    }

    @Override
    public double getValue() {
        return this.value;
    }

    @Override
    public IRange getRange() {
        return this.range;
    }

    @Override
    public void setLowerBound(double lowerBound) {

        this.range.setLowerBound(lowerBound);
        this.effectiveLowerBound = getNonInfiniteEquivalOf(this.getRange().getLowerBound());
        this.ensureValueBoundsConsistency();
        this.updateDMFromBounds();
    }

    /**
     * 
     */
    @Override
    public void setUpperBound(double upperBound) {

        this.range.setUpperBound(upperBound);
        this.effectiveUpperBound = getNonInfiniteEquivalOf(this.range.getUpperBound());
        this.ensureValueBoundsConsistency();
        this.updateDMFromBounds();
    }

    private void ensureValueBoundsConsistency() {
        if (this.value > this.effectiveUpperBound) {
            this.setValue(this.effectiveUpperBound);
        }
        else if (this.value < this.effectiveLowerBound) {
            this.setValue(this.effectiveLowerBound);
        }
    }

    @Override
    public void setValue(double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(VALUE_NAN);
        }

        if (!this.range.isInsideRange(value)) {
            throw new IllegalArgumentException("the value to set \"" + value + "\" is outside " + this.range);
        }

        this.value = value;

        // the value has been changed and then the lasts decisions are
        // not related to this new value
        this.deltaManager.getAdvancedDM().resetState();
    }

    @Override
    public IDeltaManager getDeltaManager() {
        return this.deltaManager;
    }

    @Override
    public IAdvancedAVT getAdvancedAVT() {
        return this;
    }

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
     * @throws IllegalArgumentException
     *             if value > 1 or value < 0
     */
    @Override
    public void setCriticity(double criticity) {
        if (Double.isNaN(criticity)) {
            throw new IllegalArgumentException(CRITICITY_NAN);
        }

        if (value < 0) {
            throw new IllegalArgumentException(CRITICITY_LT_0);
        }
        else if (value > 1) {
            throw new IllegalArgumentException(CRITICITY_GT_1);
        }

        int geometricStepNumber = (int) Math.round(criticity * this.deltaManager.getAdvancedDM().getNbGeometricSteps());
        this.deltaManager.getAdvancedDM().setGeometricStepNumber(geometricStepNumber);
    }

    @Override
    public void adjustValue(EFeedback feedback) {
        if (feedback == null) {
            throw new IllegalArgumentException(FEEDBACK_NULL);
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

    @Override
    public double getValueIf(EFeedback feedback) {
        if (feedback == null) {
            throw new IllegalArgumentException(FEEDBACK_NULL);
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

    private boolean willStayAtBounds(EFeedback feedback) {
        return (this.value == this.effectiveLowerBound && feedback == EFeedback.LOWER)
            || (this.value == this.effectiveUpperBound && feedback == EFeedback.GREATER);
    }

    protected void updateDelta(EFeedback feedback) {
        if (feedback == null) {
            throw new IllegalArgumentException(FEEDBACK_NULL);
        }

        this.deltaManager.adjustDelta(this.getDirectionFromFreedback(feedback));
    }

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

    protected void updateDMFromBounds() {
        // WARNING : an exception is thrown if the new range is lower to
        // deltaMin
        // a possible solution may be to set a deltaMin that is equal to
        // to the minimum between range and deltaMin itself
        // but it brakes the contract given at initialization of the
        // AVT
        this.deltaManager.getAdvancedDM().reconfigure(this.deltaManager.getAdvancedDM().getDeltaMin());
    }

    @Override
    public String toString() {
        return "AVT [v=" + value + " delta=" + this.deltaManager.getDelta() + "]";
    }

    private static double getNonInfiniteEquivalOf(double value) {
        double finiteVal;

        if (value == Double.NEGATIVE_INFINITY) {
            finiteVal = -Double.MAX_VALUE;
        }
        else if (value == Double.POSITIVE_INFINITY) {
            finiteVal = Double.MAX_VALUE;
        }
        else {
            finiteVal = value;
        }

        return finiteVal;
    }

}
