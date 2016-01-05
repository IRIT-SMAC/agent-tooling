package fr.irit.smac.libs.tooling.avt.impl;

import static org.junit.Assert.*

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.AVTBuilder
import fr.irit.smac.libs.tooling.avt.EFeedback
import fr.irit.smac.libs.tooling.avt.IAVT

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

    def 'updateValueFromBounds'(double value, double newValue) {

        given:
        Field field = StandardAVT.getDeclaredField("value")
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.PROTECTED);
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
