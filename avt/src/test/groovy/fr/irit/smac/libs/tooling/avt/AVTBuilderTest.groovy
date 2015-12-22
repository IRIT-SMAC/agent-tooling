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
package fr.irit.smac.libs.tooling.avt;

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class AVTBuilderTest extends Specification {

    @Shared AVTBuilder avtBuilderException
    
    def setupSpec() {
        avtBuilderException = new AVTBuilder()
    }

    def 'BuildHardBounds' () {

        given:
        double minValue = 0
        double maxValue = 10
        double precision = 0.001

        AVTBuilder avtBuilderBuild = new AVTBuilder()
        avtBuilderBuild.lowerBound(minValue)
        avtBuilderBuild.upperBound(maxValue)
        avtBuilderBuild.deltaMin(precision)
        avtBuilderBuild.deltaDecreaseDelay(1)
        avtBuilderBuild.deltaIncreaseDelay(1)
        avtBuilderBuild.startValue(1.0)

        when:
        IAVT avt = avtBuilderBuild.build()

        then:
        avt != null
    }

    def 'BuildSoftBounds' () {

        given:
        boolean isHardBounds = false
        double minValue = 0
        double maxValue = 10
        double precision = 0.001
        
        AVTBuilder avtBuilderBuild = new AVTBuilder()
        avtBuilderBuild.isHardBounds(isHardBounds)
        avtBuilderBuild.lowerBound(minValue)
        avtBuilderBuild.upperBound(maxValue)
        avtBuilderBuild.deltaMin(precision)
        avtBuilderBuild.deltaDecreaseDelay(1)
        avtBuilderBuild.deltaIncreaseDelay(1)
        avtBuilderBuild.startValue(1.0)

        when:
        IAVT avt = avtBuilderBuild.build()

        then:
        avt != null
    }

    def 'BuildSoftBoundsMemory' () {

        given:
        boolean isHardBounds = false
        int softBoundsMemory = 1
        double minValue = 0
        double maxValue = 10
        double precision = 0.001
        
        AVTBuilder avtBuilderBuild = new AVTBuilder()
        avtBuilderBuild.isHardBounds(isHardBounds)
        avtBuilderBuild.softBoundsMemory(softBoundsMemory)
        avtBuilderBuild.lowerBound(minValue)
        avtBuilderBuild.upperBound(maxValue)
        avtBuilderBuild.deltaMin(precision)
        avtBuilderBuild.deltaDecreaseDelay(1)
        avtBuilderBuild.deltaIncreaseDelay(1)
        avtBuilderBuild.startValue(1.0)

        when:
        IAVT avt = avtBuilderBuild.build()

        then:
        avt != null
    }

    def 'isHardBounds with a not null avtFactory should throw an IllegalStateException' () {

        given:
        avtBuilderException.avtFactory = Mock(IAVTFactory)

        when:
        avtBuilderException.isHardBounds(true)

        then:
        thrown(IllegalStateException)
    }

    def 'lowerBound with a NaN argument should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.lowerBound(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'upperBound with a NaN argument should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.upperBound(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'startValue with a NaN argument should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.startValue(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaMax with a NaN argument should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.deltaMax(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaMin with a NaN argument should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.deltaMin(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaIncreaseDelay with a negative argument should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.deltaIncreaseDelay(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaDecreaseDelay with a negative argument should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.deltaDecreaseDelay(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'softBoundsMemory with a negative argument should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.softBoundsMemory(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'softBoundsMemory with hardBounds should throw an IllegalStateException' () {

        given:
        avtBuilderException.isHardBounds = true

        when:
        avtBuilderException.softBoundsMemory(1)

        then:
        thrown(IllegalStateException)
    }

    def 'deltaIncreaseFactor with an argument lower than 1 should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.deltaIncreaseFactor(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaDecreaseFactor with an argument lower than 1 should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.deltaDecreaseFactor(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaDecreaseNoise with a negative argument should throw an IllegalArgumentException' () {

        given:
        avtBuilderException.isDeterministicDelta = false

        when:
        avtBuilderException.deltaDecreaseNoise(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaDecreaseNoise with DeterministicDelta should throw an IllegalStateException' () {

        given:
        avtBuilderException.isDeterministicDelta = true

        when:
        avtBuilderException.deltaDecreaseNoise(1)

        then:
        thrown(IllegalStateException)
    }

    def 'deltaManagerFactory with a null argument should throw an IllegalArgumentException' () {

        when:
        avtBuilderException.deltaManagerFactory(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getRange with lowerBound greather than upperBound should throw an IllegalStateException' () {

        given:
        avtBuilderException.upperBound = 2
        avtBuilderException.lowerBound = 3

        when:
        avtBuilderException.getRange()

        then:
        thrown(IllegalStateException)
    }
}
