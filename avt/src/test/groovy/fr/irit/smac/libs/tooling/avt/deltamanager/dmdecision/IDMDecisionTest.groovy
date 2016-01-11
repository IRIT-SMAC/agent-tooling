package fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision

import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision.EDecision

@Unroll
class IDMDecisionTest extends Specification {

    def 'EDecision values'(String decisionString, EDecision decision) {
        
        given:
        EDecision decisionValue = EDecision.valueOf(decisionString)
        
        expect:
        decisionValue == decision
        
        where:
        decisionString | decision
        "INCREASE_DELTA" | EDecision.INCREASE_DELTA
        "DECREASE_DELTA" | EDecision.DECREASE_DELTA
        "SAME_DELTA" | EDecision.SAME_DELTA
    }
}