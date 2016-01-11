package fr.irit.smac.libs.tooling.avt.impl

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.AVTBuilder
import fr.irit.smac.libs.tooling.avt.EFeedback
import fr.irit.smac.libs.tooling.avt.IAVT
import fr.irit.smac.libs.tooling.avt.range.impl.MutableRangeImpl

@Unroll
class SoftBoundsMemoryAVTTest extends Specification{

    @Shared AVTBuilder avtBuilder
    @Shared IAVT softBoundsMemoryAVT
    @Shared double minValue = -25
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
        avtBuilder.softBoundsMemory(1)

        softBoundsMemoryAVT = avtBuilder.build()
    }

    def setup() {

        avtBuilder.startValue(startValue)
        softBoundsMemoryAVT.lowerBound = minValue
        softBoundsMemoryAVT.upperBound = maxValue
        softBoundsMemoryAVT.value = startValue
        avtBuilder.deltaMin(precision)
    }

    def 'setLowerBound'() {

        when:
        softBoundsMemoryAVT.setLowerBound(-30)

        then:
        true
    }

    def 'setUpperBound'() {

        when:
        softBoundsMemoryAVT.setUpperBound(11)

        then:
        true
    }

    def 'setValue with a Nan argument should thrown an IllegalArgumentException'() {

        when:
        softBoundsMemoryAVT.setValue(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'adjustValue'(EFeedback feedback, double value) {

        given:
        softBoundsMemoryAVT.range = new MutableRangeImpl(minValue, maxValue)
        softBoundsMemoryAVT.adjustValue(feedback)

        expect:
        true

        where:
        feedback | value
        EFeedback.GOOD | 1.0
        EFeedback.GREATER | 1.0
        EFeedback.LOWER | 1.0
    }

    def 'adjustValue with a null argument should thrown an IllegalArgumentException'() {

        when:
        softBoundsMemoryAVT.adjustValue(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getValueIf'(EFeedback feedback, double valueIf) {

        expect:
        valueIf == softBoundsMemoryAVT.getValueIf(feedback)

        where:
        feedback | valueIf
        EFeedback.GREATER | 0.5640000000000001
        EFeedback.LOWER | 0.436
        EFeedback.GOOD | softBoundsMemoryAVT.value
    }

    def 'getValueIf with a NaN argument should thrown an IllegalArgumentException'() {

        when:
        softBoundsMemoryAVT.getValueIf(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'updateBoundsFromHistory'() {

        given:
        softBoundsMemoryAVT.range = new MutableRangeImpl(minValue, maxValue)
        softBoundsMemoryAVT.valuesHistory.add((Double)0.288)

        when:
        softBoundsMemoryAVT.updateBoundsFromHistory()

        then:
        true
    }

    def 'isHistoryMax'(Double value, boolean historyMax) {

        given:
        softBoundsMemoryAVT.valuesHistory.add((Double)5.0)
        softBoundsMemoryAVT.valuesHistory.add((Double)6.0)
        softBoundsMemoryAVT.valuesHistory.add((Double)7.0)
        softBoundsMemoryAVT.valuesHistory.add((Double)2.0)

        expect:
        softBoundsMemoryAVT.isHistoryMax(value) == historyMax

        where:
        value | historyMax
        5.0 | false
        15.0 | true
        -5.0 | false
    }

    def 'isHistoryMin'(Double value, boolean historyMin) {

        given:
        softBoundsMemoryAVT.valuesHistory.add((Double)5.0)
        softBoundsMemoryAVT.valuesHistory.add((Double)6.0)
        softBoundsMemoryAVT.valuesHistory.add((Double)7.0)
        softBoundsMemoryAVT.valuesHistory.add((Double)0.2)

        expect:
        softBoundsMemoryAVT.isHistoryMin(value) == historyMin

        where:
        value | historyMin
        5.0 | false
        15.0 | false
        -5.0 | true
    }

    def 'updateBoundsFromNewValue'(double value, boolean boundsUpdated) {

        expect:
        softBoundsMemoryAVT.updateBoundsFromNewValue(value) == boundsUpdated

        where:
        value | boundsUpdated
        -5 | false
        -50 | true
        50 | true
    }

    def 'updateValueFromBounds'(double value, double newValue) {

        given:
        Field field = StandardAVT.getDeclaredField("value")
        field.setAccessible(true)
        Field modifiersField = Field.class.getDeclaredField("modifiers")
        modifiersField.setAccessible(true)
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.PROTECTED)
        field.set(softBoundsMemoryAVT, value)
        softBoundsMemoryAVT.updateValueFromBounds()

        expect:
        softBoundsMemoryAVT.value == newValue

        where:
        value | newValue
        -40 | minValue
        30 | startValue
    }
}
