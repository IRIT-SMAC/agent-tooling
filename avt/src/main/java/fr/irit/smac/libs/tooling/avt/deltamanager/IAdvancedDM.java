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
package fr.irit.smac.libs.tooling.avt.deltamanager;

import java.math.BigDecimal;

/**
 * Advanced interface to control the DeltaManager.
 * <p>
 * Pay caution, some of those methods don't guarantee the DeltaManager state
 * consistency. Generally, you should only use this interface if you have good
 * reasons and you know what you are doing.
 * </p>
 * <p>
 * The state consistency guaranty depends on the implementation, so please refer
 * to the java doc of these implementations. The methods that doesn't guarantee
 * this consistency are tagged with a <strong>STATE CONSISTENCY
 * WARNING.</strong>
 * </p>
 * 
 * @author Sylvain Lemouzy
 */
public interface IAdvancedDM extends IDeltaManager {

    /**
     * Returns the delta min value.
     * 
     * @return the delta min value
     */
    public double getDeltaMin();

    /**
     * Returns the delta max value.
     * 
     * @return the delta max value
     */
    public double getDeltaMax();

    /**
     * Returns the next delta if it would be adjusted towards the given
     * direction.
     *
     * @param direction the direction
     * @return the next delta if it would be adjusted towards the given
     *         direction
     */
    public double getDeltaIf(EDirection direction);

    /**
     * Returns the number of geometric steps used for the adjustment of delta.
     * <p>
     * This value is actually used to figure out the criticity value of the AVT.
     * </p>
     * 
     * @return the number of geometric steps used for the adjustment of delta.
     */
    public int getNbGeometricSteps();

    /**
     * Returns the number of the geometric step corresponding to the current
     * delta value.
     * 
     * @return the number of the geometric step corresponding to the current
     *         delta value.
     */
    public int getGeometricStepNumber();

    /**
     * Sets the number of the current geometric step.
     * <p>
     * Setting this step number will change the value of delta, in order to keep
     * consistency between these two values.
     * </p>
     *
     * @param geometricStepNumber the new geometric step number
     */
    public void setGeometricStepNumber(int geometricStepNumber);

    /**
     * Sets the deltaMin value.
     * <p>
     * <strong>STATE CONSISTENCY WARNING:</strong> this method doesn't guaranty
     * the state consistency of the DeltaManager
     * </p>
     * <p>
     * Only call this method for tweeky initialisation purpose and if you know
     * what you do.
     * </p>
     * If you want to change the deltaMin value and be sure delta max is
     * reconfigured from the delta min value and the range of the AVT call the
     * <strong>reconfigure(...)</strong> method.
     * 
     * @param deltaMin
     *            the deltaMin to set
     * @throws IllegalArgumentException
     *             if <code>deltaMin > this.getDeltaMax()</code>
     */
    public void setDeltaMin(double deltaMin);

    /**
     * Sets the deltaMax value.
     * <p>
     * <strong>STATE CONSISTENCY WARNING:</strong> this method doesn't guaranty
     * the state consistency of the DeltaManager
     * </p>
     * Only call this method for tweeky initialisation purpose and if you know
     * what you do
     * 
     * @param deltaMax
     *            the deltaMax to set
     * @throws IllegalArgumentException
     *             if <code>deltaMax < this.getDeltaMin()</code>
     */
    public void setDeltaMax(double deltaMax);

    /**
     * Sets the delta value.
     * <p>
     * <strong>STATE CONSISTENCY WARNING:</strong> this method doesn't guaranty
     * the state consistency of the DeltaManager
     * </p>
     * Only call this method for tweeky initialisation purpose and if you know
     * what you do
     * 
     * @param delta
     *            the value to set
     */
    public void setDelta(double delta);

    /**
     * Reconfigure the values of delta min, delta max and nbGeometricSteps with
     * the given new delta min and the range of the avt.
     *
     * @param deltaMin            minimal delta value (or tuning step)
     */
    public void reconfigure(double deltaMin);

    /**
     * Reconfigure the values of delta min, delta max and nbGeometricSteps.
     * 
     * <strong>STATE CONSISTENCY WARNING:</strong> this method will ignore the
     * real range of the AVT (eg. size of its search space).
     * 
     * @param deltaMin
     *            minimal delta value (or tuning step)
     * @param range
     *            the size of the search space the deltaManager is supposed to
     *            evolve in.
     */
    public void reconfigure(double deltaMin, BigDecimal range);

    /**
     * Reinitialize the state of the Delta manager.
     * <p>
     * Actually this method reset the delta value and the geometric step value.
     * </p>
     */
    public void resetState();

}
