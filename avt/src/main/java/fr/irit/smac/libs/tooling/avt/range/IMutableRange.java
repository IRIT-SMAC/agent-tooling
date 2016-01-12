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
package fr.irit.smac.libs.tooling.avt.range;

/**
 * The Interface IMutableRange.
 */
public interface IMutableRange extends IRange {
    /**
     * Sets the lower bound of this range.
     *
     * @param lowerBound
     *            lower bound to be set
     * @throws IllegalArgumentException
     *             if the lowerBound to set is greater than the current
     *             upperBound (
     *             <code>if (lowerBound > this.getUpperBound())</code>)
     */
    public void setLowerBound(double lowerBound);

    /**
     * Sets the upper bound of this range.
     *
     * @param upperBound
     *            upper bound to be set
     * @throws IllegalArgumentException
     *             if the upperBound to set is lower than the current lowerBound
     *             ( <code>if (upperBound < this.getLowerBound())</code>)
     */
    public void setUpperBound(double upperBound);

}
