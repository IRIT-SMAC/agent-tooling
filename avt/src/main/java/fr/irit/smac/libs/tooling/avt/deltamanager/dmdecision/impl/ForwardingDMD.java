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

import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision;

public class ForwardingDMD implements IDMDecision {

    private final IDMDecision dmDecison;

    public ForwardingDMD(IDMDecision dmDecison) {
        super();
        if (dmDecison == null) {
            throw new IllegalArgumentException("dmDecison == null");
        }
        this.dmDecison = dmDecison;
    }

    @Override
    public EDecision getNextDecision(EDirection direction) {
        return this.dmDecison.getNextDecision(direction);
    }

    @Override
    public void resetState() {
        this.dmDecison.resetState();
    }

    @Override
    public EDecision getNextDecisionIf(EDirection direction) {
        return this.dmDecison.getNextDecisionIf(direction);
    }

}
