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

import fr.irit.smac.libs.tooling.messaging.IDirectory;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;

/**
 * Interface of a directory that can be modified, and allow add/remove of msgBox
 * and groups.
 *
 * @author lemouzy
 * @param <T> the generic type
 */
interface IMutableDirectory<T> extends IDirectory<T> {

    /**
     * Subscribe an agent to a group.
     * 
     * The group is created if it does not exist
     * 
     * @param agentRef the agent ref
     * @param groupId the group id
     * @return the ref
     */
    public Ref<T> subscribeAgentToGroup(Ref<T> agentRef,
        String groupId);

    /**
     * Subscribe an agent to a group.
     *
     * @param agentRef the agent ref
     * @param groupRef the group ref
     */
    public void subscribeAgentToGroup(Ref<T> agentRef,
        Ref<T> groupRef);

    /**
     * Unsubscribe an agent from a group.
     *
     * @param agentRef the agent ref
     * @param groupId the group id
     */
    public void unsubscribeAgentFromGroup(Ref<T> agentRef, String groupId);

    /**
     * Unsubscribe an agent from a group.
     *
     * @param agentRef the agent ref
     * @param groupRef the group ref
     */
    public void unsubscribeAgentFromGroup(Ref<T> agentRef,
        Ref<T> groupRef);

    /**
     * Creates the agent msg box.
     *
     * @param agentId the agent id
     * @return the i msg box
     */
    public IMsgBox<T> createAgentMsgBox(String agentId);

    /**
     * Removes the agent msg box.
     *
     * @param agentRef the agent ref
     */
    public void removeAgentMsgBox(Ref<T> agentRef);
}
