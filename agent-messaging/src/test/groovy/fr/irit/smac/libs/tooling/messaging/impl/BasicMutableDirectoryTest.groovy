package fr.irit.smac.libs.tooling.messaging.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class BasicMutableDirectoryTest extends Specification {

    @Shared BasicMutableDirectory basicMutableDirectory
    @Shared String agent1 = "agent1", agent2 = "agent2", group1 = "group1", group2 = "group2",unknown = "unknown"

    def setup() {

        basicMutableDirectory = new BasicMutableDirectory()
    }

    def 'constructor'() {

        expect:
        new BasicMutableDirectory() instanceof BasicMutableDirectory
    }

    def 'createAgentMsgBox should create an AgentMsgBox'() {

        when:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)

        then:
        refAgent instanceof AgentMsgBox
    }

    def 'createAgentMsgBox with an agent already associated with a msgBox should thrown an IllegalArgumentException'() {

        given:
        basicMutableDirectory.createAgentMsgBox(agent1)

        when:
        basicMutableDirectory.createAgentMsgBox(agent1)

        then:
        thrown(IllegalArgumentException)
    }

    def 'removeAgentMsgBox should remove an AgentMsgBox'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)
        Ref<String> refGroup = basicMutableDirectory.getOrCreateGroupMsgBox(group2)

        basicMutableDirectory.subscribeAgentToGroup(refAgent,refGroup)

        when:
        basicMutableDirectory.removeAgentMsgBox(refAgent)

        then:
        !basicMutableDirectory.agentDirectory.contains(refAgent)
        for (GroupMsgBox group : basicMutableDirectory.groupDirectory.values()) {
            !group.agentRefs.contains(refAgent)
        }
    }

    def 'getOrCreateGroupMsgBox should create a GroupMsgBox'() {

        when:
        Ref<String> refGroup = basicMutableDirectory.getOrCreateGroupMsgBox(group2)

        then:
        refGroup instanceof GroupMsgBox
        basicMutableDirectory.groupDirectory.size() == 2
    }

    def 'getOrCreateGroupMsgBox should get an existing GroupMsgBox'() {

        given:
        Ref<String> refGroup = basicMutableDirectory.getOrCreateGroupMsgBox(group2)

        when:
        Ref<String> refGroup2 = basicMutableDirectory.getOrCreateGroupMsgBox(group2)

        then:
        refGroup == refGroup2
    }

    def 'getAgentRef should return the reference of an agent'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)

        when:
        Ref<String> refAgent2 = basicMutableDirectory.getAgentRef(agent1)

        then:
        refAgent2 == refAgent
    }

    def 'getAgentsRef should return the reference of all the agents'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)

        when:
        ArrayList<Ref<String>> agents = basicMutableDirectory.getAgentsRef()

        then:
        agents.size() == 1
    }

    def 'getGroupRef should return the reference of a group'() {

        given:
        Ref<String> refGroup = basicMutableDirectory.getOrCreateGroupMsgBox(group2)

        when:
        Ref<String> refGroup2 = basicMutableDirectory.getGroupRef(group2)

        then:
        refGroup2 == refGroup
    }

    def 'getAgentsOfGroup should return the reference of all the groups'() {

        when:
        ArrayList<Ref<String>> groups = basicMutableDirectory.getGroupsRef()

        then:
        groups.size() == 1
    }

    def 'getAgentsOfGroup should return all the agents of a group'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)
        Ref<String> refAgent2 = basicMutableDirectory.createAgentMsgBox(agent2)
        Ref<String> refGroup = basicMutableDirectory.getOrCreateGroupMsgBox(group1)
        refGroup.subscribeAgent(refAgent)
        refGroup.subscribeAgent(refAgent2)

        when:
        Set<Ref<String>> agentsOfAGroup = basicMutableDirectory.getAgentsOfGroup(refGroup)

        then:
        agentsOfAGroup.size() == 2
    }

    def 'subscribeAgentToGroup with a reference should subscribe an agent to a group'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)
        Ref<String> refGroup = basicMutableDirectory.getOrCreateGroupMsgBox(group1)
        refGroup.unsubscribeAgent(refAgent)

        when:
        basicMutableDirectory.subscribeAgentToGroup(refAgent,refGroup)

        then:
        refGroup.getAgents().contains(refAgent)
    }

    def 'unsubscribeAgentFromGroup with reference should unsubscribe an agent from a group'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)
        Ref<String> refGroup = basicMutableDirectory.getOrCreateGroupMsgBox(group1)

        when:
        basicMutableDirectory.unsubscribeAgentFromGroup(refAgent,refGroup)

        then:
        !refGroup.getAgents().contains(refAgent)
    }

    def 'unsubscribeAgentFromGroup with a wrong reference for the group should throw an IllegalArgumentException'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)

        when:
        basicMutableDirectory.unsubscribeAgentFromGroup(refAgent,Mock(Ref))

        then:
        thrown(IllegalArgumentException)
    }

    def 'subscribeAgentToGroup without reference should subscribe an agent to a group'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)

        when:
        Ref<String> refGroup = basicMutableDirectory.subscribeAgentToGroup(refAgent,group2)

        then:
        refGroup.getAgents().contains(refAgent)
    }

    def 'subscribeAgentToGroup with a wrong reference for the group should throw an IllegalArgumentException'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)

        when:
        basicMutableDirectory.subscribeAgentToGroup(refAgent,Mock(Ref))

        then:
        thrown(IllegalArgumentException)
    }

    def 'unsubscribeAgentFromGroup without reference should unsubscribe an agent from a group'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)
        Ref<String> refGroup = basicMutableDirectory.getOrCreateGroupMsgBox(group1)
        basicMutableDirectory.subscribeAgentToGroup(refAgent,group2)

        when:
        basicMutableDirectory.unsubscribeAgentFromGroup(refAgent,group2)

        then:
        !refGroup.getAgents().contains(refAgent)
    }

    def 'unsubscribeAgentFromGroup with a wrong string for the group should throw an IllegalArgumentException'() {

        given:
        Ref<String> refAgent = basicMutableDirectory.createAgentMsgBox(agent1)

        when:
        basicMutableDirectory.unsubscribeAgentFromGroup(refAgent,unknown)

        then:
        thrown(IllegalArgumentException)
    }
}
