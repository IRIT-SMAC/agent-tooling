package fr.irit.smac.libs.tooling.avt.deltamanager.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl.DeterministicGDEFactory
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision.EDecision
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.StandardDMDFactory
import fr.irit.smac.libs.tooling.avt.range.IRange

@Unroll
class StandardDMTest extends Specification{

    @Shared StandardDM standardDM
    @Shared StandardDM standardDMConstructor

    public void setupSpec() {
        standardDM = new StandardDM(5.0, 12.0, Mock(IRange), new DeterministicGDEFactory(2.0, 4.0).createInstance(),
                        new StandardDMDFactory().createInstance())
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

    def 'adjustDelta with a null argument should thrown an IllegalArgumentException'() {

        when:
        standardDM.adjustDelta(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getIncreasedDelta'(double deltaMin, double deltaMax) {

        given:
        standardDM.deltaMax = deltaMax
        standardDM.deltaMin = deltaMin
        expect:
        standardDM.getIncreasedDelta()

        where:
        deltaMin | deltaMax
        5.0 | 12.0
        25.0 | 30.0
    }

    def 'getDeltaMax'() {

        when:
        double deltaMax = standardDM.getDeltaMax()

        then:
        deltaMax == standardDM.deltaMax
    }

    def 'setDeltaMin with a NaN argument should thrown an IllegalArgumentException'() {

        when:
        standardDM.setDeltaMin(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setDeltaMin when deltaMin is upper than deltaMax should thrown an IllegalArgumentException'() {

        when:
        standardDM.setDeltaMin(40.0)

        then:
        thrown(IllegalArgumentException)
    }

    def 'setDeltaMax with a NaN argument should thrown an IllegalArgumentException'() {

        when:
        standardDM.setDeltaMax(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setDeltaMax when deltaMax is lower than deltaMin should thrown an IllegalArgumentException'() {

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

    def 'setDelta with a NaN argument should thrown an IllegalArgumentException'() {

        when:
        standardDM.setDelta(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'computeNbGeometricStepsFromRange with deltaMin as a NaN value should thrown an IllegalArgumentException'() {

        when:
        standardDM.computeNbGeometricStepsFromRange(Math.sqrt(-1),4.0,5.0)

        then:
        thrown(IllegalArgumentException)
    }

    def 'computeNbGeometricStepsFromDeltaMax with a NaN deltaMin should thrown an IllegalArgumentException'() {

        when:
        standardDM.computeNbGeometricStepsFromDeltaMax(Math.sqrt(-1), 5.0, 4.0)

        then:
        thrown(IllegalArgumentException)
    }

    def 'computeNbGeometricStepsFromDeltaMax with a NaN deltaMax should thrown an IllegalArgumentException'() {

        when:
        standardDM.computeNbGeometricStepsFromDeltaMax(4.0, Math.sqrt(-1), 4.0)

        then:
        thrown(IllegalArgumentException)
    }

    def 'computeNbGeometricStepsFromDeltaMax with a NaN geometricFactor should thrown an IllegalArgumentException'() {

        when:
        standardDM.computeNbGeometricStepsFromDeltaMax(4.0, 10.0, Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }

    def 'setGeometricStepNumber with a negative geometricStepNumber should thrown an IllegalArgumentException'() {

        when:
        standardDM.setGeometricStepNumber(-4)

        then:
        thrown(IllegalArgumentException)
    }

    def 'setGeometricStepNumber with a geometricStepNumber upper than the number of steps should thrown an IllegalArgumentException'() {

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

    def 'getDeltaIf with a null direction should thrown an IllegalArgumentException'() {

        when:
        standardDM.getDeltaIf(null)

        then:
        thrown (IllegalArgumentException)
    }
}
