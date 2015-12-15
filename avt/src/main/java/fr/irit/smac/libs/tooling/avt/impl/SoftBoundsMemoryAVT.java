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

import fr.irit.smac.libs.tooling.avt.Feedback;
import fr.irit.smac.libs.tooling.avt.deltamanager.DeltaManagerFactory;

public class SoftBoundsMemoryAVT extends StandardAVT {

	private final int memorySize;
	private final Queue<Double> valuesHistory;

	public SoftBoundsMemoryAVT(double lowerBound, double upperBound, double startValue,
			DeltaManagerFactory<?> deltaManagerFactory, int memorySize) {
		super(lowerBound, upperBound, startValue, deltaManagerFactory);
		this.memorySize = memorySize;

		// memory size plus 1 because we add the new value before removing
		// the head of the queue (see registerNewValueAndUpdateBounds)
		this.valuesHistory = new ArrayDeque<Double>(this.memorySize + 1);
	}

	@Override
	public void setLowerBound(double lowerBound) {
		super.setLowerBound(lowerBound);
		this.updateHistoryFromBounds();
		this.updateValueFromBounds();
		this.updateDMFromBounds();
	}

	@Override
	public void setUpperBound(double upperBound) {
		super.setUpperBound(upperBound);
		this.updateHistoryFromBounds();
		this.updateValueFromBounds();
		this.updateDMFromBounds();
	}

	private void updateHistoryFromBounds() {

		Double[] values = this.valuesHistory.toArray(new Double[0]);
		this.valuesHistory.clear();
		for (Double d : values) {
			this.valuesHistory.add((d >= this.range.getLowerBound()
					? (d <= this.range.getUpperBound() ? d : this.range.getUpperBound()) : this.range.getLowerBound()));
		}
	}

	@Override
	public void setValue(double value) {
		if (Double.isNaN(value)) {
			throw new IllegalArgumentException("value isNaN");
		}
		this.value = value;
		this.registerNewValueAndUpdateBounds(value);

		// the value has been changed and then the lasts feedbacks are
		// not related to this new value
		this.deltaManager.getAdvancedDM().resetState();
	}

	@Override
	public void adjustValue(Feedback feedback) {

		if (feedback == null) {
			throw new IllegalArgumentException("feedback is null");
		}

		// 1 - Updates the delta value
		this.updateDelta(feedback);

		// 2 - Adjust the current value within the Double representation
		if (feedback != Feedback.GOOD) {
			this.value = Math.min(Double.MAX_VALUE, Math.max(-Double.MAX_VALUE,
					this.value + this.deltaManager.getDelta() * (feedback == Feedback.GREATER ? 1 : -1)));
		}

		// 3 - register the new value in the history
		this.registerNewValueAndUpdateBounds(this.value);
	}

	@Override
	public double getValueIf(Feedback feedback) {
		if (feedback == null) {
			throw new IllegalArgumentException("feedback is null");
		}

		double valueIf = this.value;

		// 1 - Updates the delta value
		double delta = this.deltaManager.getAdvancedDM().getDeltaIf(this.getDirectionFromFreedback(feedback));

		// 2 - Adjust the current value within the Double representation
		if (feedback != Feedback.GOOD) {
			valueIf = Math.min(Double.MAX_VALUE,
					Math.max(-Double.MAX_VALUE, valueIf + delta * (feedback == Feedback.GREATER ? 1 : -1)));
		}

		return valueIf;
	}

	private void registerNewValueAndUpdateBounds(double value) {

		this.valuesHistory.add(value);

		boolean boundsUpdatedFromValue = this.updateBoundsFromNewValue(value);
		boolean boundsUpdatedFromHistory = this.updateBoundsFromHistory();

		if (boundsUpdatedFromValue || boundsUpdatedFromHistory) {
			this.updateDMFromBounds();
		}
	}

	private boolean updateBoundsFromHistory() {
		boolean boundsUpdated = false;

		if (this.valuesHistory.size() >= this.memorySize) {
			double oldestValue = this.valuesHistory.poll();
			// if the oldestValue is the max of the history and different to
			// the current upperBound then update
			if (this.isHistoryMax(oldestValue) && oldestValue != this.getRange().getUpperBound()) {
				super.setUpperBound(oldestValue);
				boundsUpdated = true;
			} // the same reasoning applied to lower bound
			else if (this.isHistoryMin(oldestValue) && oldestValue != this.getRange().getLowerBound()) {
				super.setLowerBound(oldestValue);
				boundsUpdated = true;
			}
		}

		return boundsUpdated;
	}

	// optimized history comparison (stop at first min occurence)
	private boolean isHistoryMin(Double value) {
		for (Double d : this.valuesHistory) {
			if (d < value) {
				return false;
			}
		}
		return true;
	}

	// optimized history comparison (stop at first max occurence)
	private boolean isHistoryMax(Double value) {
		for (Double d : this.valuesHistory) {
			if (d > value) {
				return false;
			}
		}
		return true;
	}

	private boolean updateBoundsFromNewValue(double value) {

		boolean boundsUpdated = false;

		if (this.getRange().getLowerBound() > value) {
			super.setLowerBound(value);
			boundsUpdated = true;
		} else if (this.getRange().getUpperBound() < value) {
			super.setUpperBound(value);
			boundsUpdated = true;
		}

		return boundsUpdated;
	}

	private void setValueFromBounds(double value) {
		this.value = value;

		// the value has been changed and then the lasts feedbacks are
		// not related to this new value
		this.deltaManager.getAdvancedDM().resetState();
	}

	private void updateValueFromBounds() {
		if (this.effectiveLowerBound > this.value) {
			this.setValueFromBounds(this.effectiveLowerBound);
		} else if (this.effectiveUpperBound < this.value) {
			this.setValueFromBounds(this.effectiveUpperBound);
		}
	}
}
