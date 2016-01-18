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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.irit.smac.libs.tooling.messaging.AgentMessaging;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;
import fr.irit.smac.libs.tooling.messaging.impl.Ref;

// TODO: Auto-generated Javadoc
/**
 * The Class AdvancedMessagingExample.
 */
public class AdvancedMessagingExample {

    /**
     * Instantiates a new advanced messaging example.
     */
    private AdvancedMessagingExample() {

    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static final void main(String[] args) {

        long randomSeed = 1234L;
        int nbAgents = 100;
        int nbCycles = 1000;
        double groupCreationProb = .2;
        double msgBoxDisableEnalbeProb = .2;

        Random random = new Random(randomSeed);

        // creation of agents
        List<ChattingAgent> chattingAgents = new ArrayList<ChattingAgent>();
        List<String> groupIds = new ArrayList<String>();

        int choice = 0; // in order to generate a first deterministic type of
                        // agent
        System.out.println("Creation of agents...");
        for (int i = 0; i < nbAgents; i++) {

            ChattingAgent newAgent = null;
            String agentId = "a" + i;

            switch (choice) {
            case 0: // generating a chatting to a group agent
                String groupId = null;
                if (groupIds.isEmpty() || random.nextDouble() < groupCreationProb) {
                    groupId = "g" + groupIds.size();
                }
                else {
                    groupId = groupIds.get(random.nextInt(groupIds.size()));
                }
                newAgent = new ChattingToAGroupAgent(agentId, groupId);
                break;

            case 1: // generating a chatting to a friend agent
                String friendId = "a" + random.nextInt(chattingAgents.size());
                newAgent = new ChattingToAFriendAgent(agentId, friendId);
                break;

            case 2: // generating a broacasting agent
                newAgent = new BroadcastingAgent(agentId);
                break;

            case 3: // generating a SometimeDisposingMsgBoxBroadcastingAgent
                newAgent = new SometimeDisposingMsgBoxBroadcastingAgent(
                    agentId, random.nextLong(), msgBoxDisableEnalbeProb);
                break;
            }

            chattingAgents.add(newAgent);
            choice = random.nextInt(4);
        }

        System.out.println("Start system...");
        long tic = System.currentTimeMillis();
        // Executing cycles
        for (int i = 0; i < nbCycles; i++) {

            for (ChattingAgent agent : chattingAgents) {
                agent.readMessages();
            }
            for (ChattingAgent agent : chattingAgents) {
                agent.sendMessages();
            }
        }
        long toc = System.currentTimeMillis();
        System.out.println("Stop system...");

        int nbReceivedMsgs = 0;
        for (ChattingAgent agent : chattingAgents) {
            nbReceivedMsgs += agent.getNbReceivedMessages();
        }
        long nbMillis = toc - tic;

        System.out.println("Total received messages : " + nbReceivedMsgs);
        System.out.println("Total time : " + nbMillis / 1000. + " s");
        System.out.println("Speed : " + (nbMillis) / ((double) nbReceivedMsgs) * 1000 + " μs / message");

    }

    /**
     * The Class ChattingAgent.
     */
    private static abstract class ChattingAgent {

        /** The msg box. */
        protected IMsgBox<String> msgBox;
        
        /** The id. */
        protected final String    id;
        
        /** The nb received messages. */
        protected int             nbReceivedMessages;

        /**
         * Instantiates a new chatting agent.
         *
         * @param id the id
         */
        public ChattingAgent(String id) {
            this.id = id;
            this.nbReceivedMessages = 0;

            // get the message box
            this.msgBox = AgentMessaging.getMsgBox(id, String.class);
        }

        /**
         * Read messages.
         */
        public void readMessages() {
            List<String> receivedMessages = this.msgBox.getMsgs();
            this.nbReceivedMessages += receivedMessages.size();
        }

        /**
         * Gets the nb received messages.
         *
         * @return the nb received messages
         */
        public int getNbReceivedMessages() {
            return this.nbReceivedMessages;
        }

        /**
         * Send messages.
         */
        public abstract void sendMessages();
    }

    /**
     * The Class ChattingToAGroupAgent.
     */
    private static class ChattingToAGroupAgent extends ChattingAgent {

        /** The my group. */
        private final Ref<String> myGroup;

        /**
         * Instantiates a new chatting to a group agent.
         *
         * @param id the id
         * @param groupId the group id
         */
        public ChattingToAGroupAgent(String id, String groupId) {
            super(id);
            this.myGroup = this.msgBox.subscribeToGroup(groupId);
        }

        /* (non-Javadoc)
         * @see fr.irit.smac.libs.tooling.messaging.example.AdvancedMessagingExample.ChattingAgent#sendMessages()
         */
        @Override
        public void sendMessages() {
            this.msgBox.sendToGroup("Chatting to my group !", this.myGroup);
        }

    }

    /**
     * The Class BroadcastingAgent.
     */
    private static class BroadcastingAgent extends ChattingAgent {

        /**
         * Instantiates a new broadcasting agent.
         *
         * @param id the id
         */
        public BroadcastingAgent(String id) {
            super(id);
        }

        /* (non-Javadoc)
         * @see fr.irit.smac.libs.tooling.messaging.example.AdvancedMessagingExample.ChattingAgent#sendMessages()
         */
        @Override
        public void sendMessages() {
            this.msgBox.broadcast("I'm joyfully broadcasting !");
        }

    }

    /**
     * The Class ChattingToAFriendAgent.
     */
    private static class ChattingToAFriendAgent extends ChattingAgent {

        /** The my friend id. */
        private final String myFriendId;
        
        /** The my friend. */
        private Ref<String>  myFriend;

        /**
         * Instantiates a new chatting to a friend agent.
         *
         * @param id the id
         * @param myFriendId the my friend id
         */
        public ChattingToAFriendAgent(String id, String myFriendId) {
            super(id);
            this.myFriendId = myFriendId;
        }

        /* (non-Javadoc)
         * @see fr.irit.smac.libs.tooling.messaging.example.AdvancedMessagingExample.ChattingAgent#sendMessages()
         */
        @Override
        public void sendMessages() {
            if (this.myFriend == null) {
                this.myFriend = this.msgBox.getDirectory().getAgentRef(this.myFriendId);
            }
            if (this.myFriend != null) { // if my friend hasn't disposed it's
                                         // msgBox
                this.msgBox.send("I love you my friend !", this.myFriend);
            }
        }
    }

    /**
     * The Class SometimeDisposingMsgBoxBroadcastingAgent.
     */
    private static class SometimeDisposingMsgBoxBroadcastingAgent extends BroadcastingAgent {

        /** The random. */
        private final Random random;
        
        /** The switching prob. */
        private final double switchingProb;
        
        /** The enabled. */
        private boolean      enabled;

        /**
         * Instantiates a new sometime disposing msg box broadcasting agent.
         *
         * @param id the id
         * @param randomSeed the random seed
         * @param switchingProb the switching prob
         */
        public SometimeDisposingMsgBoxBroadcastingAgent(String id, long randomSeed, double switchingProb) {
            super(id);
            this.enabled = true;
            this.random = new Random(randomSeed);
            this.switchingProb = switchingProb;
        }

        /**
         * Switch msg box.
         */
        private void switchMsgBox() {
            if (this.enabled) {
                this.msgBox.dispose();
            }
            else {
                this.msgBox = AgentMessaging.getMsgBox(this.id, String.class);
            }
            this.enabled = !this.enabled;
        }

        /* (non-Javadoc)
         * @see fr.irit.smac.libs.tooling.messaging.example.AdvancedMessagingExample.ChattingAgent#readMessages()
         */
        @Override
        public void readMessages() {
            if (this.random.nextDouble() < this.switchingProb) {
                this.switchMsgBox();
            }
            if (this.enabled) {
                super.readMessages();
            }
        }

    }

}
