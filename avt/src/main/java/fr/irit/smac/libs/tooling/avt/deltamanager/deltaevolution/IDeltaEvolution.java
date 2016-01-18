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
package fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution;

/**
 * The Interface IDeltaEvolution.
 * 
 * <p>
 * A DeltaEvolution defines how to increase or to decrease the delta.
 * </p>
 */
public interface IDeltaEvolution {

    /**
     * Gets the increased delta.
     *
     * @param delta
     *            the delta
     * @return the increased delta
     */
    public double getIncreasedDelta(double delta);

    /**
     * Gets the decreased delta.
     *
     * @param delta
     *            the delta
     * @return the decreased delta
     */
    public double getDecreasedDelta(double delta);
}
