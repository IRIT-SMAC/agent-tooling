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
import fr.irit.smac.libs.tooling.avt.AVTBuilder
import fr.irit.smac.libs.tooling.avt.EFeedback
import fr.irit.smac.libs.tooling.avt.IAVT
import fr.irit.smac.libs.tooling.avt.IAdvancedAVT
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection

@Unroll
class StandardAVTTest extends Specification {

    @Shared AVTBuilder avtBuilder
    @Shared StandardAVT standardAVTConstructor
    @Shared IAVT standardAVT
    @Shared double minValue = -5
    @Shared double maxValue = 10
    @Shared double startValue = 0.5
    @Shared double precision = 0.001

    def setupSpec() {

        avtBuilder = new AVTBuilder()
        avtBuilder.lowerBound(minValue)
        avtBuilder.upperBound(maxValue)
        avtBuilder.deltaMin(precision)
        avtBuilder.deltaDecreaseDelay(1)
        avtBuilder.deltaIncreaseDelay(1)
        avtBuilder.startValue(startValue)

        standardAVT = avtBuilder.build()
    }

    def setup() {

        avtBuilder.startValue(startValue)
        avtBuilder.lowerBound(minValue)
        avtBuilder.upperBound(maxValue)
        standardAVT.value = startValue
        avtBuilder.deltaMin(precision)
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

    def 'lower bound'() {

        when:
        standardAVT.setLowerBound(-4)

        then:
        true
    }

    def 'upper bound'() {

        when:
        standardAVT.setUpperBound(4)

        then:
        true
    }

    def 'ensureValueBoundsConsistency'(double value, double newValue) {

        given:
        standardAVT.lowerBound = -30
        standardAVT.upperBound = 30
        standardAVT.value = value
        standardAVT.lowerBound = minValue
        standardAVT.upperBound = maxValue
        standardAVT.ensureValueBoundsConsistency()

        expect:
        standardAVT.value == newValue

        where:
        value | newValue
        20 | maxValue
        -20 | minValue
    }

    def 'setValue'() {

        when:
        standardAVT.setValue(0.3)

        then:
        true
    }

    def 'setValue with a NaN argument should throw an IllegalArgumentException'() {

        when:
        standardAVT.setValue(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setValue with a value outside the range should throw an IllegalArgumentException'() {

        when:
        standardAVT.setValue(15)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getValue'() {

        when:
        double value = standardAVT.getValue()

        then:
        standardAVT.value == value
    }

    def 'getDeltaManager'() {

        when:
        IDeltaManager deltaManager = standardAVT.getDeltaManager()

        then:
        standardAVT.deltaManager == deltaManager
    }

    def 'getAdvancedAVT'() {

        when:
        IAdvancedAVT advancedAVT = standardAVT.getAdvancedAVT()

        then:
        standardAVT.advancedAVT == advancedAVT
    }

    def 'getCriticity'() {

        when:
        double criticity = standardAVT.getCriticity()

        then:
        criticity == 0.5
    }

    def 'setCriticity'() {

        when:
        standardAVT.setCriticity(0.5)

        then:
        true
    }

    def 'setCriticity with a NaN argument should throw an IllegalArgumentException'() {

        when:
        standardAVT.setCriticity(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setCriticity with a negative value should throw an IllegalArgumentException'() {

        given:
        standardAVT.value = -1

        when:
        standardAVT.setCriticity(-5)

        then:
        thrown(IllegalArgumentException)
    }

    def 'setCriticity with a value upper than 1 should throw an IllegalArgumentException'() {

        given:
        standardAVT.value = 4

        when:
        standardAVT.setCriticity(5)

        then:
        thrown(IllegalArgumentException)
    }

    def 'updateDelta with a null argument should throw an IllegalArgumentException'() {

        when:
        standardAVT.updateDelta(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'adjustValue with a good new feedback '(EFeedback feedback, EFeedback newFeedback) {

        given:
        standardAVT.updateDelta(newFeedback)

        expect:
        standardAVT.adjustValue(feedback)

        where:
        feedback | newFeedback
        EFeedback.LOWER | EFeedback.GOOD
        EFeedback.GREATER | EFeedback.GOOD
        EFeedback.GOOD | EFeedback.GOOD
    }

    def 'adjustValue with a not good new feedback '(EFeedback feedback, EFeedback newFeedback, double newValue) {

        given:
        standardAVT.adjustValue(feedback)

        expect:
        standardAVT.value == newValue

        where:
        feedback | newFeedback | newValue
        EFeedback.LOWER | EFeedback.LOWER | 0.46799999999999997
        EFeedback.GREATER | EFeedback.GREATER | 0.532
    }

    def 'adjustValue with a null argument should thrown an IllegalArgumentException'() {

        when:
        standardAVT.adjustValue(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getValueIf with a not good new feedback'(EFeedback feedback, double delta, double newValue, EDirection direction) {

        expect:
        newValue == standardAVT.getValueIf(feedback)

        where:
        feedback | delta | newValue | direction
        EFeedback.GREATER | 5.0 | 0.532 | EDirection.DIRECT
        EFeedback.GREATER | 5.0 | 0.532 | EDirection.INDIRECT
        EFeedback.GREATER | 5.0 | 0.532 | EDirection.NONE
        EFeedback.LOWER | 5.0 | 0.46799999999999997 | EDirection.INDIRECT
        EFeedback.LOWER | 5.0 | 0.46799999999999997 | EDirection.DIRECT
        EFeedback.LOWER | 5.0 | 0.46799999999999997 | EDirection.NONE
    }

    def 'getValueIf with a good new feedback'(EDirection direction, EFeedback feedback) {

        given:
        EFeedback newFeedback = EFeedback.GOOD

        expect:
        standardAVT.value == standardAVT.getValueIf(newFeedback)

        where:
        direction | feedback
        EDirection.NONE | EFeedback.GREATER
        EDirection.DIRECT | EFeedback.GREATER
        EDirection.INDIRECT | EFeedback.GREATER
        EDirection.NONE | EFeedback.LOWER
        EDirection.DIRECT | EFeedback.LOWER
        EDirection.INDIRECT | EFeedback.LOWER
    }

    def 'getValueIf with a null argument should thrown an IllegalArgumentException'() {

        when:
        standardAVT.getValueIf(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'willStayAtBounds'(EFeedback feedback, double value, boolean stayAtBounds) {

        given:
        standardAVT.value = value

        expect:
        stayAtBounds == standardAVT.willStayAtBounds(feedback)

        where:
        feedback | value | stayAtBounds
        EFeedback.LOWER | minValue | true
        EFeedback.GREATER | maxValue | true
        EFeedback.GOOD | 1.0 | false
        EFeedback.LOWER | 2.0 | false
        EFeedback.GREATER | 2.0 | false
    }

    def 'getNonInfiniteEquivalOf'(double value, double finiteVal) {

        expect:
        finiteVal == standardAVT.getNonInfiniteEquivalOf(value)

        where:
        value | finiteVal
        Double.NEGATIVE_INFINITY | -Double.MAX_VALUE
        Double.POSITIVE_INFINITY | Double.MAX_VALUE
        5.0 | 5.0
    }

    def 'getDirectionFromFeedback' (EFeedback feedback, EDirection  direction) {

        given:
        EDirection newDirection = standardAVT.getDirectionFromFreedback(feedback)

        expect:
        direction == newDirection

        where:
        feedback | direction
        EFeedback.GREATER | EDirection.DIRECT
        EFeedback.LOWER | EDirection.INDIRECT
        EFeedback.GOOD | EDirection.NONE
        null | EDirection.NONE
    }

    def 'toString display' () {

        when:
        String string = standardAVT.toString()

        then:
        string == "AVT [v=0.5 delta=0.032]"
    }
}
