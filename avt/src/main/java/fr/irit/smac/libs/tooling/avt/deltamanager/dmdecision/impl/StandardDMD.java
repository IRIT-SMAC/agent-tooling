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
package fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl;

import fr.irit.smac.libs.tooling.avt.EMessageException;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision;

/**
 * The Class StandardDMD.
 * 
 * <p>
 * If two consecutive feedbacks are identical, delta is increased. Otherwise,
 * delta is decreased.
 * </p>
 */
public class StandardDMD implements IDMDecision {

    /** The last direction. */
    private EDirection lastDirection;

    /**
     * Instantiates a new standard dmd.
     */
    public StandardDMD() {
        this.resetState();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision#
     * getNextDecision
     * (fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection)
     */
    @Override
    public EDecision getNextDecision(EDirection direction) {

        EDecision decision = this.getNextDecisionIf(direction);
        this.lastDirection = direction;

        return decision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision#resetState
     * ()
     */
    @Override
    public void resetState() {
        this.lastDirection = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision#
     * getNextDecisionIf
     * (fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection)
     */
    @Override
    public EDecision getNextDecisionIf(EDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException(EMessageException.DIRECTION_NULL.toString());
        }

        EDecision decision = null;

        if (this.lastDirection == EDirection.NONE) {
            if (direction == EDirection.NONE) {
                decision = EDecision.DECREASE_DELTA;
            }
            else {
                decision = EDecision.INCREASE_DELTA;
            }
        }
        else if (this.lastDirection != null) {
            if (this.lastDirection != direction) {
                decision = EDecision.DECREASE_DELTA;
            }
            else {
                decision = EDecision.INCREASE_DELTA;
            }
        }

        return decision;
    }

}
