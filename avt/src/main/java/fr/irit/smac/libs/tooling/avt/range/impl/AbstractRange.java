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

import fr.irit.smac.libs.tooling.avt.EMessageException;
import fr.irit.smac.libs.tooling.avt.range.IRange;

/**
 * The Class AbstractRange.
 */
public abstract class AbstractRange implements IRange {

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.range.IRange#computeRangeSize()
     */
    @Override
    public BigDecimal computeRangeSize() {
        if (this.hasInfiniteSize()) {
            return BigDecimal.valueOf(Double.MAX_VALUE).subtract(BigDecimal.valueOf(-Double.MAX_VALUE));
        }
        else {
            return BigDecimal.valueOf(this.getUpperBound()).subtract(BigDecimal.valueOf(this.getLowerBound()));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.range.IRange#hasInfiniteSize()
     */
    @Override
    public boolean hasInfiniteSize() {
        return this.getUpperBound() == Double.POSITIVE_INFINITY || this.getLowerBound() == Double.NEGATIVE_INFINITY;
    }

    /**
     * Check bounds consistency.
     */
    protected void checkBoundsConsistency() {
        if (Double.isNaN(this.getLowerBound())) {
            throw new IllegalArgumentException(EMessageException.LOWER_BOUND_NAN.toString());
        }
        if (Double.isNaN(this.getUpperBound())) {
            throw new IllegalArgumentException(EMessageException.UPPER_BOUND_NAN.toString());
        }

        if (this.getLowerBound() > this.getUpperBound()) {
            throw new IllegalArgumentException(EMessageException.LOWER_BOUND_GT_UPPER_BOUND.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.range.IRange#isInsideRange(double)
     */
    @Override
    public boolean isInsideRange(double value) {
        return value >= this.getLowerBound() && value <= this.getUpperBound();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + this.getLowerBound() + " ; " + this.getUpperBound() + "]";
    }

}
