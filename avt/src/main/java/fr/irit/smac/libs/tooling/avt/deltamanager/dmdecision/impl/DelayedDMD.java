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

public class DelayedDMD extends ForwardingDMD {

    private final int increaseDelay;
    private final int decreaseDelay;

    private int       increaseRequestCount;
    private int       decreaseRequestCount;

    public DelayedDMD(IDMDecision dmDecison, int increaseDelay, int decreaseDelay) {
        super(dmDecison);
        this.increaseDelay = increaseDelay;
        this.decreaseDelay = decreaseDelay;

        this.resetState();
    }

    private boolean isIncreaseAllowed() {
        this.decreaseRequestCount = 0;

        if (this.increaseRequestCount <= this.increaseDelay) {
            this.increaseRequestCount++;
        }

        return this.increaseRequestCount > this.increaseDelay;
    }

    private boolean isDecreaseAllowed() {
        this.increaseRequestCount = 0;

        if (this.decreaseRequestCount <= this.decreaseDelay) {
            this.decreaseRequestCount++;
        }

        return this.decreaseRequestCount > this.decreaseDelay;
    }

    private boolean isIncreaseAllowedIf() {
        int anIncreaseRequestCount = this.increaseRequestCount;
        if (anIncreaseRequestCount <= this.increaseDelay) {
            anIncreaseRequestCount++;
        }

        return anIncreaseRequestCount > this.increaseDelay;
    }

    private boolean isDecreaseAllowedIf() {
        int aDecreaseRequestCount = this.decreaseRequestCount;

        if (aDecreaseRequestCount <= this.decreaseDelay) {
            aDecreaseRequestCount++;
        }

        return aDecreaseRequestCount > this.decreaseDelay;
    }

    @Override
    public EDecision getNextDecision(EDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException("direction is null");
        }

        EDecision d = super.getNextDecision(direction);

        if (d == EDecision.SAME_DELTA) {
            this.resetState();
        }
        else if (d == EDecision.INCREASE_DELTA) {
            d = this.isIncreaseAllowed() ? d : EDecision.SAME_DELTA;
        }
        else if (d == EDecision.DECREASE_DELTA) {
            d = this.isDecreaseAllowed() ? d : EDecision.SAME_DELTA;
        }

        return d;
    }

    @Override
    public EDecision getNextDecisionIf(EDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException("direction is null");
        }

        EDecision d = super.getNextDecisionIf(direction);

        if (d == EDecision.INCREASE_DELTA) {
            d = this.isIncreaseAllowedIf() ? d : EDecision.SAME_DELTA;
        }
        else if (d == EDecision.DECREASE_DELTA) {
            d = this.isDecreaseAllowedIf() ? d : EDecision.SAME_DELTA;
        }

        return d;
    }

    @Override
    public void resetState() {
        super.resetState();

        this.increaseRequestCount = 0;
        this.decreaseRequestCount = 0;
    }
}
