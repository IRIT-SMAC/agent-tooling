package fr.irit.smac.libs.tooling.avt.deltamanager

import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection

@Unroll
class IDeltaManagerTest extends Specification {

    def 'EDecision values'(String directionString, EDirection direction) {

        given:
        EDirection directionValue = EDirection.valueOf(directionString)

        expect:
        directionValue == direction

        where:
        directionString | direction
        "DIRECT" | EDirection.DIRECT
        "INDIRECT" | EDirection.INDIRECT
        "NONE" | EDirection.NONE
    }
}
