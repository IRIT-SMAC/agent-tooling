package fr.irit.smac.libs.tooling.avt

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class EMessageExceptionTest extends Specification{

    def 'EMessageException valueOf'() {

        expect:
        EMessageException.valueOf("START_VALUE_NAN") == EMessageException.START_VALUE_NAN
    }
}
