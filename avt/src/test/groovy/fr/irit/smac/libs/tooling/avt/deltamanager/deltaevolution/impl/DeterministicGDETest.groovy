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
package fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class DeterministicGDETest extends Specification{

    @Shared DeterministicGDE deterministicGDE
    @Shared DeterministicGDE deterministicGDE2
    
    def setupSpec() {
        deterministicGDE = new DeterministicGDEFactory(2, 3).createInstance()
    }

    def 'DeterministicGDE with an increaseFactor lower than 1 should throw an IllegalArgumentException' () {

        when:
        deterministicGDE2 = new DeterministicGDEFactory(0, 3).createInstance()

        then:
        thrown(IllegalArgumentException)
    }

    def 'DeterministicGDE with a decreaseFactor lower than 1 should throw an IllegalArgumentException' () {

        when:
        deterministicGDE2 = new DeterministicGDEFactory(1, 0).createInstance()

        then:
        thrown(IllegalArgumentException)
    }

    def 'DeterministicGDE with a NaN decreaseFactor should throw an IllegalArgumentException' () {

        when:
        deterministicGDE2 = new DeterministicGDEFactory(1, Double.NaN).createInstance()

        then:
        thrown(IllegalArgumentException)
    }

    def 'DeterministicGDE with a NaN increaseFactor should throw an IllegalArgumentException' () {

        when:
        deterministicGDE2 = new DeterministicGDEFactory(Double.NaN,1).createInstance()

        then:
        thrown(IllegalArgumentException)
    }

    def 'getDecreaseFactor should return decreaseFactor' () {

        given:
        deterministicGDE2 = new DeterministicGDEFactory(2,3).createInstance()

        when:
        double decreaseFactor = deterministicGDE2.getDecreaseFactor()

        then:
        decreaseFactor == deterministicGDE2.decreaseFactor
    }

    def 'getDecreasedDelta with a NaN argument should throw an IllegalArgumentException' () {

        when:
        deterministicGDE.getDecreasedDelta(Double.NaN)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getIncreasedDelta with a NaN argument should throw an IllegalArgumentException' () {

        when:
        deterministicGDE.getIncreasedDelta(Double.NaN)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getIncreasedDelta should return delta * decreaseFactor' () {

        given:
        double delta = 2.0

        when:
        double newDelta = deterministicGDE.getIncreasedDelta(delta)

        then:
        newDelta == delta * deterministicGDE.increaseFactor
    }

    def 'getDecreasedDelta should return delta / decreaseFactor' () {

        given:
        double delta = 2.0

        when:
        double newDelta = deterministicGDE.getDecreasedDelta(delta)

        then:
        newDelta == delta / deterministicGDE.decreaseFactor
    }
}
