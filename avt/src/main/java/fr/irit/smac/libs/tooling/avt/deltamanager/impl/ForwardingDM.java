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

import fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager;

/**
 * The Class ForwardingDM.
 * 
 * <p>
 * ForwardingDM allows to encapsulate another implementation to implement a
 * different aspect to this implementation.
 * </p>
 */
public class ForwardingDM implements IAdvancedDM {

    /** The dm. */
    protected final IDeltaManager dm;

    /**
     * Instantiates a new forwarding dm.
     *
     * @param dm
     *            the dm
     * @throws IllegalArgumentException
     *             if dm == null
     */
    public ForwardingDM(IDeltaManager dm) {
        super();
        if (dm == null) {
            throw new IllegalArgumentException("dm == null");
        }

        this.dm = dm;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager#adjustDelta(
     * fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection)
     */
    @Override
    public void adjustDelta(EDirection direction) {
        this.dm.adjustDelta(direction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager#getDelta()
     */
    @Override
    public double getDelta() {
        return this.dm.getDelta();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager#getAdvancedDM()
     */
    @Override
    public IAdvancedDM getAdvancedDM() {
        return this; // caution, ensure return this and not the reference
                     // of the forwarded object
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#getDeltaMin()
     */
    @Override
    public double getDeltaMin() {
        return this.dm.getAdvancedDM().getDeltaMin();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#getDeltaMax()
     */
    @Override
    public double getDeltaMax() {
        return this.dm.getAdvancedDM().getDeltaMax();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#setDeltaMin(double
     * )
     */
    @Override
    public void setDeltaMin(double deltaMin) {
        this.dm.getAdvancedDM().setDeltaMin(deltaMin);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#setDeltaMax(double
     * )
     */
    @Override
    public void setDeltaMax(double deltaMax) {
        this.dm.getAdvancedDM().setDeltaMax(deltaMax);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#setDelta(double)
     */
    @Override
    public void setDelta(double delta) {
        this.dm.getAdvancedDM().setDelta(delta);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#getGeometricStepNumber
     * ()
     */
    @Override
    public int getGeometricStepNumber() {
        return this.dm.getAdvancedDM().getGeometricStepNumber();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#getNbGeometricSteps
     * ()
     */
    @Override
    public int getNbGeometricSteps() {
        return this.dm.getAdvancedDM().getNbGeometricSteps();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#reconfigure(double
     * )
     */
    @Override
    public void reconfigure(double deltaMin) {
        this.dm.getAdvancedDM().reconfigure(deltaMin);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#reconfigure(double
     * , java.math.BigDecimal)
     */
    @Override
    public void reconfigure(double deltaMin, BigDecimal range) {
        this.dm.getAdvancedDM().reconfigure(deltaMin, range);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#setGeometricStepNumber
     * (int)
     */
    @Override
    public void setGeometricStepNumber(int geometricStepNumber) {
        this.dm.getAdvancedDM().setGeometricStepNumber(geometricStepNumber);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#resetState()
     */
    @Override
    public void resetState() {
        this.dm.getAdvancedDM().resetState();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM#getDeltaIf(fr.
     * irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection)
     */
    @Override
    public double getDeltaIf(EDirection direction) {
        return this.dm.getAdvancedDM().getDeltaIf(direction);
    }

}
