package fr.irit.smac.libs.tooling.avt.deltamanager.impl

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDE
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl.DeterministicGDE
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl.DeterministicGDEFactory
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision.EDecision
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.StandardDMDFactory
import fr.irit.smac.libs.tooling.avt.range.IRange
import fr.irit.smac.libs.tooling.avt.range.impl.MutableRangeImpl

@Unroll
class StandardDMTest extends Specification{

    @Shared StandardDM standardDM
    @Shared StandardDM standardDM2

    public void setupSpec() {
        standardDM = new StandardDMFactory(new DeterministicGDEFactory (2.0,4.0), new StandardDMDFactory(), 3.0,6.0).createInstance(new MutableRangeImpl(1.0, 12.0))
    }

    def 'standardDM with a null deltaMax'() {

        when:
        standardDM2 = new StandardDMFactory(new DeterministicGDEFactory (2.0,4.0), new StandardDMDFactory(), 1.0).createInstance(new MutableRangeImpl(1.0, 12.0))


        then:
        standardDM2.deltaMax == 2.0
    }

    def 'standardDM with a NaN deltaMin should throw an IllegalArgumentException'() {

        when:
        standardDM2 = new StandardDMFactory(new DeterministicGDEFactory (2.0,4.0), new StandardDMDFactory(), Math.sqrt(-1)).createInstance(new MutableRangeImpl(1.0, 12.0))


        then:
        thrown(IllegalArgumentException)
    }

    def 'standardDM with a negative deltaMin should throw an IllegalArgumentException'() {

        when:
        standardDM2 = new StandardDMFactory(new DeterministicGDEFactory (2.0,4.0), new StandardDMDFactory(), -4.0).createInstance(new MutableRangeImpl(1.0, 12.0))


        then:
        thrown(IllegalArgumentException)
    }

    def 'standardDM with a NaN deltaMax should throw an IllegalArgumentException'() {

        when:
        standardDM2 = new StandardDM(1, Math.sqrt(-1), Mock(IRange), new DeterministicGDEFactory(2.0, 4.0).createInstance(),
                        new StandardDMDFactory().createInstance())

        then:
        thrown(IllegalArgumentException)
    }

    def 'standardDM with a null range should throw an IllegalArgumentException'() {

        when:
        standardDM2 = new StandardDMFactory(new DeterministicGDEFactory (2.0,4.0), new StandardDMDFactory(), 3.0).createInstance(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'standardDM with a deltaMin upper than the deltaMax should throw an IllegalArgumentException'() {

        when:
        standardDM2 = new StandardDMFactory(new DeterministicGDEFactory (2.0,4.0), new StandardDMDFactory(), 4.0,-5.0).createInstance(new MutableRangeImpl(1.0, 12.0))


        then:
        thrown(IllegalArgumentException)
    }

    def 'standardDM with a null deltaEvolution should throw an IllegalArgumentException'() {

        when:
        standardDM2 = new StandardDM(11, 12.0, Mock(IRange), null,
                        new StandardDMDFactory().createInstance())

        then:
        thrown(IllegalArgumentException)
    }

    def 'standardDM with an increaseFactor <= 1 should throw an IllegalArgumentException'() {

        given:
        IGeometricDE deterministicGDE = new DeterministicGDE(4.0, 7.0)
        Field field = DeterministicGDE.getDeclaredField("increaseFactor")
        field.setAccessible(true)
        Field modifiersField = Field.class.getDeclaredField("modifiers")
        modifiersField.setAccessible(true)
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.PROTECTED)
        field.set(deterministicGDE, -4)

        when:
        standardDM2 = new StandardDM(5, 11.0, Mock(IRange), deterministicGDE,
                        new StandardDMDFactory().createInstance())

        then:
        thrown(IllegalArgumentException)
    }

    def 'resetState'() {

        given:
        standardDM.range.upperBound = Double.POSITIVE_INFINITY

        when:
        standardDM.resetState()

        then:
        standardDM.delta == 6.0
    }

    def 'adjustDelta'(EDirection direction, EDecision nextDecision) {

        given:
        EDecision decision = standardDM.dmDecision.getNextDecision(direction)
        standardDM.adjustDelta(direction)

        expect:
        standardDM.dmDecision.getNextDecision(direction) == nextDecision

        where:
        direction | nextDecision
        EDirection.DIRECT | EDecision.INCREASE_DELTA
        EDirection.NONE | EDecision.DECREASE_DELTA
    }

    def 'adjustDelta with a null argument should throw an IllegalArgumentException'() {

        when:
        standardDM.adjustDelta(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getIncreasedDelta'(double deltaMin, double deltaMax, double increasedDelta) {

        given:
        standardDM.deltaMax = deltaMax
        standardDM.deltaMin = deltaMin

        expect:
        true
        standardDM.getIncreasedDelta() == increasedDelta

        where:
        deltaMin | deltaMax | increasedDelta
        2.0 | 6.0 | 3.0
        5.0 | 12.0 | 5.0
        25.0 | 30.0 | 25.0
    }

    def 'getDeltaMax'() {

        when:
        double deltaMax = standardDM.getDeltaMax()

        then:
        deltaMax == standardDM.deltaMax
    }

    def 'setDeltaMin with a NaN argument should throw an IllegalArgumentException'() {

        when:
        standardDM.setDeltaMin(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setDeltaMin when deltaMin is upper than deltaMax should throw an IllegalArgumentException'() {

        when:
        standardDM.setDeltaMin(40.0)

        then:
        thrown(IllegalArgumentException)
    }

    def 'setDeltaMax with a NaN argument should throw an IllegalArgumentException'() {

        when:
        standardDM.setDeltaMax(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setDeltaMax when deltaMax is lower than deltaMin should throw an IllegalArgumentException'() {

        when:
        standardDM.setDeltaMax(-40.0)

        then:
        thrown(IllegalArgumentException)
    }

    def 'setDelta'() {

        given:
        double delta = 5.0

        when:
        standardDM.setDelta(delta)

        then:
        standardDM.delta == delta
    }

    def 'setDelta with a NaN argument should throw an IllegalArgumentException'() {

        when:
        standardDM.setDelta(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'computeNbGeometricStepsFromRange with deltaMin as a NaN value should throw an IllegalArgumentException'() {

        when:
        standardDM.computeNbGeometricStepsFromRange(Math.sqrt(-1),4.0,5.0)

        then:
        thrown(IllegalArgumentException)
    }

    def 'computeNbGeometricStepsFromDeltaMax with a NaN deltaMin should throw an IllegalArgumentException'() {

        when:
        standardDM.computeNbGeometricStepsFromDeltaMax(Math.sqrt(-1), 5.0, 4.0)

        then:
        thrown(IllegalArgumentException)
    }

    def 'computeNbGeometricStepsFromDeltaMax with a NaN deltaMax should throw an IllegalArgumentException'() {

        when:
        standardDM.computeNbGeometricStepsFromDeltaMax(4.0, Math.sqrt(-1), 4.0)

        then:
        thrown(IllegalArgumentException)
    }

    def 'computeNbGeometricStepsFromDeltaMax with a NaN geometricFactor should throw an IllegalArgumentException'() {

        when:
        standardDM.computeNbGeometricStepsFromDeltaMax(4.0, 10.0, Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setGeometricStepNumber with a negative geometricStepNumber should throw an IllegalArgumentException'() {

        when:
        standardDM.setGeometricStepNumber(-4)

        then:
        thrown(IllegalArgumentException)
    }

    def 'setGeometricStepNumber with a geometricStepNumber upper than the number of steps should throw an IllegalArgumentException'() {

        when:
        standardDM.setGeometricStepNumber(50)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getDeltaIf' (EDirection direction, double delta) {

        given:
        double deltaIf = standardDM.getDeltaIf(direction)

        expect:
        deltaIf == delta

        where:
        direction | delta
        EDirection.DIRECT | 25.0
        EDirection.NONE | 1.25
    }

    def 'getDeltaIf with a null direction should throw an IllegalArgumentException'() {

        when:
        standardDM.getDeltaIf(null)

        then:
        thrown (IllegalArgumentException)
    }

    def 'reconfigure with a null range should throw an IllegalArgumentException'() {

        when:
        standardDM.reconfigure(4.0,null)

        then:
        thrown (IllegalArgumentException)
    }

    def 'reconfigure with a range equal lower than or equal to deltaMin should throw an IllegalArgumentException'() {

        when:
        standardDM.reconfigure(8.0,new BigDecimal(5.0))

        then:
        thrown (IllegalArgumentException)
    }
}
