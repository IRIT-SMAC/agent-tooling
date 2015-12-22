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
package fr.irit.smac.libs.tooling.avt.impl;

import static org.junit.Assert.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory
import fr.irit.smac.libs.tooling.avt.range.IMutableRange

@Unroll
class StandardAVTTest extends Specification {

    @Shared StandardAVT standardAVTConstructor
    @Shared StandardAVT standardAVT
    @Shared double startValue

    def setup() {
        startValue = 5
        standardAVT = new StandardAVT(2,6,startValue,Mock(IDeltaManagerFactory))
    }

    def 'StandardAVT with a NaN lowerBound should throw an IllegalArgumentException' () {

        when:
        standardAVTConstructor = new StandardAVT(Math.sqrt(-1),2,4,Mock(IDeltaManagerFactory))

        then:
        thrown(IllegalArgumentException)
    }

    def 'StandardAVT with a NaN upperBound should throw an IllegalArgumentException' () {

        when:
        standardAVTConstructor = new StandardAVT(1,Math.sqrt(-1),4,Mock(IDeltaManagerFactory))

        then:
        thrown(IllegalArgumentException)
    }

    def 'StandardAVT with a NaN startValue should throw an IllegalArgumentException' () {

        when:
        standardAVTConstructor = new StandardAVT(1,1,Math.sqrt(-1),Mock(IDeltaManagerFactory))

        then:
        thrown(IllegalArgumentException)
    }

    def 'StandardAVT with no deltaManagerFactory should throw an IllegalArgumentException' () {

        when:
        standardAVTConstructor = new StandardAVT(1,1,1,null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getValue' () {

        when:
        double value = standardAVT.getValue()

        then:
        startValue == value
    }

    def 'getRange' () {

        when:
        IMutableRange range = standardAVT.getRange()

        then:
        range != null
    }

//    def 'setLowerBound' () {
//
//        given:
//        double lower = 5
//
//        when:
//        avt.setLowerBound(lower)
//
//        then:
//        avt.range.getLowerBound() == lower
//    }
    
//    def 'ensureValueBoundsConsistency' (double value, double correctNewValue) {
//        
//        given:
//        avt.value = value
//        avt.effectiveUpperBound = 10
//        avt.ensureValueBoundsConsistency()
//        
//        expect:
//        avt.value == correctNewValue
//        
//        where:
//        value << [4,40]
//        correctNewValue << [4,10]
//    }
}
