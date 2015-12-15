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

public class AdvancedMessagingExample {

	public static final void main(String[] args) {
		
		long randomSeed = 1234l;
		int nbAgents = 100;
		int nbCycles = 1000;
		double groupCreationProb = .2;
		double msgBoxDisableEnalbeProb = .2;
		
		Random random = new Random(randomSeed);
				
		// creation of agents
		List<ChattingAgent> chattingAgents = new ArrayList<ChattingAgent>();
		List<String> groupIds = new ArrayList<String>();
		
		int choice = 0; // in order to generate a first deterministic type of agent
		System.out.println("Creation of agents...");
		for (int i = 0 ; i < nbAgents ; i++) {
			
			ChattingAgent newAgent = null;
			String agentId = "a"+i; 
					
			switch(choice){
			case 0:	// generating a chatting to a group agent
				String groupId = null;
				if (groupIds.isEmpty() || random.nextDouble() < groupCreationProb) {
					groupId = "g"+groupIds.size();
				} else {
					groupId = groupIds.get(random.nextInt(groupIds.size()));
				}
				newAgent = new ChattingToAGroupAgent(agentId, groupId);
				break;
				
			case 1: // generating a chatting to a friend agent
				String friendId = "a"+random.nextInt(chattingAgents.size());
				newAgent = new ChattingToAFriendAgent(agentId, friendId);
				break;
				
			case 2: // generating a broacasting agent
				newAgent = new BroadcastingAgent(agentId);
				break;
			
			case 3: // generating a SometimeDisposingMsgBoxBroadcastingAgent
				newAgent = new SometimeDisposingMsgBoxBroadcastingAgent(
									agentId, random.nextLong(), msgBoxDisableEnalbeProb
						   );
				break;
			}
			
			chattingAgents.add(newAgent);
			choice = random.nextInt(4);
		}
		
		System.out.println("Start system...");
		long tic = System.currentTimeMillis();
		// Executing cycles
		for (int i = 0 ; i < nbCycles ; i++) {
			
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
		System.out.println("Total time : " + nbMillis/1000. + " s");
		System.out.println("Speed : "+ (nbMillis) / ((double)nbReceivedMsgs) * 1000 + " μs / message");
		
	}
	
	private static abstract class ChattingAgent {
		
		protected IMsgBox<String> msgBox;
		protected final String id;
		protected int nbReceivedMessages;
		
		public ChattingAgent(String id) {
			this.id = id;
			this.nbReceivedMessages = 0;

			// get the message box
			this.msgBox = AgentMessaging.getMsgBox(id, String.class);
		}
		
		public void readMessages() {
			List<String> receivedMessages = this.msgBox.getMsgs();
			this.nbReceivedMessages += receivedMessages.size();
		}
		
		public int getNbReceivedMessages() {
			return this.nbReceivedMessages;
		}
		
		public abstract void sendMessages();
	}
	
	
	private static class ChattingToAGroupAgent  extends ChattingAgent {

		private final Ref<String> myGroup;
		
		public ChattingToAGroupAgent(String id, String groupId) {
			super(id);
			this.myGroup = this.msgBox.subscribeToGroup(groupId);
		}

		@Override
		public void sendMessages() {
			this.msgBox.sendToGroup("Chatting to my group !", this.myGroup);
		}
		
	}	
	
	private static class BroadcastingAgent  extends ChattingAgent {

		public BroadcastingAgent(String id) {
			super(id);
		}

		@Override
		public void sendMessages() {
			this.msgBox.broadcast("I'm joyfully broadcasting !");
		}
		
	}
	
	private static class ChattingToAFriendAgent  extends ChattingAgent {

		private final String myFriendId;
		private Ref<String> myFriend;
		
		public ChattingToAFriendAgent(String id, String myFriendId) {
			super(id);
			this.myFriendId = myFriendId;
		}

		@Override
		public void sendMessages() {
			if (this.myFriend == null) {
				this.myFriend = this.msgBox.getDirectory().getAgentRef(this.myFriendId);
			}
			if (this.myFriend != null) { // if my friend hasn't disposed it's msgBox
				this.msgBox.send("I love you my friend !", this.myFriend);
			}
		}
	}
	
	private static class SometimeDisposingMsgBoxBroadcastingAgent  extends BroadcastingAgent {

		private final Random random;
		private final double switchingProb;
		private boolean enabled;
		
		public SometimeDisposingMsgBoxBroadcastingAgent(String id, long randomSeed, double switchingProb) {
			super(id);
			this.enabled = true;
			this.random = new Random(randomSeed);
			this.switchingProb = switchingProb;
		}

		private void switchMsgBox() {
			if (this.enabled) {
				this.msgBox.dispose();
			} else {
				this.msgBox = AgentMessaging.getMsgBox(this.id, String.class);
			}
			this.enabled = !this.enabled;
		}

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
