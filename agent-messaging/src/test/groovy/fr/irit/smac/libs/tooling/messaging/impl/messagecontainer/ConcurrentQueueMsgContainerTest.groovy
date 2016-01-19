package fr.irit.smac.libs.tooling.messaging.impl.messagecontainer

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ConcurrentQueueMsgContainerTest extends Specification{

    @Shared ConcurrentQueueMsgContainer concurrentQueueMsgContainer

    def setup() {

        concurrentQueueMsgContainer = new ConcurrentQueueMsgContainer()
    }

    def 'constructor'(){

        expect:
        new ConcurrentQueueMsgContainer() instanceof ConcurrentQueueMsgContainer
    }

    def 'putMsg should add a message to the queue'() {

        when:
        boolean isPut = concurrentQueueMsgContainer.putMsg("hello")

        then:
        isPut == true
    }

    def 'getMsgs should return the messages of the queue'() {

        given:
        concurrentQueueMsgContainer.putMsg("hi")
        concurrentQueueMsgContainer.putMsg("bye")

        when:
        List<String> messages = concurrentQueueMsgContainer.getMsgs()

        then:
        messages.size() == 2
    }
}
