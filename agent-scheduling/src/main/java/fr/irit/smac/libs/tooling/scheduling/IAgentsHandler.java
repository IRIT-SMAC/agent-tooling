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
package fr.irit.smac.libs.tooling.scheduling;

import java.util.Collection;

/**
 * 
 * This interface exposes methods to add or remove agents from the system.
 * <p>
 * An extremely important guarantee that every implementation should follow is
 * that an agent is never executed multiple times in parallel.
 * <p>
 * The two basic recommendations which should be followed by the implementations
 * are:
 * <p>
 * When adding one or several agents to the system, if this or these agents are
 * already added to the system nothing should happens.
 * <p>
 * When removing one or several agents from the system, if this or these agents
 * are not in the system nothing should happens.
 * 
 * @author jorquera
 * 
 * @param <Agent>
 *            the type of agents to be handled by the system
 */
public interface IAgentsHandler<Agent> {

	/**
	 * 
	 * @return a collection containing the agents currently handled by the
	 *         system
	 */
	Collection<Agent> getAgents();

	/**
	 * Add an agent to the system
	 * 
	 * @param agent
	 *            the agent to be added to the system
	 */
	void addAgent(Agent agent);

	/**
	 * Remove an agent from the system
	 * 
	 * @param agent
	 *            the agent to be removed from the system
	 */
	void removeAgent(Agent agent);

	/**
	 * Add a collection of agents to the system
	 * 
	 * @param agents
	 *            the agents to be added to the system
	 */
	void addAgents(Collection<Agent> agents);

	/**
	 * Remove a collection of agents from the system
	 * 
	 * @param agents
	 *            the agents to be removed
	 */
	void removeAgents(Collection<Agent> agents);

}
