package fr.irit.smac.libs.tooling.avt;

import spock.lang.Specification

class EFeedbackTest extends Specification{

    def 'EFeedback values'(String feedbackString, EFeedback feedback) {
        
        given:
        EFeedback decisionValue = EFeedback.valueOf(feedbackString)
        
        expect:
        decisionValue == feedback
        
        where:
        feedbackString | feedback
        "GREATER" | EFeedback.GREATER
        "LOWER" | EFeedback.LOWER
        "GOOD" | EFeedback.GOOD
    }
    
    def 'EFeedback string'(String feedbackString, EFeedback feedback) {
        
        expect:
        feedback.toString() == feedbackString
        
        where:
        feedbackString | feedback
        "+" | EFeedback.GREATER
        "-" | EFeedback.LOWER
        "g" | EFeedback.GOOD
    }

}
