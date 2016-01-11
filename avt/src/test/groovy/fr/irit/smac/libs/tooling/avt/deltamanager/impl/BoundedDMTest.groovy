package fr.irit.smac.libs.tooling.avt.deltamanager.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl.DeterministicGDEFactory
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.StandardDMDFactory
import fr.irit.smac.libs.tooling.avt.range.IRange

@Unroll
class BoundedDMTest extends Specification {

    @Shared BoundedDM boundedDM

    def setupSpec() {

        StandardDM standardDM = new StandardDM(5.0, 12.0, Mock(IRange), new DeterministicGDEFactory(2.0, 4.0).createInstance(),
                        new StandardDMDFactory().createInstance())
        boundedDM = new BoundedDM(standardDM)
    }

    def 'reconfigure'() {

        when:
        boundedDM.reconfigure(4, 5.0)

        then:
        true
    }

    def 'ensureBoundedDelta'() {

        given:
        double delta = -5.0
        boundedDM.dm.delta = delta

        when:
        boundedDM.dm.getAdvancedDM().getDeltaMin()

        boundedDM.ensureBoundedDelta()

        then:
        boundedDM.delta != -delta
    }

    def 'getBoundedDelta with a NaN delta should thrown an IllegalArgumentException'() {

        when:
        boundedDM.getBoundedDelta(Math.sqrt(-1))

        then:
        thrown(IllegalArgumentException)
    }
}
