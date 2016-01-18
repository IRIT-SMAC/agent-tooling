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

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSink;

/**
 * A MsgSink that is used to broadcast a message to a collection of agent Ref
 * This implementation is theorically thread safe.
 *
 * @author lemouzy
 * @param <T> the generic type
 */
class GroupMsgBox<T> extends Ref<T> implements IMsgSink<T> {

    /** The agent refs. */
    private final Set<Ref<T>> agentRefs;

    /**
     * Instantiates a new group msg box.
     *
     * @param groupId the group id
     */
    public GroupMsgBox(String groupId) {
        super(groupId);
        this.agentRefs = new ConcurrentSkipListSet<Ref<T>>();
    }

    /**
     * Subscribe agent.
     *
     * @param agentRef the agent ref
     */
    public void subscribeAgent(Ref<T> agentRef) {
        this.agentRefs.add(agentRef);
    }

    /**
     * Unsubscribe agent.
     *
     * @param agentRef the agent ref
     */
    public void unsubscribeAgent(Ref<T> agentRef) {
        this.agentRefs.remove(agentRef);
    }

    /**
     * Gets the agents.
     *
     * @return the agents
     */
    public Set<Ref<T>> getAgents() {
        return this.agentRefs;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.impl.Ref#getMsgSink()
     */
    @Override
    IMsgSink<T> getMsgSink() {
        return this;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSink#putMsg(java.lang.Object)
     */
    @Override
    public boolean putMsg(T msg) {
        boolean sentToAll = true;

        for (Ref<T> ref : agentRefs) {

            sentToAll = ref.getMsgSink().putMsg(msg) && sentToAll;
        }

        return sentToAll;
    }

}
