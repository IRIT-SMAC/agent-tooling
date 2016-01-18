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
    @Shared double max = 6.0
    @Shared double min = 3.0

    def setupSpec() {

        StandardDM standardDM = new StandardDMFactory(new DeterministicGDEFactory (2.0,4.0), new StandardDMDFactory(), min, max).createInstance(new MutableRangeImpl(1.0, 12.0))

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
        double delta = forwardingDM.getDeltaMax()

        then:
        delta == max
    }

    def 'getDeltaMin'() {

        when:
        double deltaMin = forwardingDM.getDeltaMin()

        then:
        deltaMin == min
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
