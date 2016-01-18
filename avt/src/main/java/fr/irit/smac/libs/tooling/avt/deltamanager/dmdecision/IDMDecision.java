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
package fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision;

import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection;

/**
 * The Interface IDMDecision.
 * 
 * <p>
 * A DMDecision decides on the direction of the evolution of the delta.
 * </p>
 */
public interface IDMDecision {

    /**
     * The Enum EDecision.
     */
    public enum EDecision {

        /** The increase delta. */
        INCREASE_DELTA,
        /** The decrease delta. */
        DECREASE_DELTA,
        /** The same delta. */
        SAME_DELTA
    }

    /**
     * Gets the next decision.
     *
     * @param direction
     *            the direction
     * @return the next decision
     */
    public EDecision getNextDecision(EDirection direction);

    /**
     * Gets the next decision if.
     *
     * @param direction
     *            the direction
     * @return the next decision if
     */
    public EDecision getNextDecisionIf(EDirection direction);

    /**
     * Reset state.
     */
    public void resetState();
}
