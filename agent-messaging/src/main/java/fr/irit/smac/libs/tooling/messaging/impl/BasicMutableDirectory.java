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
 * @param <T>
 */
public class BasicMutableDirectory<T> implements
    IMutableDirectory<T> {

    private final Map<String, AgentMsgBox<T>> agentDirectory;
    private final Map<String, GroupMsgBox<T>> groupDirectory;

    private final ReentrantLock                     groupModificationLock;
    private final ReentrantLock                     agentModificationLock;

    private final GroupMsgBox<T>              broadCastMsgBox;

    public BasicMutableDirectory() {
        super();
        this.agentDirectory = new ConcurrentHashMap<String, AgentMsgBox<T>>();
        this.groupDirectory = new ConcurrentHashMap<String, GroupMsgBox<T>>();

        this.groupModificationLock = new ReentrantLock();
        this.agentModificationLock = new ReentrantLock();

        this.broadCastMsgBox = this.getOrCreateGroupMsgBox(IDirectory.ALL);
    }

    // ///////////////////////////////////////////////////////////////////////////////
    // Agent - Group Creation / Deletition
    // ///////////////////////////////////////////////////////////////////////////////

    @Override
    public IMsgBox<T> createAgentMsgBox(String agentId) {

        AgentMsgBox<T> agentRef;
        this.agentModificationLock.lock();

        if (!this.agentDirectory.containsKey(agentId)) {
            agentRef = new AgentMsgBox<T>(agentId, this);
            this.subscribeAgentToGroup(agentRef, broadCastMsgBox);
            this.agentDirectory.put(agentId, agentRef);
        }
        else {
            this.agentModificationLock.unlock();
            throw new IllegalArgumentException("The agent " + agentId
                + " is already associated with a msgBox.");
        }

        this.agentModificationLock.unlock();

        return agentRef;
    }

    @Override
    public void removeAgentMsgBox(Ref<T> agentRef) {
        this.agentModificationLock.lock();
        this.groupModificationLock.lock();
        this.agentDirectory.remove(agentRef.getId());

        for (GroupMsgBox<T> group : groupDirectory.values()) {
            this.unsubscribeAgentFromGroup(agentRef, group);
        }

        this.groupModificationLock.lock();
        this.agentModificationLock.unlock();
    }

    private GroupMsgBox<T> getOrCreateGroupMsgBox(String groupId) {
        GroupMsgBox<T> groupRef;

        this.groupModificationLock.lock();

        if (this.groupDirectory.containsKey(groupId)) {
            groupRef = this.groupDirectory.get(groupId);
        }
        else {
            groupRef = new GroupMsgBox<T>(groupId);
            this.groupDirectory.put(groupId, groupRef);
        }

        this.groupModificationLock.unlock();

        return groupRef;
    }

    // ///////////////////////////////////////////////////////////////////////////////
    // Agent access
    // ///////////////////////////////////////////////////////////////////////////////

    @Override
    public AgentMsgBox<T> getAgentRef(String agentId) {
        return this.agentDirectory.get(agentId);
    }

    @Override
    public List<Ref<T>> getAgentsRef() {
        return new ArrayList<Ref<T>>(agentDirectory.values());
    }

    // ///////////////////////////////////////////////////////////////////////////////
    // Group access
    // ///////////////////////////////////////////////////////////////////////////////

    @Override
    public List<Ref<T>> getGroupsRef() {
        return new ArrayList<Ref<T>>(groupDirectory.values());
    }

    @Override
    public GroupMsgBox<T> getGroupRef(String groupId) {
        return this.groupDirectory.get(groupId);
    }

    @Override
    public Set<Ref<T>> getAgentsOfGroup(Ref<T> groupRef) {
        return this.getAgentsOfGroup(groupRef.getId());
    }

    @Override
    public Set<Ref<T>> getAgentsOfGroup(String groupId) {
        return this.groupDirectory.get(groupId).getAgents();
    }

    // ///////////////////////////////////////////////////////////////////////////////
    // Subscription concerns
    // ///////////////////////////////////////////////////////////////////////////////

    @Override
    public Ref<T> subscribeAgentToGroup(Ref<T> agentRef,
        String groupId) {
        GroupMsgBox<T> groupRef = this.getOrCreateGroupMsgBox(groupId);
        this.subscribeAgentToGroup(agentRef, groupRef);
        return groupRef;
    }

    @Override
    public void subscribeAgentToGroup(Ref<T> agentRef,
        Ref<T> groupId) {
        this.groupModificationLock.lock();
        if (this.groupDirectory.containsValue(groupId)) {
            ((GroupMsgBox<T>) groupId).subscribeAgent(agentRef);
        }
        else {
            this.groupModificationLock.unlock();
            throw new IllegalArgumentException(
                "Trying to subscribe an agent to an unknown group : "
                    + groupId);
        }

        this.groupModificationLock.unlock();
    }

    @Override
    public void unsubscribeAgentFromGroup(Ref<T> agentRef,
        Ref<T> groupRef) {
        this.groupModificationLock.lock();

        if (this.groupDirectory.containsValue(groupRef)) {
            ((GroupMsgBox<T>) groupRef).unsubscribeAgent(agentRef);
        }
        else {
            this.groupModificationLock.unlock();
            throw new IllegalArgumentException(
                "Trying to unsubscribe an agent from an unknown group : "
                    + groupRef);
        }

        this.groupModificationLock.unlock();
    }

    @Override
    public void unsubscribeAgentFromGroup(Ref<T> agentRef, String groupId) {
        this.groupModificationLock.lock();

        GroupMsgBox<T> groupMsgBox = this.groupDirectory.get(groupId);
        if (groupMsgBox != null) {
            groupMsgBox.unsubscribeAgent(agentRef);
        }
        else {
            this.groupModificationLock.unlock();
            throw new IllegalArgumentException(
                "Trying to unsubscribe an agent from an unknown group : "
                    + groupId);
        }

        this.groupModificationLock.unlock();
    }

}
