package fr.irit.smac.libs.tooling.messaging.example;

import fr.irit.smac.libs.tooling.messaging.AgentMessaging;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;
import fr.irit.smac.libs.tooling.messaging.impl.Ref;

public class SimpleMessagingExample {

	public static void main(String[] args) {
		// creation for message boxes able to handle String message type
		IMsgBox<String> msgBox1 = AgentMessaging.getMsgBox("agent1", String.class);
		IMsgBox<String> msgBox2 = AgentMessaging.getMsgBox("agent2", String.class);
		
		// Getting the ref of the agent2
		Ref<String> refAgent2 = msgBox1.getDirectory().getAgentRef("agent2");
		
		// another way to get the ref of the agent2
		refAgent2 = msgBox2.getRef();
		
		// talking to an agent without ref, This is a SLOW method !!!!
		msgBox1.send("I'm talking to you ! => SLOW", "agent2");
		
		// talking to an agent without ref, This is a fast method !!!
		// as no directory read is done in the backend
		msgBox1.send("I'm talking to you ! => FAST", refAgent2);
		
		// reading the msgBox
		for (String msg : msgBox2.getMsgs()) {
			System.out.println("Agent 2 received : " + msg);
		}
		
		// consulting the message box removes the readed messages
		// so here the message box is empty
		System.out.println("msgBox2 size after consultation : " + msgBox2.getMsgs().size());
		
	}
}
