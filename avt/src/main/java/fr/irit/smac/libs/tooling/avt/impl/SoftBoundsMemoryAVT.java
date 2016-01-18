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

import java.util.ArrayDeque;
import java.util.Queue;

import fr.irit.smac.libs.tooling.avt.EFeedback;
import fr.irit.smac.libs.tooling.avt.EMessageException;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory;

/**
 * The Class SoftBoundsMemoryAVT.
 * 
 * <p>
 * The values taken are stored in an history. The bounds are updated
 * according to the values contained in the history.
 * </p>
 * 
 * <p>
 * The SoftBoundsMemoryAVT is useful when the research area is unknown.
 * </p>
 */
public class SoftBoundsMemoryAVT extends StandardAVT {

    /** The memory size. */
    private final int           memorySize;

    /** The values history. */
    private final Queue<Double> valuesHistory;

    /**
     * Instantiates a new soft bounds memory avt.
     *
     * @param lowerBound
     *            the lower bound
     * @param upperBound
     *            the upper bound
     * @param startValue
     *            the start value
     * @param deltaManagerFactory
     *            the delta manager factory
     * @param memorySize
     *            the memory size
     */
    public SoftBoundsMemoryAVT(double lowerBound, double upperBound, double startValue,
        IDeltaManagerFactory<?> deltaManagerFactory, int memorySize) {
        super(lowerBound, upperBound, startValue, deltaManagerFactory);
        this.memorySize = memorySize;

        // memory size plus 1 because we add the new value before removing
        // the head of the queue (see registerNewValueAndUpdateBounds)
        this.valuesHistory = new ArrayDeque<Double>(this.memorySize + 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.impl.StandardAVT#setLowerBound(double)
     */
    @Override
    public void setLowerBound(double lowerBound) {
        super.setLowerBound(lowerBound);
        this.updateHistoryFromBounds();
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
        this.updateHistoryFromBounds();
        this.updateValueFromBounds();
        this.updateDMFromBounds();
    }

    /**
     * Update history from bounds.
     */
    private void updateHistoryFromBounds() {

        Double[] values = this.valuesHistory.toArray(new Double[0]);
        this.valuesHistory.clear();
        for (Double d : values) {
            this.valuesHistory.add(d >= this.range.getLowerBound()
                ? (d <= this.range.getUpperBound() ? d : this.range.getUpperBound()) : this.range.getLowerBound());
        }
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
        this.registerNewValueAndUpdateBounds(value);

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

        // 3 - register the new value in the history
        this.registerNewValueAndUpdateBounds(this.value);
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
     * Register new value and update bounds.
     *
     * @param value
     *            the value
     */
    private void registerNewValueAndUpdateBounds(double value) {

        this.valuesHistory.add(value);

        boolean boundsUpdatedFromValue = this.updateBoundsFromNewValue(value);
        boolean boundsUpdatedFromHistory = this.updateBoundsFromHistory();

        if (boundsUpdatedFromValue || boundsUpdatedFromHistory) {
            this.updateDMFromBounds();
        }
    }

    /**
     * Update bounds from history.
     *
     * @return true, if successful
     */
    private boolean updateBoundsFromHistory() {
        boolean boundsUpdated = false;

        if (this.valuesHistory.size() >= this.memorySize) {
            double oldestValue = this.valuesHistory.poll();
            // if the oldestValue is the max of the history and different to
            // the current upperBound then update
            if (this.isHistoryMax(oldestValue)
                && oldestValue < this.getRange().getUpperBound()) {
                super.setUpperBound(oldestValue);
                boundsUpdated = true;
            } // the same reasoning applied to lower bound
            else if (this.isHistoryMin(oldestValue)
                && oldestValue > this.getRange().getLowerBound()) {
                super.setLowerBound(oldestValue);
                boundsUpdated = true;
            }
        }

        return boundsUpdated;
    }

    /**
     * Checks if is history min.
     *
     * @param value
     *            the value
     * @return true, if is history min
     */
    // optimized history comparison (stop at first min occurence)
    private boolean isHistoryMin(Double value) {
        for (Double d : this.valuesHistory) {
            if (d < value) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if is history max.
     *
     * @param value
     *            the value
     * @return true, if is history max
     */
    // optimized history comparison (stop at first max occurence)
    private boolean isHistoryMax(Double value) {
        for (Double d : this.valuesHistory) {
            if (d > value) {
                return false;
            }
        }
        return true;
    }

    /**
     * Update bounds from new value.
     *
     * @param value
     *            the value
     * @return true, if successful
     */
    private boolean updateBoundsFromNewValue(double value) {

        boolean boundsUpdated = false;

        if (this.getRange().getLowerBound() > value) {
            super.setLowerBound(value);
            boundsUpdated = true;
        }
        else if (this.getRange().getUpperBound() < value) {
            super.setUpperBound(value);
            boundsUpdated = true;
        }

        return boundsUpdated;
    }

    /**
     * Sets the value from bounds.
     *
     * @param value
     *            the new value from bounds
     */
    private void setValueFromBounds(double value) {
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
