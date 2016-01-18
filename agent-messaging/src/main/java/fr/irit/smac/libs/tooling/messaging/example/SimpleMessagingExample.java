/*
 * #%L
 * agent-messaging
 * %%
 * Copyright (C) 2014 - 2015 IRIT - SMAC Team
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package fr.irit.smac.libs.tooling.messaging.example;

import fr.irit.smac.libs.tooling.messaging.AgentMessaging;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;
import fr.irit.smac.libs.tooling.messaging.impl.Ref;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleMessagingExample.
 */
public class SimpleMessagingExample {

    /**
     * The main method.
     *
     * @param args the arguments
     */
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
