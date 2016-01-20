package fr.irit.smac.libs.tooling.messaging.impl.messagecontainer;

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class BlockingQueueMsgContainerTest extends Specification {

    @Shared BlockingQueueMsgContainer blockingQueueMsgContainer

    def setup() {

        blockingQueueMsgContainer = new BlockingQueueMsgContainer()
    }

    def 'constructor'(){

        expect:
        new BlockingQueueMsgContainer() instanceof BlockingQueueMsgContainer
    }
    
    def 'putMsg should put a message in the queue'() {
        
        when:
        boolean isPut = blockingQueueMsgContainer.putMsg("hello")
        
        then:
        isPut == true
    }
    
    def 'getMsgs should get the messages of the queue'() {
        
        given:
        blockingQueueMsgContainer.putMsg("hi")
        blockingQueueMsgContainer.putMsg("bye")
        
        when:
        List<String> messages = blockingQueueMsgContainer.getMsgs()
        
        then:
        messages.size() == 2
    }

}
