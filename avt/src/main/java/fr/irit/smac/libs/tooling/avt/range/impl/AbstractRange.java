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
package fr.irit.smac.libs.tooling.avt.range.impl;

import java.math.BigDecimal;

import fr.irit.smac.libs.tooling.avt.range.IRange;

public abstract class AbstractRange implements IRange {

    @Override
    public BigDecimal computeRangeSize() {
        if (this.hasInfiniteSize()) {
            return BigDecimal.valueOf(Double.MAX_VALUE).subtract(BigDecimal.valueOf(-Double.MAX_VALUE));
        }
        else {
            return BigDecimal.valueOf(this.getUpperBound()).subtract(BigDecimal.valueOf(this.getLowerBound()));
        }
    }

    @Override
    public boolean hasInfiniteSize() {
        return this.getUpperBound() == Double.POSITIVE_INFINITY || this.getLowerBound() == Double.NEGATIVE_INFINITY;
    }

    protected void checkBoundsConsistency() {
        if (Double.isNaN(this.getLowerBound())) {
            throw new IllegalArgumentException("lowerBound isNaN");
        }
        if (Double.isNaN(this.getUpperBound())) {
            throw new IllegalArgumentException("upperBound isNaN");
        }

        if (this.getLowerBound() > this.getUpperBound()) {
            throw new IllegalArgumentException("lowerBound > upperBound");
        }
    }

    @Override
    public boolean isInsideRange(double value) {
        return value >= this.getLowerBound() && value <= this.getUpperBound();
    }

    @Override
    public String toString() {
        return "[" + this.getLowerBound() + " ; " + this.getUpperBound() + "]";
    }

}
