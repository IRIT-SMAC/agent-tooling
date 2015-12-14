/*
 * #%L
 * agent-tooling
 * %%
 * Copyright (C) 2014 IRIT - SMAC Team
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
package fr.irit.smac.libs.tooling.messaging.impl;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSink;

/**
 * A MsgSink that is used to broadcast a message to a collection of agent Ref
 * This implementation is theorically thread safe
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
class GroupMsgBox<MsgType> extends Ref<MsgType> implements IMsgSink<MsgType> {

	private final Set<Ref<MsgType>> agentRefs;

	public GroupMsgBox(String groupId) {
		super(groupId);
		this.agentRefs = new ConcurrentSkipListSet<Ref<MsgType>>();
	}

	public void subscribeAgent(Ref<MsgType> agentRef) {
		this.agentRefs.add(agentRef);
	}

	public void unsubscribeAgent(Ref<MsgType> agentRef) {
		this.agentRefs.remove(agentRef);
	}

	public Set<Ref<MsgType>> getAgents() {
		return this.agentRefs;
	}

	@Override
	IMsgSink<MsgType> getMsgSink() {
		return this;
	}

	@Override
	public boolean putMsg(MsgType msg) {
		boolean sentToAll = true;

		for (Ref<MsgType> ref : agentRefs) {

			sentToAll = ref.getMsgSink().putMsg(msg) && sentToAll;
		}

		return sentToAll;
	}

}
