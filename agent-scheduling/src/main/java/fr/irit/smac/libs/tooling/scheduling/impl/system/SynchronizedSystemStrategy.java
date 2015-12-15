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
package fr.irit.smac.libs.tooling.scheduling.impl.system;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy;

/**
 * TODO: document
 * 
 * @author jorquera
 * 
 */
public class SynchronizedSystemStrategy extends
		AbstractSystemStrategy<IAgentStrategy> {

	public SynchronizedSystemStrategy(Collection<IAgentStrategy> agents,
			ExecutorService agentExecutor) {
		super(agentExecutor);

		this.addAgents(agents);
	}

	public SynchronizedSystemStrategy(Collection<IAgentStrategy> agents) {
		this(agents, Executors.newFixedThreadPool(1));
	}

	private final Map<IAgentStrategy, Callable<?>> agentsCallables = Collections
			.synchronizedMap(new HashMap<IAgentStrategy, Callable<?>>());

	@Override
	protected void doStep() {
		Set<Future<?>> executionResults = new HashSet<Future<?>>();

		// start the execution of the agents
		for (Callable<?> c : agentsCallables.values()) {
			executionResults.add(agentExecutor.submit(c));
		}

		// wait for all agents to finish
		for (Future<?> f : executionResults) {
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addAgent(final IAgentStrategy agent) {
		if (!this.agents.contains(agent)) {
			super.addAgent(agent);
			this.agentsCallables.put(agent, new Callable<Object>() {

				@Override
				public Object call() throws Exception {
					agent.nextStep();
					return this;
				}

			});
		}
	}

	@Override
	public void removeAgent(IAgentStrategy agent) {
		if (this.agents.contains(agent)) {
			super.removeAgent(agent);
			this.agentsCallables.remove(agent);
		}
	}

}
