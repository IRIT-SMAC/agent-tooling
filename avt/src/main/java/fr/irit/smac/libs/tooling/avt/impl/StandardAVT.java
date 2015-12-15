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

import fr.irit.smac.libs.tooling.avt.AdvancedAVT;
import fr.irit.smac.libs.tooling.avt.Feedback;
import fr.irit.smac.libs.tooling.avt.deltamanager.DeltaManager;
import fr.irit.smac.libs.tooling.avt.deltamanager.DeltaManager.Direction;
import fr.irit.smac.libs.tooling.avt.deltamanager.DeltaManagerFactory;
import fr.irit.smac.libs.tooling.avt.range.MutableRange;
import fr.irit.smac.libs.tooling.avt.range.Range;
import fr.irit.smac.libs.tooling.avt.range.impl.MutableRangeImpl;

public class StandardAVT implements AdvancedAVT {

	protected MutableRange range;
	protected double value;

	// bounds that are effectively used when updating
	// value (cause lowerBound and upperBound can be
	// Double.(POSITIVE or NEGATIVE)_INFINITY
	protected double effectiveLowerBound;
	protected double effectiveUpperBound;

	protected final DeltaManager deltaManager;

	/**
	 * Constructs a StandardAVT
	 * 
	 * @param lowerBound
	 * @param upperBound
	 * @param startValue
	 * @param deltaManagerFactory
	 */
	public StandardAVT(double lowerBound, double upperBound, double startValue,
			DeltaManagerFactory<?> deltaManagerFactory) {
		super();

		if (Double.isNaN(lowerBound)) {
			throw new IllegalArgumentException("lowerBound isNaN");
		}

		if (Double.isNaN(upperBound)) {
			throw new IllegalArgumentException("upperBound isNaN");
		}

		if (Double.isNaN(startValue)) {
			throw new IllegalArgumentException("startValue isNaN");
		}

		if (deltaManagerFactory == null) {
			throw new IllegalArgumentException("deltaManagerFactory == null");
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
	public Range getRange() {
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
		} else if (this.value < this.effectiveLowerBound) {
			this.setValue(this.effectiveLowerBound);
		}
	}

	@Override
	public void setValue(double value) {
		if (Double.isNaN(value)) {
			throw new IllegalArgumentException("value isNaN");
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
	public DeltaManager getDeltaManager() {
		return this.deltaManager;
	}

	@Override
	public AdvancedAVT getAdvancedAVT() {
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
			throw new IllegalArgumentException("criticity isNaN");
		}

		if (value < 0) {
			throw new IllegalArgumentException("criticity < 0");
		} else if (value > 1) {
			throw new IllegalArgumentException("criticity > 1");
		}

		int geometricStepNumber = (int) Math.round(criticity * this.deltaManager.getAdvancedDM().getNbGeometricSteps());
		this.deltaManager.getAdvancedDM().setGeometricStepNumber(geometricStepNumber);
	}

	@Override
	public void adjustValue(Feedback feedback) {
		if (feedback == null) {
			throw new IllegalArgumentException("feedback is null");
		}

		// 1 - If the avt will stay at a bound then the real feedback is good
		feedback = this.willStayAtBounds(feedback) ? Feedback.GOOD : feedback;

		// 2 - Updates the delta value
		this.updateDelta(feedback);

		// 3 - Adjust the current value
		if (feedback != Feedback.GOOD) {
			this.value = Math.min(this.effectiveUpperBound, Math.max(this.effectiveLowerBound,
					this.value + this.deltaManager.getDelta() * (feedback == Feedback.GREATER ? 1 : -1)));
		}
	}

	@Override
	public double getValueIf(Feedback feedback) {
		if (feedback == null) {
			throw new IllegalArgumentException("feedback is null");
		}

		double valueIf = this.value;

		// 1 - If the avt will stay at a bound then the real feedback is good
		feedback = this.willStayAtBounds(feedback) ? Feedback.GOOD : feedback;

		// 2 - Updates the delta value
		double delta = this.deltaManager.getAdvancedDM().getDeltaIf(this.getDirectionFromFreedback(feedback));

		// 3 - Adjust the current value
		if (feedback != Feedback.GOOD) {
			valueIf = Math.min(this.effectiveUpperBound,
					Math.max(this.effectiveLowerBound, valueIf + delta * (feedback == Feedback.GREATER ? 1 : -1)));
		}

		return valueIf;
	}

	private boolean willStayAtBounds(Feedback feedback) {
		return (this.value == this.effectiveLowerBound && feedback == Feedback.LOWER)
				|| (this.value == this.effectiveUpperBound && feedback == Feedback.GREATER);
	}

	protected void updateDelta(Feedback feedback) {
		if (feedback == null) {
			throw new IllegalArgumentException("feedback is null");
		}

		this.deltaManager.adjustDelta(this.getDirectionFromFreedback(feedback));
	}

	protected Direction getDirectionFromFreedback(Feedback feedback) {
		if (feedback == Feedback.GREATER) {
			return Direction.DIRECT;
		} else if (feedback == Feedback.LOWER) {
			return Direction.INDIRECT;
		} else {
			return Direction.NONE;
		}
	}

	protected void updateDMFromBounds() {
		// WARNING : an exception is thrown if the new range is lower to deltaMin
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
		} else if (value == Double.POSITIVE_INFINITY) {
			finiteVal = Double.MAX_VALUE;
		} else {
			finiteVal = value;
		}

		return finiteVal;
	}

}
