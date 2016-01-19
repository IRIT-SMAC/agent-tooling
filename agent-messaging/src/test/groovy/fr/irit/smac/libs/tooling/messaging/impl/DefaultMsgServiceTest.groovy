package fr.irit.smac.libs.tooling.messaging.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class DefaultMsgServiceTest extends Specification {

    @Shared DefaultMsgService defaultMsgService
    @Shared String message = "hello", agent1 = "agent1", agent2 = "agent2", group1 = "group1"

    def setup() {

        defaultMsgService = new DefaultMsgService()
    }

    def 'constructor'() {

        expect:
        new DefaultMsgService() instanceof DefaultMsgService
    }

    def 'getDirectory should return the directory of the service'() {

        when:
        BasicMutableDirectory directory = defaultMsgService.getDirectory()

        then:
        defaultMsgService.directory == directory
    }

    def 'send without reference should send a message to an agent'() {

        given:
        AgentMsgBox agentMsgBox = defaultMsgService.getMsgBox(agent1)

        when:
        boolean isSent = defaultMsgService.send(message,agent1)

        then:
        isSent == true
    }

    def 'send with reference should send a message to an agent'() {

        given:
        Ref<String> refAgent = defaultMsgService.getMsgBox(agent1)

        when:
        boolean isSent = defaultMsgService.send(message,refAgent)

        then:
        isSent == true
    }

    def 'sendToGroup without reference should send a message to a group'() {

        given:
        defaultMsgService.directory.getOrCreateGroupMsgBox(group1)

        when:
        boolean isSent = defaultMsgService.sendToGroup(message,group1)

        then:
        isSent == true
    }

    def 'sendToGroup with reference should send a message to a group'() {

        given:
        Ref<String> refGroup = defaultMsgService.directory.getOrCreateGroupMsgBox(group1)

        when:
        boolean isSent = defaultMsgService.sendToGroup(message,refGroup)

        then:
        isSent == true
    }

    def 'broadcast should broadcast a message to all the agents'() {

        when:
        boolean isBroadcast = defaultMsgService.broadcast("hi")

        then:
        isBroadcast == true
    }

    def 'getMsgBox should create a msgBox to an agent'() {

        when:
        AgentMsgBox agentMsgBox = defaultMsgService.getMsgBox(agent2)

        then:
        agentMsgBox.toString() == "@" + agent2
    }
}
