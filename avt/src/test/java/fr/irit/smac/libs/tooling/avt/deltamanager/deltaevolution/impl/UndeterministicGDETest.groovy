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

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class UndeterministicGDETest extends Specification{

    @Shared UndeterministicGDE undeterministicGDEConstructor
    @Shared UndeterministicGDE undeterministicGDE

    def setupSpec() {
        undeterministicGDE = new UndeterministicGDE(1, 5, 4, 1)
    }

    def 'UndeterministicGDE (3 arguments) with correct arguments' () {

        when:
        undeterministicGDEConstructor = new UndeterministicGDE(2,7,5)

        then:
        undeterministicGDEConstructor != null
    }

    def 'UndeterministicGDE (4 arguments) with a negative decreaseNoise throw an IllegalArgumentException' () {

        when:
        undeterministicGDEConstructor = new UndeterministicGDE(2,1,-1,1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'UndeterministicGDE (4 arguments) with a NaN decreaseNoise throw an IllegalArgumentException' () {

        when:
        undeterministicGDEConstructor = new UndeterministicGDE(2,1,Math.sqrt(-1),1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'UndeterministicGDE (4 arguments) with a (decreaseFactor - decreaseNoise) lower than 1 throw an IllegalArgumentException' () {

        when:
        undeterministicGDEConstructor = new UndeterministicGDE(1,1,1,1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'UndeterministicGDE (4 arguments) with correct arguments' () {

        given:
        double decreaseNoise = 4

        when:
        undeterministicGDEConstructor = new UndeterministicGDE(1,5,decreaseNoise,1)

        then:
        undeterministicGDEConstructor.decreaseNoise == decreaseNoise
        undeterministicGDEConstructor.random != null
    }

    def 'getDecreasedDelta with a NaN argument throw an IllegalArgumentException' () {

        when:
        double delta = undeterministicGDE.getDecreasedDelta(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'getDecreasedDelta should return delta' () {

        when:
        double delta = undeterministicGDE.getDecreasedDelta(4)

        then:
        delta != null
    }
}
