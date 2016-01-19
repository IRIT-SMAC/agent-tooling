package fr.irit.smac.libs.tooling.messaging

import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.messaging.impl.DefaultMsgService

@Unroll
class AgentMessagingTest extends Specification {

    def 'constructor should be private'() {

        when:
        Constructor constructor = AgentMessaging.class.getDeclaredConstructor()

        then:
        Modifier.isPrivate(constructor.getModifiers()) == true

        and:
        constructor.setAccessible(true)
        constructor.newInstance()
    }

    def 'getMsgBox should create a message box'() {

        when:
        IMsgBox<String> msgBox = AgentMessaging.getMsgBox("agent1", String.class)

        then:
        msgBox instanceof IMsgBox
    }

    def 'getMsgService'() {

        when:
        IMsgService<String> msgService = AgentMessaging.getMsgService(String.class)

        then: 'a message service is created'
        msgService instanceof DefaultMsgService

        when:
        IMsgService<String> msgService2 = AgentMessaging.getMsgService(String.class)

        then: 'the message service is got'
        msgService == msgService2
    }
}
