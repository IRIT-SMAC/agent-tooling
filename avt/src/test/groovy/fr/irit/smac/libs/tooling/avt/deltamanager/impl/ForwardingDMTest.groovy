package fr.irit.smac.libs.tooling.avt.deltamanager.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl.DeterministicGDEFactory
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.StandardDMDFactory
import fr.irit.smac.libs.tooling.avt.range.impl.MutableRangeImpl

@Unroll
class ForwardingDMTest extends Specification {

    @Shared ForwardingDM forwardingDM

    def setupSpec() {

        StandardDM standardDM = new StandardDMFactory(new DeterministicGDEFactory (2.0,4.0), new StandardDMDFactory(), 3.0,6.0).createInstance(new MutableRangeImpl(1.0, 12.0))

        forwardingDM = new ForwardingDM(standardDM)
    }

    def 'ForwardingDM with a dm null should throw an IllegalArgumentException'() {

        when:
        new ForwardingDM(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getDeltaMax'() {

        when:
        double deltaMax = forwardingDM.getDeltaMax()

        then:
        deltaMax == forwardingDM.dm.getAdvancedDM().getDeltaMax()
    }

    def 'getDeltaMin'() {

        when:
        double deltaMin = forwardingDM.getDeltaMin()

        then:
        deltaMin == forwardingDM.dm.getAdvancedDM().getDeltaMin()
    }

    def 'setDeltaMin'() {

        given:
        double deltaMin = 5.0

        when:
        forwardingDM.setDeltaMin(deltaMin)

        then:
        forwardingDM.dm.getAdvancedDM().getDeltaMin() == deltaMin
    }

    def 'setDeltaMax'() {

        given:
        double deltaMax = 5.0

        when:
        forwardingDM.setDeltaMax(deltaMax)

        then:
        forwardingDM.dm.getAdvancedDM().getDeltaMax() == deltaMax
    }

    def 'setDelta'() {

        given:
        double delta = 4.0

        when:
        forwardingDM.setDelta(delta)

        then:
        forwardingDM.dm.getAdvancedDM().getDelta() == delta
    }
}
