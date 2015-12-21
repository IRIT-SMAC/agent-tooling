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

import java.math.BigDecimal;

public interface IRange {

    /**
     * The lower bound of this range.
     * 
     * @return the lower bound
     */
    public double getLowerBound();

    /**
     * The upper bound of this range.
     * 
     * @return the upper bound
     */
    public double getUpperBound();

    /**
     * 
     * @param value
     * @return true if value is inside lower et upper bounds, including bounds
     */
    public boolean isInsideRange(double value);

    /**
     * Computes the size of the range
     */
    public BigDecimal computeRangeSize();

    /**
     * @return isInfinite
     */
    public boolean hasInfiniteSize();
}
