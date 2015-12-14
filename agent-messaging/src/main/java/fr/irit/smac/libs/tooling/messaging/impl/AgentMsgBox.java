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

import java.util.List;

import fr.irit.smac.libs.tooling.messaging.IDirectory;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.BlockingQueueMsgContainer;
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.DummyMsgContainer;
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgContainer;
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSink;

/**
 * Base implementation of the MsgBox used by agents
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
class AgentMsgBox<MsgType> extends Ref<MsgType> implements IMsgBox<MsgType> {

	private final static DummyMsgContainer dummyMsgContainer = new DummyMsgContainer();

	private final IMutableDirectory<MsgType> directory;
	private volatile IMsgContainer<MsgType> msgContainer;
	private final BasicSender<MsgType> sender;

	public AgentMsgBox(String agentId, IMutableDirectory<MsgType> directory) {
		super(agentId);
		this.directory = directory;
		this.msgContainer = new BlockingQueueMsgContainer<MsgType>();
		this.sender = new BasicSender<MsgType>(this.directory);
	}

	@Override
	public Ref<MsgType> getRef() {
		return this;
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Directory Concerns
	// /////////////////////////////////////////////////////////////////////////////

	@Override
	public IDirectory<MsgType> getDirectory() {
		return this.directory;
	}

	@Override
	public Ref<MsgType> subscribeToGroup(String groupId) {
		return this.directory.subscribeAgentToGroup(this, groupId);
	}

	@Override
	public void subscribeToGroup(Ref<MsgType> groupRef) {
		this.directory.subscribeAgentToGroup(this, groupRef);
	}

	@Override
	public void unsubscribeToGroup(String groupId) {
		this.directory.unsubscribeAgentFromGroup(this, groupId);
	}

	@Override
	public void unsubscribeToGroup(Ref<MsgType> groupRef) {
		this.directory.unsubscribeAgentFromGroup(this, groupRef);
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Sending concerns
	// /////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean send(MsgType msg, String receiverId) {
		return this.sender.send(msg, receiverId);
	}

	@Override
	public boolean send(MsgType msg, Ref<MsgType> receiverRef) {
		return this.sender.send(msg, receiverRef);
	}

	@Override
	public boolean sendToGroup(MsgType msg, String groupId) {
		return this.sender.sendToGroup(msg, groupId);
	}

	@Override
	public boolean sendToGroup(MsgType msg, Ref<MsgType> groupRef) {
		return this.sender.sendToGroup(msg, groupRef);
	}

	@Override
	public boolean broadcast(MsgType msg) {
		return this.sender.broadcast(msg);
	}

	// /////////////////////////////////////////////////////////////////////////////
	// MsgContainer Concerns
	// /////////////////////////////////////////////////////////////////////////////

	@Override
	public List<MsgType> getMsgs() {
		return this.msgContainer.getMsgs();
	}

	@Override
	IMsgSink<MsgType> getMsgSink() {
		return this.msgContainer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispose() {
		this.directory.removeAgentMsgBox(this);
		this.msgContainer = (IMsgContainer<MsgType>) AgentMsgBox.dummyMsgContainer;
	}

}
