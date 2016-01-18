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
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory;

/**
 * The Class SoftBoundsAVT.
 * 
 * <p>
 * If the value exceeds the bounds, the bounds are updated according to the
 * value.
 * </p>
 * <p>
 * The SoftBoundsAVT is useful when the research area is unknown.
 * </p>
 */
public class SoftBoundsAVT extends StandardAVT {

    /**
     * Instantiates a new soft bounds avt.
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
    public SoftBoundsAVT(double lowerBound, double upperBound, double startValue,
        IDeltaManagerFactory<?> deltaManagerFactory) {
        super(lowerBound, upperBound, startValue, deltaManagerFactory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.impl.StandardAVT#setLowerBound(double)
     */
    @Override
    public void setLowerBound(double lowerBound) {
        super.setLowerBound(lowerBound);
        this.updateValueFromBounds();
        this.updateDMFromBounds();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.impl.StandardAVT#setUpperBound(double)
     */
    @Override
    public void setUpperBound(double upperBound) {
        super.setUpperBound(upperBound);
        this.updateValueFromBounds();
        this.updateDMFromBounds();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.impl.StandardAVT#setValue(double)
     */
    @Override
    public void setValue(double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(EMessageException.VALUE_NAN.toString());
        }
        this.value = value;

        if (!this.getRange().isInsideRange(this.value)) {
            this.updateBoundsFromValue(this.value);
        }

        // the value has been changed and then the lasts feedbacks are
        // not related to this new value
        this.deltaManager.getAdvancedDM().resetState();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.impl.StandardAVT#adjustValue(fr.irit.smac
     * .libs.tooling.avt.EFeedback)
     */
    @Override
    public void adjustValue(EFeedback feedback) {
        if (feedback == null) {
            throw new IllegalArgumentException(EMessageException.FEEDBACK_NULL.toString());
        }

        // 1 - Updates the delta value
        this.updateDelta(feedback);

        // 2 - Adjust the current value within the Double representation
        if (feedback != EFeedback.GOOD) {
            this.value = Math.min(Double.MAX_VALUE, Math.max(-Double.MAX_VALUE,
                this.value + this.deltaManager.getDelta() * (feedback == EFeedback.GREATER ? 1 : -1)));
        }

        // 3 - if value is outside the bounds then move the bounds
        if (!this.range.isInsideRange(this.value)) {
            this.updateBoundsFromValue(this.value);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.impl.StandardAVT#getValueIf(fr.irit.smac
     * .libs.tooling.avt.EFeedback)
     */
    @Override
    public double getValueIf(EFeedback feedback) {
        if (feedback == null) {
            throw new IllegalArgumentException(EMessageException.FEEDBACK_NULL.toString());
        }

        double valueIf = this.value;

        // 1 - Updates the delta value
        double delta = this.deltaManager.getAdvancedDM().getDeltaIf(this.getDirectionFromFreedback(feedback));

        // 2 - Adjust the current value within the Double representation
        if (feedback != EFeedback.GOOD) {
            valueIf = Math.min(Double.MAX_VALUE,
                Math.max(-Double.MAX_VALUE, valueIf + delta * (feedback == EFeedback.GREATER ? 1 : -1)));
        }

        return valueIf;
    }

    /**
     * Update bounds from value.
     *
     * @param value
     *            the value
     */
    private void updateBoundsFromValue(double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(EMessageException.VALUE_NAN.toString());
        }

        if (this.getRange().getLowerBound() > value) {
            this.setLowerBoundFromValue(value);
        }

        if (this.getRange().getUpperBound() < value) {
            this.setUpperBoundFromValue(value);
        }

    }

    /**
     * Sets the lower bound from value.
     *
     * @param lowerBound
     *            the new lower bound from value
     */
    private void setLowerBoundFromValue(double lowerBound) {
        if (Double.isNaN(lowerBound)) {
            throw new IllegalArgumentException(EMessageException.LOWER_BOUND_NAN.toString());
        }

        super.setLowerBound(lowerBound);
        this.updateDMFromBounds();
    }

    /**
     * Sets the upper bound from value.
     *
     * @param upperBound
     *            the new upper bound from value
     */
    private void setUpperBoundFromValue(double upperBound) {
        if (Double.isNaN(upperBound)) {
            throw new IllegalArgumentException(EMessageException.UPPER_BOUND_NAN.toString());
        }
        super.setUpperBound(upperBound);
        this.updateDMFromBounds();
    }

    /**
     * Sets the value from bounds.
     *
     * @param value
     *            the new value from bounds
     */
    private void setValueFromBounds(double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(EMessageException.VALUE_NAN.toString());
        }
        this.value = value;

        // the value has been changed and then the lasts feedbacks are
        // not related to this new value
        this.deltaManager.getAdvancedDM().resetState();
    }

    /**
     * Update value from bounds.
     */
    private void updateValueFromBounds() {
        if (this.effectiveLowerBound > this.value) {
            this.setValueFromBounds(this.effectiveLowerBound);
        }
        else if (this.effectiveUpperBound < this.value) {
            this.setValueFromBounds(this.effectiveUpperBound);
        }
    }
}
