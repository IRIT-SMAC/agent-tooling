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

import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDE;

public class DeterministicGDE implements IGeometricDE {

    protected final double decreaseFactor;
    protected final double increaseFactor;

    /**
     * 
     * @param decreaseFactor
     *            the decrease factor used for delta decrease
     * @param increaseFactor
     *            the increase factor used for delta decrease
     * @throws IllegalArgumentException
     *             if decreaseFactor < 1
     * @throws IllegalArgumentException
     *             if increaseFactor < 1
     */
    public DeterministicGDE(double increaseFactor, double decreaseFactor) {

        if (Double.isNaN(increaseFactor)) {
            throw new IllegalArgumentException("increaseFactor isNaN");
        }

        if (Double.isNaN(decreaseFactor)) {
            throw new IllegalArgumentException("decreaseFactor isNaN");
        }

        if (decreaseFactor < 1) {
            throw new IllegalArgumentException("decrease factor < 1");
        }

        if (increaseFactor < 1) {
            throw new IllegalArgumentException("increase factor < 1");
        }

        this.increaseFactor = increaseFactor;
        this.decreaseFactor = decreaseFactor;
    }

    @Override
    public double getIncreasedDelta(double delta) {
        if (Double.isNaN(delta)) {
            throw new IllegalArgumentException("increaseFactor delta");
        }

        return delta * this.increaseFactor;
    }

    @Override
    public double getDecreasedDelta(double delta) {
        if (Double.isNaN(delta)) {
            throw new IllegalArgumentException("increaseFactor delta");
        }

        return delta / this.decreaseFactor;
    }

    @Override
    public double getIncreaseFactor() {
        return this.increaseFactor;
    }

    @Override
    public double getDecreaseFactor() {
        return this.decreaseFactor;
    }

}
