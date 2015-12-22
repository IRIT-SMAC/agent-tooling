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

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class MutableRangeImplTest extends Specification {

    @Shared MutableRangeImpl mutableRange
    @Shared double upper = 5
    @Shared double lower = 1

    def setup() {
        mutableRange = new MutableRangeImpl(lower,upper)
    }

    def 'MutableRangeImpl' () {

        given:
        MutableRangeImpl mutableRangeConstructor

        when:
        mutableRangeConstructor = new MutableRangeImpl(lower,upper)

        then:
        mutableRangeConstructor != null
    }

    def 'getLowerBound' () {

        when:
        double lowerBound = mutableRange.getLowerBound()

        then:
        lowerBound == lower
    }

    def 'getUpperBound' () {

        when:
        double upperBound = mutableRange.getUpperBound()

        then:
        upperBound == upper
    }

    def 'setUpperBound' () {

        given:
        double upperBound = 4

        when:
        mutableRange.setUpperBound(upperBound)

        then:
        mutableRange.upperBound == upperBound
    }

    def 'setLowerBound' () {

        given:
        double lowerBound = 3

        when:
        mutableRange.setLowerBound(lowerBound)

        then:
        mutableRange.lowerBound == lowerBound
    }

    def 'hasInfiniteSize' (boolean infinite, double upperBound, double lowerBound) {

        given:
        mutableRange.setUpperBound(upperBound)
        mutableRange.setLowerBound(lowerBound)
        boolean infiniteSize = mutableRange.hasInfiniteSize()

        expect:
        infiniteSize == infinite

        where:
        infinite | upperBound | lowerBound
        false | 7 | 5
        true | Double.POSITIVE_INFINITY | 4
        true | 4 | Double.NEGATIVE_INFINITY
        true | Double.POSITIVE_INFINITY | Double.NEGATIVE_INFINITY
    }

    def 'setUpperBound with a NaN argument should throw an IllegalArgumentException' () {

        when:
        mutableRange.setUpperBound(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setLowerBound with a NaN argument should throw an IllegalArgumentException' () {

        when:
        mutableRange.setLowerBound(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setLowerBound should throw an IllegalArgumentException when lowerBound > upperBound' () {

        when:
        mutableRange.setLowerBound(7)

        then:
        thrown(IllegalArgumentException)
    }

    def 'isInsideRange' (boolean inside, double value) {

        given:
        boolean isInside = mutableRange.isInsideRange(value)

        expect:
        isInside == inside

        where:
        inside | value
        true | 4
        false | -4
    }

    def 'toString should return the correct string' () {

        when:
        String str = mutableRange.toString()

        then:
        str == "[" + lower + " ; " + upper + "]"
    }

    def 'computeRangeSize with infinity size' () {

        given:
        mutableRange.setUpperBound(Double.POSITIVE_INFINITY)
        mutableRange.setLowerBound(Double.NEGATIVE_INFINITY)

        when:
        BigDecimal bd = mutableRange.computeRangeSize()

        then:
        bd == 3.5953862697246314E+308
    }
}
