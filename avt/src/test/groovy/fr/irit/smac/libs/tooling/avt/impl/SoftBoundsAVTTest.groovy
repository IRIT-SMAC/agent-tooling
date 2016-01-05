package fr.irit.smac.libs.tooling.avt.impl;

import static org.junit.Assert.*

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import spock.lang.Shared
import spock.lang.Specification;
import spock.lang.Unroll;
import fr.irit.smac.libs.tooling.avt.AVTBuilder;
import fr.irit.smac.libs.tooling.avt.EFeedback
import fr.irit.smac.libs.tooling.avt.IAVT;
import fr.irit.smac.libs.tooling.avt.IAdvancedAVT
import fr.irit.smac.libs.tooling.avt.deltamanager.IAdvancedDM
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection
import fr.irit.smac.libs.tooling.avt.deltamanager.impl.ForwardingDM
import fr.irit.smac.libs.tooling.avt.range.IMutableRange
import fr.irit.smac.libs.tooling.avt.range.impl.MutableRangeImpl

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

    def 'setValue'() {

        given:
        double newValue = 0.2

        when:
        softBoundsAVT.setValue(newValue)

        then:
        softBoundsAVT.value == newValue
    }

    def 'adjustValue'(EFeedback feedback, double newValue) {

        given:
        softBoundsAVT.adjustValue(feedback)

        expect:
        newValue == softBoundsAVT.value

        where:
        feedback | newValue
        EFeedback.GOOD | startValue
        EFeedback.GREATER | 0.532
        EFeedback.LOWER | 0.46799999999999997
    }

    def 'adjustValue with a null argument should thrown an IllegalArgumentException'() {

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

    def 'getValueIf with a NaN argument should thrown an IllegalArgumentException'() {

        when:
        softBoundsAVT.getValueIf(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'updateBoundsFromValue with a NaN argument should thrown an IllegalArgumentException'() {

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

    def 'setLowerBoundFromValue with a NaN argument should thrown an IllegalArgumentException'() {

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

    def 'setUpperBoundFromValue with a NaN argument should thrown an IllegalArgumentException'() {

        when:
        softBoundsAVT.setUpperBoundFromValue(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setValueFromBounds with a NaN argument should thrown an IllegalArgumentException'() {

        when:
        softBoundsAVT.setValueFromBounds(Math.sqrt(-1))

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
        field.set(softBoundsAVT, value)
        softBoundsAVT.updateValueFromBounds()

        expect:
        softBoundsAVT.value == newValue

        where:
        value | newValue
        20 | maxValue
        -20 | minValue
    }
}
