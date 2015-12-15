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
package fr.irit.smac.libs.tooling.avt;

import fr.irit.smac.libs.tooling.avt.deltamanager.DeltaManager;
import fr.irit.smac.libs.tooling.avt.range.Range;

/**
 * Advanced interface to control the AVT.
 * <p>
 * Pay caution, some of those methods don't guarantee the AVT state consistency. Generally, you should only use this
 * interface if you have good reasons and you know what you are doing.
 * </p>
 * <p>
 * The state consistency guaranty depends on the implementation, so please refer to the java doc of these
 * implementations. The methods that doesn't guarantee this consistency are tagged with a <strong>STATE CONSISTENCY
 * WARNING</strong>
 * </p>
 * 
 * @author Sylvain Lemouzy
 */
public interface AdvancedAVT extends AVT {

	/**
	 * Returns the next value of the AVT if it'd receive the given feedback
	 * 
	 * @param feedback
	 * @return the next value of the AVT if it'd receive the given feedback
	 */
	public double getValueIf(Feedback feedback);

	/**
	 * @return The range that represents the AVT search space
	 */
	public Range getRange();

	/**
	 * Sets the lower bound of this AVT.
	 * <p>
	 * If the current value is lower than the new <code>lowerBound</code> then the current value is set to
	 * <code>lowerBound</code>. In that case the AVT state is reinitialized in order to keep state consistency.
	 * </p>
	 * <p>
	 * The state consistency of the AVT is also ensured by the reconfiguration of the DeltaManager from the new bounds
	 * values.
	 * </p>
	 * 
	 * @param lowerBound
	 *            lower bound to be set
	 * @throws IllegalArgumentException
	 *             if the lowerBound to set is greater than the current upperBound (
	 *             <code>if (lowerBound > this.getUpperBound())</code>)
	 */
	public void setLowerBound(double lowerBound);

	/**
	 * Sets the upper bound of this AVT.
	 * <p>
	 * If the current value is higher than the new <code>upperBound</code> then the current value is set to
	 * <code>upperBound</code>. In that case the AVT state is reinitialized in order to keep state consistency.
	 * </p>
	 * <p>
	 * The state consistency of the AVT is also ensured by the reconfiguration of the DeltaManager from the new bounds
	 * values.
	 * </p>
	 * 
	 * @param upperBound
	 *            upper bound to be set
	 * @throws IllegalArgumentException
	 *             if the upperBound to set is lower than the current lowerBound (
	 *             <code>if (upperBound < this.getLowerBound())</code>)
	 */
	public void setUpperBound(double upperBound);

	/**
	 * Sets the current value.
	 * <p>
	 * In order to keep state consistency, the AVT state is reinitialized.
	 * </p>
	 * 
	 * @param value
	 *            the value to be set
	 * @throws IllegalArgumentException
	 *             if the specified value is outside the bounds (
	 *             <code>this.value < this.getLowerBound() || this.value > this.getUpperBound()</code>)
	 */
	public void setValue(double value);

	/**
	 * Sets the criticity of the AVT.
	 * <p>
	 * Changing the criticity will change the AVT value accuracy estimation. Thus, it will change the way the AVT
	 * adjusts its value :
	 * </p>
	 * <ul>
	 * <li>If <code>criticity = 1</code> then the adjustment step will be big</li>
	 * <li>If <code>criticity = 0</code> then the adjustment step will be small</li>
	 * </ul>
	 * 
	 * @param value
	 *            the criticity value to set (value must belong to [0.; 1.])
	 * @throws IllegalArgumentException
	 *             if value > 1 or value < 0
	 */
	public void setCriticity(double value);

	/**
	 * The delta manager used to determine the step adjustment value.
	 * 
	 * @return the delta manager
	 */
	public DeltaManager getDeltaManager();
}
