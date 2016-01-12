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

/**
 * Definition of the basic interface of a DeltaManager.
 * 
 * A DeltaManager can be used two way:
 * <ul>
 * <li>by sequentially calling the <code>adjustDelta</code> method</li>
 * <li>or by sequentially calling either <code>increaseDelta</code> or
 * <code>decreaseDelta</code> methods.</li>
 * </ul>
 * 
 * @author Sylvain Lemouzy
 */
public interface IDeltaManager {

    /**
     * Defines the direction given to the method <code>adjustDelta</code> in
     * order to update the tunning step delta.
     * 
     * @author Sylvain Lemouzy
     */
    public enum EDirection {

        /** The direct. */
        DIRECT,
        /** The indirect. */
        INDIRECT,
        /** The none. */
        NONE
    }

    /**
     * Adjusts (tunes) the delta value from the succession of directions
     * successively given to this method.
     * <p>
     * The way the adjustment is done depends on the implementation of the
     * DeltaManager
     * </p>
     * 
     * @param direction
     *            the direction of the adjustment
     */
    public void adjustDelta(EDirection direction);

    /**
     * Returns the delta value.
     * 
     * @return the delta value
     */
    public double getDelta();

    /**
     * Returns an advanced interface to control the DeltaManager.
     * <p>
     * Pay caution, some of the methods of this interface doesn't guarantee the
     * DeltaManager state consistency. Generally, you should only uses this
     * interface if you have good reasons and you know what you are doing.
     * </p>
     * 
     * @return this AVT instance with an advanced interface
     */
    public IAdvancedDM getAdvancedDM();
}
