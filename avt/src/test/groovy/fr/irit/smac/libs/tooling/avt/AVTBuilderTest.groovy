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
package fr.irit.smac.libs.tooling.avt

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory

@Unroll
class AVTBuilderTest extends Specification {

    @Shared AVTBuilder avtBuilder

    def setupSpec() {
        avtBuilder = new AVTBuilder()
    }

    def setup() {

        avtBuilder.lowerBound = 5.0
        avtBuilder.upperBound = 10.0
    }

    def 'avtBuilder with a lowerBound greater than the upperBound should thrown an IllegalStateException' () {

        given:
        avtBuilder.lowerBound = 15.0
        avtBuilder.upperBound = 10.0

        when:
        avtBuilder.checkLowerBound()

        then:
        thrown(IllegalStateException)
    }

    def 'avtBuilder with a startValue greater than the upperBound should thrown an IllegalStateException' () {

        given:
        avtBuilder.startValue = 25.0

        when:
        avtBuilder.checkStartValue()

        then:
        thrown(IllegalStateException)
    }

    def 'avtBuilder with a startValue lower than the lowerBound should thrown an IllegalStateException' () {

        given:
        avtBuilder.startValue = -25.0

        when:
        avtBuilder.checkStartValue()

        then:
        thrown(IllegalStateException)
    }

    def 'avtBuilder with a negative deltaMin should thrown an IllegalStateException' () {

        given:
        avtBuilder.deltaMin = -5.0

        when:
        avtBuilder.checkDeltaMin()

        then:
        thrown(IllegalStateException)
    }

    def 'avtBuilder with a deltaMin greater than the deltaMax should thrown an IllegalStateException' () {

        given:
        avtBuilder.deltaMin = 5.0
        avtBuilder.deltaMax = -15.0

        when:
        avtBuilder.checkDeltaMin()

        then:
        thrown(IllegalStateException)
    }

    def 'avtBuilder with a deltaMin lower than the deltaMax' () {

        given:
        avtBuilder.deltaMax = 15.0
        avtBuilder.deltaMin = 5.0

        when:
        avtBuilder.checkDeltaMin()

        then:
        true
    }

    def 'avtBuilder with a null avtFactory should thrown an IllegalStateException' () {

        given:
        avtBuilder.avtFactory = null

        when:
        avtBuilder.checkAvtFactory()

        then:
        thrown(IllegalStateException)
    }

    def 'isHardBounds with a not null avtFactory should throw an IllegalStateException' () {

        given:
        avtBuilder.avtFactory = Mock(IAVTFactory)

        when:
        avtBuilder.isHardBounds(true)

        then:
        thrown(IllegalStateException)
    }

    def 'avtFactory'() {

        given:
        IAVTFactory avtFactory = Mock(IAVTFactory)
        when:
        avtBuilder.avtFactory(avtFactory)

        then:
        avtBuilder.avtFactory == avtFactory
    }

    def 'isDelayedDelta'() {

        given:
        boolean isDelayed = true

        when:
        avtBuilder.isDelayedDelta(isDelayed)

        then:
        avtBuilder.isDelayedDelta == isDelayed
    }

    def 'isBoundedDelta'() {

        given:
        boolean isDeterministic = true

        when:
        avtBuilder.isBoundedDelta(isDeterministic)

        then:
        avtBuilder.isBoundedDelta == isDeterministic
    }

    def 'isDeterministicDelta'() {

        given:
        boolean isDeterministic = true

        when:
        AVTBuilder avtBuilder2 = avtBuilder.isDeterministicDelta(isDeterministic)

        then:
        avtBuilder.isDeterministicDelta  == isDeterministic
        avtBuilder2 == avtBuilder
    }

    def 'lowerBound with a NaN argument should throw an IllegalArgumentException' () {

        when:
        avtBuilder.lowerBound(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'upperBound with a NaN argument should throw an IllegalArgumentException' () {

        when:
        avtBuilder.upperBound(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'startValue with a NaN argument should throw an IllegalArgumentException' () {

        when:
        avtBuilder.startValue(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaMax' () {

        given:
        double deltaMax = 5.0

        when:
        avtBuilder.deltaMax(deltaMax)

        then:
        avtBuilder.deltaMax == deltaMax
    }

    def 'deltaMax with a NaN argument should throw an IllegalArgumentException' () {

        when:
        avtBuilder.deltaMax(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaMin with a NaN argument should throw an IllegalArgumentException' () {

        when:
        avtBuilder.deltaMin(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaIncreaseDelay with a negative argument should throw an IllegalArgumentException' () {

        when:
        avtBuilder.deltaIncreaseDelay(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaDecreaseDelay with a negative argument should throw an IllegalArgumentException' () {

        when:
        avtBuilder.deltaDecreaseDelay(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'softBoundsMemory with a negative argument should throw an IllegalArgumentException' () {

        when:
        avtBuilder.softBoundsMemory(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'softBoundsMemory with hardBounds should throw an IllegalStateException' () {

        given:
        avtBuilder.isHardBounds = true

        when:
        avtBuilder.softBoundsMemory(1)

        then:
        thrown(IllegalStateException)
    }

    def 'deltaIncreaseFactor'() {

        given:
        double increaseFactor = 2.5

        when:
        avtBuilder.deltaIncreaseFactor (increaseFactor)

        then:
        avtBuilder.deltaIncreaseFactor  == increaseFactor
    }

    def 'deltaIncreaseFactor with an argument lower than 1 should throw an IllegalArgumentException' () {

        when:
        avtBuilder.deltaIncreaseFactor(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaDecreaseFactor'() {

        given:
        double decreaseFactor = 2.5

        when:
        avtBuilder.deltaDecreaseFactor(decreaseFactor)

        then:
        avtBuilder.deltaDecreaseFactor  == decreaseFactor
    }

    def 'deltaDecreaseFactor with an argument lower than 1 should throw an IllegalArgumentException' () {

        when:
        avtBuilder.deltaDecreaseFactor(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaDecreaseNoise'() {

        given:
        double decreaseNoise = 2.5
        avtBuilder.isDeterministicDelta = false

        when:
        avtBuilder.deltaDecreaseNoise(decreaseNoise)

        then:
        avtBuilder.deltaDecreaseNoise  == decreaseNoise
    }

    def 'deltaDecreaseNoise with a negative argument should throw an IllegalArgumentException' () {

        given:
        avtBuilder.isDeterministicDelta = false

        when:
        avtBuilder.deltaDecreaseNoise(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'deltaDecreaseNoise with DeterministicDelta should throw an IllegalStateException' () {

        given:
        avtBuilder.isDeterministicDelta = true

        when:
        avtBuilder.deltaDecreaseNoise(1)

        then:
        thrown(IllegalStateException)
    }

    def 'deltaRandomSeed'() {

        given:
        long seed = 4.0

        when:
        avtBuilder.deltaRandomSeed(seed)

        then:
        avtBuilder.isDeterministicDelta == false
        avtBuilder.deltaRandomSeed == seed
    }

    def 'deltaManagerFactory' () {

        given:
        IDeltaManagerFactory deltaManagerFactory = Mock(IDeltaManagerFactory)

        when:
        AVTBuilder avtBuilder2 = avtBuilder.deltaManagerFactory(deltaManagerFactory)

        then:
        avtBuilder.deltaManagerFactory == deltaManagerFactory
        avtBuilder2 == avtBuilder
    }

    def 'deltaManagerFactory with a null argument should throw an IllegalArgumentException' () {

        when:
        avtBuilder.deltaManagerFactory(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getPortionOfRange'() {

        given:
        double portion = 4.0

        when:
        double portionOfRange = avtBuilder.getPortionOfRange(portion)

        then:
        portionOfRange == 20.0
    }

    def 'getRange' () {

        when:
        BigDecimal range = avtBuilder.getRange()

        then:
        range == 5.0
    }

    def 'getRange with lowerBound greather than upperBound should throw an IllegalStateException' () {

        given:
        avtBuilder.upperBound = 2
        avtBuilder.lowerBound = 3

        when:
        avtBuilder.getRange()

        then:
        thrown(IllegalStateException)
    }

    def 'getBigDecValueOf'(double value, BigDecimal bdVal) {

        expect:
        bdVal == avtBuilder.getBigDecValueOf(value)

        where:
        value | bdVal
        Double.NEGATIVE_INFINITY | BigDecimal.valueOf(-Double.MAX_VALUE)
        Double.POSITIVE_INFINITY | BigDecimal.valueOf(Double.MAX_VALUE)
        5.0 | BigDecimal.valueOf(5.0)
    }

    def 'getRangeMiddle'() {

        when:
        double rangeMiddle = avtBuilder.getRangeMiddle()

        then:
        rangeMiddle == 7.5
    }

    def 'getRangeMiddle when lowerBound is upper than upperBound should throw an IllegalStateException'() {

        given:
        avtBuilder.lowerBound = 15.0

        when:
        avtBuilder.getRangeMiddle()

        then:
        thrown(IllegalStateException)
    }

    def 'build'() {

        given:
        avtBuilder.startValue = null
        avtBuilder.deltaManagerFactory = null
        avtBuilder.isDeterministicDelta = false
        avtBuilder.deltaMin = null
        avtBuilder.deltaMax = 5.0

        when:
        avtBuilder.build()

        then:
        true
    }

    def 'build with a not null deltaManagerFactory'() {

        given:
        avtBuilder.startValue = null
        avtBuilder.deltaManagerFactory = Mock(IDeltaManagerFactory)
        avtBuilder.isDeterministicDelta = false
        avtBuilder.deltaMin = null
        avtBuilder.deltaMax = 5.0

        when:
        avtBuilder.build()

        then:
        true
    }

    def 'build with a false isDelayedDelta and a false isBoundedDelta'() {

        given:
        avtBuilder.startValue = null
        avtBuilder.deltaManagerFactory = null
        avtBuilder.isDeterministicDelta = false
        avtBuilder.deltaMin = null
        avtBuilder.deltaMax = 5.0
        avtBuilder.isDelayedDelta = false
        avtBuilder.isBoundedDelta = false
        
        when:
        avtBuilder.build()

        then:
        true
    }
}
