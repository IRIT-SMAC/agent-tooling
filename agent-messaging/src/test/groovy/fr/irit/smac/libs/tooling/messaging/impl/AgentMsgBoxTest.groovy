package fr.irit.smac.libs.tooling.messaging.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgContainer

@Unroll
class AgentMsgBoxTest extends Specification{

    @Shared AgentMsgBox agentMsgBox
    @Shared DefaultMsgService defaultMsgService
    @Shared String message = "hello", agent1 = "agent1", group1 = "group1"

    def setup() {

        defaultMsgService = new DefaultMsgService()
        agentMsgBox = defaultMsgService.getMsgBox(agent1)
    }

    def 'constructor'() {

        expect:
        new DefaultMsgService().getMsgBox(agent1) instanceof AgentMsgBox
    }

    def 'getRef should return the reference of the agentMsgBox'() {

        when:
        Ref<String> ref = agentMsgBox.getRef()

        then:
        ref == agentMsgBox
    }

    def 'getDirectory should return the directory of the agentMsgBox'() {

        when:
        BasicMutableDirectory directory = agentMsgBox.getDirectory()

        then:
        directory == agentMsgBox.directory
    }

    def 'subscribeToGroup without reference should subscribe the agent to the group1'() {

        when:
        Ref<String> refGroup = agentMsgBox.subscribeToGroup(group1)

        then:
        refGroup.toString() == "@" + group1
    }

    def 'subscribeToGroup with reference should subscribe the agent to the group1'() {

        given:
        Ref<String> refGroup = agentMsgBox.subscribeToGroup(group1)

        when:
        agentMsgBox.subscribeToGroup(refGroup)

        then:
        true
    }

    def 'unsubscribeToGroup without reference should unsubscribe the agent to the group1'() {

        given:
        Ref<String> refGroup = agentMsgBox.subscribeToGroup(group1)

        when:
        agentMsgBox.unsubscribeToGroup(group1)

        then:
        true
    }

    def 'unsubscribeToGroup with reference should subscribe the agent to the group1'() {

        given:
        Ref<String> refGroup = agentMsgBox.subscribeToGroup(group1)

        when:
        agentMsgBox.unsubscribeToGroup(refGroup)

        then:
        true
    }

    def 'send without reference should send a message to the receiver'() {

        when:
        boolean isSent = agentMsgBox.send(message,agent1)

        then:
        isSent == true
    }

    def 'send with a reference should send a message to the receiver'() {

        when:
        boolean isSent = agentMsgBox.send(message,agentMsgBox.getRef())

        then:
        isSent == true
    }

    def 'sendToGroup without a reference should send a message to the receiver'() {

        given:
        Ref<String> refGroup = defaultMsgService.directory.getOrCreateGroupMsgBox(group1)

        when:
        boolean isSent = agentMsgBox.sendToGroup(message,group1)

        then:
        isSent == true
    }

    def 'sendToGroup with a reference should send a message to the receiver'() {

        given:
        Ref<String> refGroup = defaultMsgService.directory.getOrCreateGroupMsgBox(group1)

        when:
        boolean isSent = agentMsgBox.sendToGroup(message,refGroup)

        then:
        isSent == true
    }

    def 'broadcast'() {

        when:
        boolean isBroadcast = agentMsgBox.broadcast(message)

        then:
        isBroadcast == true
    }

    def 'getMsgs should return the messages of the box'() {

        when:
        List<String> msgs = agentMsgBox.getMsgs()

        then:
        msgs != null
    }

    def 'getMsgSink should return the container of the messages'() {

        when:
        IMsgContainer msgContainer = agentMsgBox.getMsgSink()

        then:
        msgContainer != null
    }

    def 'dispose should remove the agentMsgBox from its directory'() {

        when:
        agentMsgBox.dispose()

        then:
        !agentMsgBox.directory.agentDirectory.contains(agentMsgBox)
    }
}
