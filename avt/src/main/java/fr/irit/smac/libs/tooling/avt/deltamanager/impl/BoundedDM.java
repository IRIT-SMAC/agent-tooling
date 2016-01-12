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
package fr.irit.smac.libs.tooling.avt.deltamanager.impl;

import java.math.BigDecimal;

import fr.irit.smac.libs.tooling.avt.EMessageException;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager;

/**
 * The Class BoundedDM.
 */
public class BoundedDM extends ForwardingDM {

    /**
     * Instantiates a new bounded dm.
     *
     * @param dm the dm
     * @throws IllegalArgumentException             if dm == null
     */
    public BoundedDM(IDeltaManager dm) {
        super(dm);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.impl.ForwardingDM#adjustDelta(fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection)
     */
    @Override
    public void adjustDelta(EDirection direction) {
        super.adjustDelta(direction);
        this.ensureBoundedDelta();
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.impl.ForwardingDM#reconfigure(double, java.math.BigDecimal)
     */
    @Override
    public void reconfigure(double deltaMin, BigDecimal range) {
        super.reconfigure(deltaMin, range);
        this.ensureBoundedDelta();
    }

    /**
     * Ensure bounded delta.
     */
    private void ensureBoundedDelta() {
        double currentDelta = this.getBoundedDelta(this.dm.getDelta());
        if (!BigDecimal.valueOf(currentDelta).equals(BigDecimal.valueOf(this.dm.getDelta()))) {
            this.dm.getAdvancedDM().setDelta(currentDelta);
        }
    }

    /**
     * Gets the bounded delta.
     *
     * @param delta the delta
     * @return the bounded delta
     */
    private double getBoundedDelta(double delta) {
        if (Double.isNaN(delta)) {
            throw new IllegalArgumentException(EMessageException.DELTA_NAN.toString());
        }
        double currentDelta = delta;
        if (currentDelta < this.dm.getAdvancedDM().getDeltaMin()) {
            currentDelta = this.dm.getAdvancedDM().getDeltaMin();
        }
        return currentDelta;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.impl.ForwardingDM#getDeltaIf(fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection)
     */
    @Override
    public double getDeltaIf(EDirection direction) {
        double delta = super.getDeltaIf(direction);
        return this.getBoundedDelta(delta);
    }

}
