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
package fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl;

import fr.irit.smac.libs.tooling.avt.EMessageException;
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDE;

/**
 * The Class DeterministicGDE.
 * 
 * <p>
 * If the delta has to increase, it is multiplied by an increaseFactor. If the
 * delta has to decrease, it is divided by a decreaseFactor.
 * </p>
 */
public class DeterministicGDE implements IGeometricDE {

    /** The decrease factor. */
    protected final double decreaseFactor;

    /** The increase factor. */
    protected final double increaseFactor;

    /**
     * Instantiates a new deterministic gde.
     *
     * @param increaseFactor
     *            the increase factor used for delta decrease
     * @param decreaseFactor
     *            the decrease factor used for delta decrease
     * @throws IllegalArgumentException
     *             if increaseFactor < 1
     */
    public DeterministicGDE(double increaseFactor, double decreaseFactor) {

        if (Double.isNaN(increaseFactor)) {
            throw new IllegalArgumentException(EMessageException.INCREASE_FACTOR_NAN.toString());
        }

        if (Double.isNaN(decreaseFactor)) {
            throw new IllegalArgumentException(EMessageException.DECREASE_FACTOR_NAN.toString());
        }

        if (decreaseFactor < 1) {
            throw new IllegalArgumentException(EMessageException.DECREASE_FACTOR_LT_1.toString());
        }

        if (increaseFactor < 1) {
            throw new IllegalArgumentException(EMessageException.INCREASE_FACTOR_LT_1.toString());
        }

        this.increaseFactor = increaseFactor;
        this.decreaseFactor = decreaseFactor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IDeltaEvolution
     * #getIncreasedDelta(double)
     */
    @Override
    public double getIncreasedDelta(double delta) {
        if (Double.isNaN(delta)) {
            throw new IllegalArgumentException(EMessageException.INCREASE_FACTOR_DELTA.toString());
        }

        return delta * this.increaseFactor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IDeltaEvolution
     * #getDecreasedDelta(double)
     */
    @Override
    public double getDecreasedDelta(double delta) {
        if (Double.isNaN(delta)) {
            throw new IllegalArgumentException(EMessageException.INCREASE_FACTOR_DELTA.toString());
        }

        return delta / this.decreaseFactor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDE
     * #getIncreaseFactor()
     */
    @Override
    public double getIncreaseFactor() {
        return this.increaseFactor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDE
     * #getDecreaseFactor()
     */
    @Override
    public double getDecreaseFactor() {
        return this.decreaseFactor;
    }

}
