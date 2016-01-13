package fr.irit.smac.libs.tooling.avt.impl

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.AVTBuilder
import fr.irit.smac.libs.tooling.avt.EFeedback
import fr.irit.smac.libs.tooling.avt.IAVT

@Unroll
class SoftBoundsAVTTest extends Specification {

    @Shared AVTBuilder avtBuilder
    @Shared IAVT softBoundsAVT
    @Shared double minValue = -5
    @Shared double maxValue = 10
    @Shared double startValue = 0.5
    @Shared double precision = 0.001
    @Shared boolean isHardBounds = false

    def setupSpec() {

        avtBuilder = new AVTBuilder()
        avtBuilder.lowerBound(minValue)
        avtBuilder.upperBound(maxValue)
        avtBuilder.deltaMin(precision)
        avtBuilder.deltaDecreaseDelay(1)
        avtBuilder.deltaIncreaseDelay(1)
        avtBuilder.startValue(startValue)
        avtBuilder.isHardBounds(isHardBounds)

        softBoundsAVT = avtBuilder.build()
    }

    def setup() {

        avtBuilder.startValue(startValue)
        softBoundsAVT.lowerBound = minValue
        softBoundsAVT.upperBound = maxValue
        softBoundsAVT.value = startValue
        avtBuilder.deltaMin(precision)
    }

    def 'setLowerBound'() {

        when:
        softBoundsAVT.setLowerBound(-30)

        then:
        true
    }

    def 'setUpperBound'() {

        when:
        softBoundsAVT.setUpperBound(11)

        then:
        true
    }
    
    def 'setValue'(double value) {
        
        given:
        softBoundsAVT.setValue(value)

        expect:
        softBoundsAVT.value == value
        
        where:
        value << [1.5,-20.0]
    }

    def 'setValue with a null value should throw an IllegalArgumentException'() {

        when:
        softBoundsAVT.setValue(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'adjustValue'(EFeedback feedback, double newValue, double delta) {

        given:
        softBoundsAVT.deltaManager.delta = delta
        softBoundsAVT.adjustValue(feedback)

        expect:
        newValue == softBoundsAVT.value

        where:
        feedback | newValue | delta
        EFeedback.GOOD | startValue | softBoundsAVT.deltaManager.delta
        EFeedback.GREATER | 0.532 | softBoundsAVT.deltaManager.delta
        EFeedback.LOWER | 0.46799999999999997 | softBoundsAVT.deltaManager.delta
        EFeedback.LOWER | -99.5 | 100.0
    }

    def 'adjustValue with a null argument should throw an IllegalArgumentException'() {

        when:
        softBoundsAVT.adjustValue(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'updateBoundsFromValue'(double value, double lowerBound, double upperBound) {

        given:
        softBoundsAVT.updateBoundsFromValue(value)

        expect:
        true

        where:
        value | lowerBound | upperBound
        -20 | -20 | maxValue
        20 | minValue | 20
    }

    def 'getValueIf'(EFeedback feedback, double valueIf) {

        expect:
        valueIf == softBoundsAVT.getValueIf(feedback)

        where:
        feedback | valueIf
        EFeedback.GREATER | 0.532
        EFeedback.LOWER | 0.46799999999999997
        EFeedback.GOOD | softBoundsAVT.value
    }

    def 'getValueIf with a NaN argument should throw an IllegalArgumentException'() {

        when:
        softBoundsAVT.getValueIf(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'updateBoundsFromValue with a NaN argument should throw an IllegalArgumentException'() {

        when:
        softBoundsAVT.updateBoundsFromValue(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setLowerBoundFromValue'() {

        given:
        double lower = -15.0

        when:
        softBoundsAVT.setLowerBoundFromValue(lower)

        then:
        true
    }

    def 'setLowerBoundFromValue with a NaN argument should throw an IllegalArgumentException'() {

        when:
        softBoundsAVT.setLowerBoundFromValue(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setUpperBoundFromValue'() {

        given:
        double upper = 15.0

        when:
        softBoundsAVT.setUpperBoundFromValue(upper)

        then:
        true
    }

    def 'setUpperBoundFromValue with a NaN argument should throw an IllegalArgumentException'() {

        when:
        softBoundsAVT.setUpperBoundFromValue(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setValueFromBounds with a NaN argument should throw an IllegalArgumentException'() {

        when:
        softBoundsAVT.setValueFromBounds(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'updateValueFromBounds'(double value, double newValue) {

        given:
        softBoundsAVT.value = value
        softBoundsAVT.effectiveLowerBound = -5.0
        softBoundsAVT.effectiveUpperBound = 5.0
        softBoundsAVT.updateValueFromBounds()

        expect:
        softBoundsAVT.value == newValue

        where:
        value | newValue
        20.0 | 5.0
        -20.0 | -5.0
    }
}
