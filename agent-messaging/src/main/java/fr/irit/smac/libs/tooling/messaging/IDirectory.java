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
package fr.irit.smac.libs.tooling.messaging;

import java.util.List;
import java.util.Set;

import fr.irit.smac.libs.tooling.messaging.impl.Ref;

/**
 * A directory that permits : - to link agentIds to their refs. - to link
 * groupIds to their refs. - to access the agents refs belonging to a group.
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
public interface IDirectory<MsgType> {

	/**
	 * Name of the group that will contain all the agents (used for broadcast).
	 */
	public final static String ALL = "all";

	/**
	 * Returns the agent ref from the given agent id
	 * 
	 * @param agentId
	 * @return
	 */
	public Ref<MsgType> getAgentRef(String agentId);

	/**
	 * Returns the group ref from the given agent id
	 * 
	 * @param agentId
	 * @return
	 */
	public Ref<MsgType> getGroupRef(String groupId);

	/**
	 * Returns all the agents inscribed in a group
	 * 
	 * @param groupRef
	 * @return
	 */
	public Set<Ref<MsgType>> getAgentsOfGroup(Ref<MsgType> groupRef);

	/**
	 * Returns all the agents inscribed in a group
	 * 
	 * @param groupRef
	 * @return
	 */
	public Set<Ref<MsgType>> getAgentsOfGroup(String groupId);

	/**
	 * Returns all the agent refs inscribed in the directory
	 * 
	 * @return
	 */
	public List<Ref<MsgType>> getGroupsRef();

	/**
	 * Returns all the groups refs inscribed in the directory
	 * 
	 * @return
	 */
	public List<Ref<MsgType>> getAgentsRef();
}
