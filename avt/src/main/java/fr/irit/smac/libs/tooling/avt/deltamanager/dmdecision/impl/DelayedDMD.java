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
 * The Class DelayedDMD.
 * 
 * <p>
 * DelayedDMD allows to choose the number of times the DeltaManager have to wait
 * before deciding on the direction of the evolution of the delta.
 * </p>
 */
public class DelayedDMD extends ForwardingDMD {

    /** The increase delay. */
    private final int increaseDelay;

    /** The decrease delay. */
    private final int decreaseDelay;

    /** The increase request count. */
    private int       increaseRequestCount;

    /** The decrease request count. */
    private int       decreaseRequestCount;

    /**
     * Instantiates a new delayed dmd.
     *
     * @param dmDecison
     *            the dm decison
     * @param increaseDelay
     *            the increase delay
     * @param decreaseDelay
     *            the decrease delay
     */
    public DelayedDMD(IDMDecision dmDecison, int increaseDelay, int decreaseDelay) {
        super(dmDecison);
        this.increaseDelay = increaseDelay;
        this.decreaseDelay = decreaseDelay;

        this.resetState();
    }

    /**
     * Checks if is increase allowed.
     *
     * @return true, if is increase allowed
     */
    private boolean isIncreaseAllowed() {
        this.decreaseRequestCount = 0;

        if (this.increaseRequestCount <= this.increaseDelay) {
            this.increaseRequestCount++;
        }

        return this.increaseRequestCount > this.increaseDelay;
    }

    /**
     * Checks if is decrease allowed.
     *
     * @return true, if is decrease allowed
     */
    private boolean isDecreaseAllowed() {
        this.increaseRequestCount = 0;

        if (this.decreaseRequestCount <= this.decreaseDelay) {
            this.decreaseRequestCount++;
        }

        return this.decreaseRequestCount > this.decreaseDelay;
    }

    /**
     * Checks if is increase allowed if.
     *
     * @return true, if is increase allowed if
     */
    private boolean isIncreaseAllowedIf() {
        int anIncreaseRequestCount = this.increaseRequestCount;
        if (anIncreaseRequestCount <= this.increaseDelay) {
            anIncreaseRequestCount++;
        }

        return anIncreaseRequestCount > this.increaseDelay;
    }

    /**
     * Checks if is decrease allowed if.
     *
     * @return true, if is decrease allowed if
     */
    private boolean isDecreaseAllowedIf() {
        int aDecreaseRequestCount = this.decreaseRequestCount;

        if (aDecreaseRequestCount <= this.decreaseDelay) {
            aDecreaseRequestCount++;
        }

        return aDecreaseRequestCount > this.decreaseDelay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.ForwardingDMD
     * #getNextDecision
     * (fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection)
     */
    @Override
    public EDecision getNextDecision(EDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException(EMessageException.DIRECTION_NULL.toString());
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.ForwardingDMD
     * #getNextDecisionIf
     * (fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection)
     */
    @Override
    public EDecision getNextDecisionIf(EDirection direction) {
        if (direction == null) {
            throw new IllegalArgumentException(EMessageException.DIRECTION_NULL.toString());
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.ForwardingDMD
     * #resetState()
     */
    @Override
    public void resetState() {
        super.resetState();

        this.increaseRequestCount = 0;
        this.decreaseRequestCount = 0;
    }
}
