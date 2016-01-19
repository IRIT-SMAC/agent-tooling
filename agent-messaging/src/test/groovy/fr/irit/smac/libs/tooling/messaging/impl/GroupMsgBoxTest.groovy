package fr.irit.smac.libs.tooling.messaging.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class GroupMsgBoxTest extends Specification{

    @Shared DefaultMsgService defaultMsgService
    @Shared GroupMsgBox groupMsgBox
    @Shared Ref<String> refAgent
    @Shared String message = "hello", group1 = "group1"

    def setup() {

        defaultMsgService = new DefaultMsgService()
        groupMsgBox = defaultMsgService.directory.getOrCreateGroupMsgBox(group1)
        AgentMsgBox agentMsgBox = defaultMsgService.getMsgBox("agent1")
        refAgent = agentMsgBox.getRef()
    }

    def 'create a groupMsgBox'() {

        expect:
        new DefaultMsgService().directory.getOrCreateGroupMsgBox(group1) instanceof GroupMsgBox
    }

    def 'subscribeAgent should subscribe an agent to the group'() {

        when:
        groupMsgBox.subscribeAgent(refAgent)

        then:
        groupMsgBox.agentRefs.contains(refAgent)
    }

    def 'unsubscribeAgent should unsubscribe an agent to the group'() {

        when:
        groupMsgBox.unsubscribeAgent(refAgent)

        then:
        !groupMsgBox.agentRefs.contains(refAgent)
    }

    def 'getAgents should return the agents of the group'() {

        when:
        Set<Ref<String>> agents = groupMsgBox.getAgents()

        then:
        agents == groupMsgBox.agentRefs
    }

    def 'getMsgSink should return the instance of the group'() {

        when:
        GroupMsgBox groupMsgBox = groupMsgBox.getMsgSink()

        then:
        groupMsgBox == groupMsgBox
    }

    def 'putMsg should send a message to the agents of the group'() {

        given:
        groupMsgBox.subscribeAgent(refAgent)

        when:
        boolean isSent = groupMsgBox.putMsg(message)

        then:
        isSent == true
    }

    def 'putMsg should send a message to the agents who are not disposed'() {

        given:
        AgentMsgBox agentMsgBox2 = defaultMsgService.getMsgBox("agent0")
        agentMsgBox2.msgContainer = agentMsgBox2.dummyMsgContainer
        Ref<String> refAgent2 = agentMsgBox2.getRef()
        groupMsgBox.subscribeAgent(refAgent)
        groupMsgBox.subscribeAgent(refAgent2)

        when:
        boolean isSent = groupMsgBox.putMsg(message)

        then:
        isSent == false
    }
}
