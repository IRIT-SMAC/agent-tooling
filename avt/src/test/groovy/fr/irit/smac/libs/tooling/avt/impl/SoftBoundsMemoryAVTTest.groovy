package fr.irit.smac.libs.tooling.avt.impl

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
        softBoundsMemoryAVT.valuesHistory.clear()
    }

    def 'setLowerBound'() {

        given:
        double lowerBound = -30
        
        when:
        softBoundsMemoryAVT.setLowerBound(-30)

        then:
        softBoundsMemoryAVT.range.lowerBound == lowerBound
    }

    def 'setUpperBound'() {

        given:
        double upperBound = 11
        
        when:
        softBoundsMemoryAVT.setUpperBound(upperBound)

        then:
        softBoundsMemoryAVT.range.upperBound == upperBound
    }

    def 'updateHistoryFromBounds'() {

        given:
        double lowerBound = softBoundsMemoryAVT.range.lowerBound
        double upperBound = softBoundsMemoryAVT.range.upperBound
        softBoundsMemoryAVT.valuesHistory.add((Double)-30.0)
        softBoundsMemoryAVT.valuesHistory.add((Double)0.2)
        softBoundsMemoryAVT.valuesHistory.add((Double)7.0)
        softBoundsMemoryAVT.valuesHistory.add((Double)25.0)

        when:
        softBoundsMemoryAVT.updateHistoryFromBounds()

        then:
        softBoundsMemoryAVT.valuesHistory.toArray() == [lowerBound,0.2,upperBound,upperBound]
    }

    def 'setValue with a Nan argument should throw an IllegalArgumentException'() {

        when:
        softBoundsMemoryAVT.setValue(Double.NaN)

        then:
        thrown(IllegalArgumentException)
    }

    def 'adjustValue'(EFeedback feedback, double value) {

        given:
        softBoundsMemoryAVT.range = new MutableRangeImpl(minValue, maxValue)
        softBoundsMemoryAVT.adjustValue(feedback)
        double lowerBound = softBoundsMemoryAVT.range.lowerBound
        double upperBound = softBoundsMemoryAVT.range.upperBound
        double delta = softBoundsMemoryAVT.deltaManager.delta
        
        expect:
        softBoundsMemoryAVT.deltaManager.delta == delta
        softBoundsMemoryAVT.value == value
        softBoundsMemoryAVT.range.lowerBound == lowerBound
        softBoundsMemoryAVT.range.upperBound == upperBound
        
        where:
        feedback | value
        EFeedback.GOOD | 0.5
        EFeedback.GREATER | 0.5640000000000001
        EFeedback.LOWER | 0.436
    }

    def 'adjustValue with a null argument should throw an IllegalArgumentException'() {

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

    def 'getValueIf with a NaN argument should throw an IllegalArgumentException'() {

        when:
        softBoundsMemoryAVT.getValueIf(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'registerNewValueAndUpdateBounds'() {

        given:
        double value = -30.0
        softBoundsMemoryAVT.valuesHistory.add((Double)0.2)

        when:

        softBoundsMemoryAVT.registerNewValueAndUpdateBounds(value)

        then:
        softBoundsMemoryAVT.getRange().getLowerBound() == value
    }

    def 'updateBoundsFromHistory with an empty history should return false'() {

        given:
        softBoundsMemoryAVT.range = new MutableRangeImpl(minValue, maxValue)
        softBoundsMemoryAVT.valuesHistory.clear()

        when:
        boolean boundsUpdated = softBoundsMemoryAVT.updateBoundsFromHistory()

        then:
        boundsUpdated == false
    }

    def 'updateBoundsFromHistory with an history containing numbers differents from the bounds should return true' (double value1, double value2) {

        given:
        softBoundsMemoryAVT.range = new MutableRangeImpl(minValue, maxValue)
        softBoundsMemoryAVT.valuesHistory.clear()
        softBoundsMemoryAVT.valuesHistory.add((Double)value1)
        softBoundsMemoryAVT.valuesHistory.add((Double)value2)
        boolean boundsUpdated = softBoundsMemoryAVT.updateBoundsFromHistory()

        expect:
        boundsUpdated == true

        where:
        value1 | value2
        0.188 | 0.088
        0.088 | 1.88
        softBoundsMemoryAVT.effectiveLowerBound | softBoundsMemoryAVT.effectiveUpperBound
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
        softBoundsMemoryAVT.value = value
        softBoundsMemoryAVT.effectiveLowerBound = -5.0
        softBoundsMemoryAVT.effectiveUpperBound = 10.0
        softBoundsMemoryAVT.updateValueFromBounds()

        expect:
        softBoundsMemoryAVT.value == newValue

        where:
        value | newValue
        -20.0 | -5.0
        20.0 | 10.0
    }
}
