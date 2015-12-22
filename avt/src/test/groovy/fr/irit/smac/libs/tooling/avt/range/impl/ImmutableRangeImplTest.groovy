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
class ImmutableRangeImplTest extends Specification {

    @Shared ImmutableRangeImpl immutableRange

    def setupSpec() {
        immutableRange = new ImmutableRangeImpl(1, 1)
    }

    def 'ImmutableRangeImpl' () {

        given:
        ImmutableRangeImpl immutableRangeConstructor

        when:
        immutableRangeConstructor = new ImmutableRangeImpl(1,1)

        then:
        immutableRangeConstructor != null
    }

    def 'getLowerBound' () {

        when:
        double lowerBound = immutableRange.getLowerBound()

        then:
        lowerBound == 1
    }

    def 'getUpperBound' () {

        when:
        double upperBound = immutableRange.getUpperBound()

        then:
        upperBound == 1
    }
}
