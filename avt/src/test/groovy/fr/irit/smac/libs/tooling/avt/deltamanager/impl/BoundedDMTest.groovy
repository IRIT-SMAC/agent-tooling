package fr.irit.smac.libs.tooling.avt.deltamanager.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl.DeterministicGDEFactory
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.StandardDMDFactory
import fr.irit.smac.libs.tooling.avt.range.impl.MutableRangeImpl

@Unroll
class BoundedDMTest extends Specification {

    @Shared BoundedDM boundedDM
    
    def setupSpec() {

        StandardDMFactory standardDMFactory = new StandardDMFactory(new DeterministicGDEFactory (2.0,4.0), new StandardDMDFactory(), 3.0,6.0)
        boundedDM = new BoundedDMFactory(standardDMFactory).createInstance(new MutableRangeImpl(1.0, 12.0))
    }
    
    def 'reconfigure'() {

        given:
        double deltaMin = 4
        
        when:
        boundedDM.reconfigure(deltaMin, 5.0)

        then:
        boundedDM.deltaMin == deltaMin
        boundedDM.deltaMax == 4.0
        boundedDM.nbGeometricSteps == 1
        boundedDM.delta == 4.0
        
    }

    def 'ensureBoundedDelta'() {

        given:
        boundedDM.dm.delta = -5.0

        when:
        boundedDM.ensureBoundedDelta()

        then:
        boundedDM.delta == 4.0
    }

    def 'getBoundedDelta with a NaN delta should throw an IllegalArgumentException'() {

        when:
        boundedDM.getBoundedDelta(Double.NaN)

        then:
        thrown(IllegalArgumentException)
    }
}
