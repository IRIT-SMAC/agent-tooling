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
 * The Class ForwardingDMD.
 * 
 * <p>
 * ForwardingDMD allows to encapsulate another implementation to implement a
 * different aspect to this implementation.
 * </p>
 */
public class ForwardingDMD implements IDMDecision {

    /** The dm decison. */
    private final IDMDecision dmDecison;

    /**
     * Instantiates a new forwarding dmd.
     *
     * @param dmDecison
     *            the dm decison
     */
    public ForwardingDMD(IDMDecision dmDecison) {
        super();
        if (dmDecison == null) {
            throw new IllegalArgumentException(EMessageException.DM_DECISION.toString());
        }
        this.dmDecison = dmDecison;
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
        return this.dmDecison.getNextDecision(direction);
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
        this.dmDecison.resetState();
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
        return this.dmDecison.getNextDecisionIf(direction);
    }

}
