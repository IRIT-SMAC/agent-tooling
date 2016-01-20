package fr.irit.smac.libs.tooling.messaging.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class BasicSenderTest extends Specification {

    @Shared DefaultMsgService defaultMsgService
    @Shared AgentMsgBox agentMsgBox
    @Shared BasicSender basicSender
    @Shared GroupMsgBox groupMsgBox
    @Shared String message = "hello", agent1 = "agent1", group1 = "group1"

    def setup() {

        defaultMsgService = new DefaultMsgService()
        groupMsgBox = defaultMsgService.directory.getOrCreateGroupMsgBox(group1)
        agentMsgBox = defaultMsgService.getMsgBox(agent1)
        basicSender = defaultMsgService.sender
    }

    def 'constructor'() {

        expect:
        new DefaultMsgService().sender instanceof BasicSender<T>
    }

    def 'getDirectory should return the directory of the sender'() {

        when:
        BasicMutableDirectory basicMutableDirectory = basicSender.getDirectory()

        then:
        basicMutableDirectory == basicSender.directory
    }

    def 'send without reference should send a message to the receiver'() {

        when:
        boolean isSent = basicSender.send(message,agent1)

        then:
        isSent == true
    }

    def 'send with an unknown string for the receiver should throw an IllegalArgumentException'() {

        when:
        basicSender.send(message,"agent2")

        then:
        thrown(IllegalArgumentException)
    }

    def 'send with reference should send a message to the receiver'() {

        when:
        boolean isSent = basicSender.send(message,agentMsgBox.getRef())

        then:
        isSent == true
    }

    def 'sendToGroup with reference should send a message to the group'() {

        when:
        boolean isSent = basicSender.sendToGroup(message, groupMsgBox)

        then:
        isSent == true
    }

    def 'sendToGroup without reference should send a message to the group'() {

        when:
        boolean isSent = basicSender.sendToGroup(message, group1)

        then:
        isSent == true
    }

    def 'sendToGroup with a wrong groupId should throw an IllegalArgumentException'() {

        when:
        basicSender.sendToGroup(message, "group2")

        then:
        thrown(IllegalArgumentException)
    }

    def 'broadcast'() {

        when:
        boolean isSent = basicSender.broadcast(message)

        then:
        isSent == true
    }
}
