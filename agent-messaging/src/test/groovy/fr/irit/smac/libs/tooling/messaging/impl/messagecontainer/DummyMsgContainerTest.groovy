package fr.irit.smac.libs.tooling.messaging.impl.messagecontainer

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class DummyMsgContainerTest extends Specification {

    @Shared DummyMsgContainer dummyMsgContainer

    def setup() {

        dummyMsgContainer = new DummyMsgContainer()
    }

    def 'constructor'() {

        expect:
        new DummyMsgContainer() instanceof DummyMsgContainer
    }

    def 'putMsg should return false'() {

        when:
        boolean isPut = dummyMsgContainer.putMsg("hello")

        then:
        isPut == false
    }

    def 'getMsgs should throw an IllegalStateException'() {

        when:
        dummyMsgContainer.getMsgs()

        then:
        thrown(IllegalStateException)
    }
}
