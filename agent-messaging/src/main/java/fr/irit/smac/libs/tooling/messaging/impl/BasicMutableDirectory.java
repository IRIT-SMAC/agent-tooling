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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import fr.irit.smac.libs.tooling.messaging.IDirectory;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;

/**
 * Basic implementation of the msgBox directory.
 * 
 * Manages msgBox and Groups. Theoretically this implementation is thread safe.
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
public class BasicMutableDirectory<MsgType> implements
		IMutableDirectory<MsgType> {

	private final Map<String, AgentMsgBox<MsgType>> agentDirectory;
	private final Map<String, GroupMsgBox<MsgType>> groupDirectory;

	private final ReentrantLock groupModificationLock;
	private final ReentrantLock agentModificationLock;

	private final GroupMsgBox<MsgType> broadCastMsgBox;

	public BasicMutableDirectory() {
		super();
		this.agentDirectory = new ConcurrentHashMap<String, AgentMsgBox<MsgType>>();
		this.groupDirectory = new ConcurrentHashMap<String, GroupMsgBox<MsgType>>();

		this.groupModificationLock = new ReentrantLock();
		this.agentModificationLock = new ReentrantLock();

		this.broadCastMsgBox = this.getOrCreateGroupMsgBox(IDirectory.ALL);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Agent - Group Creation / Deletition
	// ///////////////////////////////////////////////////////////////////////////////

	@Override
	public IMsgBox<MsgType> createAgentMsgBox(String agentId) {

		AgentMsgBox<MsgType> agentRef;
		this.agentModificationLock.lock();

		if (!this.agentDirectory.containsKey(agentId)) {
			agentRef = new AgentMsgBox<MsgType>(agentId, this);
			this.subscribeAgentToGroup(agentRef, broadCastMsgBox);
			this.agentDirectory.put(agentId, agentRef);
		} else {
			this.agentModificationLock.unlock();
			throw new IllegalArgumentException("The agent " + agentId
					+ " is already associated with a msgBox.");
		}

		this.agentModificationLock.unlock();

		return agentRef;
	}

	@Override
	public void removeAgentMsgBox(Ref<MsgType> agentRef) {
		this.agentModificationLock.lock();
		this.groupModificationLock.lock();
		this.agentDirectory.remove(agentRef.getId());

		for (GroupMsgBox<MsgType> group : groupDirectory.values()) {
			this.unsubscribeAgentFromGroup(agentRef, group);
		}

		this.groupModificationLock.lock();
		this.agentModificationLock.unlock();
	}

	private GroupMsgBox<MsgType> getOrCreateGroupMsgBox(String groupId) {
		GroupMsgBox<MsgType> groupRef;

		this.groupModificationLock.lock();

		if (this.groupDirectory.containsKey(groupId)) {
			groupRef = this.groupDirectory.get(groupId);
		} else {
			groupRef = new GroupMsgBox<MsgType>(groupId);
			this.groupDirectory.put(groupId, groupRef);
		}

		this.groupModificationLock.unlock();

		return groupRef;
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Agent access
	// ///////////////////////////////////////////////////////////////////////////////

	@Override
	public AgentMsgBox<MsgType> getAgentRef(String agentId) {
		return this.agentDirectory.get(agentId);
	}

	@Override
	public List<Ref<MsgType>> getAgentsRef() {
		return new ArrayList<Ref<MsgType>>(agentDirectory.values());
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Group access
	// ///////////////////////////////////////////////////////////////////////////////

	@Override
	public List<Ref<MsgType>> getGroupsRef() {
		return new ArrayList<Ref<MsgType>>(groupDirectory.values());
	}

	@Override
	public GroupMsgBox<MsgType> getGroupRef(String groupId) {
		return this.groupDirectory.get(groupId);
	}

	@Override
	public Set<Ref<MsgType>> getAgentsOfGroup(Ref<MsgType> groupRef) {
		return this.getAgentsOfGroup(groupRef.getId());
	}

	@Override
	public Set<Ref<MsgType>> getAgentsOfGroup(String groupId) {
		return this.groupDirectory.get(groupId).getAgents();
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// Subscription concerns
	// ///////////////////////////////////////////////////////////////////////////////

	@Override
	public Ref<MsgType> subscribeAgentToGroup(Ref<MsgType> agentRef,
			String groupId) {
		GroupMsgBox<MsgType> groupRef = this.getOrCreateGroupMsgBox(groupId);
		this.subscribeAgentToGroup(agentRef, groupRef);
		return groupRef;
	}

	@Override
	public void subscribeAgentToGroup(Ref<MsgType> agentRef,
			Ref<MsgType> groupId) {
		this.groupModificationLock.lock();
		if (this.groupDirectory.containsValue(groupId)) {
			((GroupMsgBox<MsgType>) groupId).subscribeAgent(agentRef);
		} else {
			this.groupModificationLock.unlock();
			throw new IllegalArgumentException(
					"Trying to subscribe an agent to an unknown group : "
							+ groupId);
		}

		this.groupModificationLock.unlock();
	}

	@Override
	public void unsubscribeAgentFromGroup(Ref<MsgType> agentRef,
			Ref<MsgType> groupRef) {
		this.groupModificationLock.lock();

		if (this.groupDirectory.containsValue(groupRef)) {
			((GroupMsgBox<MsgType>) groupRef).unsubscribeAgent(agentRef);
		} else {
			this.groupModificationLock.unlock();
			throw new IllegalArgumentException(
					"Trying to unsubscribe an agent from an unknown group : "
							+ groupRef);
		}

		this.groupModificationLock.unlock();
	}

	@Override
	public void unsubscribeAgentFromGroup(Ref<MsgType> agentRef, String groupId) {
		this.groupModificationLock.lock();

		GroupMsgBox<MsgType> groupMsgBox = this.groupDirectory.get(groupId);
		if (groupMsgBox != null) {
			groupMsgBox.unsubscribeAgent(agentRef);
		} else {
			this.groupModificationLock.unlock();
			throw new IllegalArgumentException(
					"Trying to unsubscribe an agent from an unknown group : "
							+ groupId);
		}

		this.groupModificationLock.unlock();
	}

}
