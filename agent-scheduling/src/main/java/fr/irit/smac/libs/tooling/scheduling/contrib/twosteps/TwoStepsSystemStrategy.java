/*
 * #%L
 * agent-scheduling
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
package fr.irit.smac.libs.tooling.scheduling.contrib.twosteps;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy;
import fr.irit.smac.libs.tooling.scheduling.impl.system.AbstractSystemStrategy;
import fr.irit.smac.libs.tooling.scheduling.impl.system.SynchronizedSystemStrategy;

/**
 * 
 * TODO: document
 * 
 * @author jorquera
 * 
 */
public class TwoStepsSystemStrategy extends
		AbstractSystemStrategy<ITwoStepsAgent> {

	private static enum STATE {
		PERCEIVE, DECIDE_ACT
	};

	private volatile STATE currentState = STATE.PERCEIVE;

	protected class AgentWrapper implements IAgentStrategy {

		private final ITwoStepsAgent agent;

		public AgentWrapper(ITwoStepsAgent agent) {
			this.agent = agent;
		}

		@Override
		public void nextStep() {

			switch (TwoStepsSystemStrategy.this.currentState) {
			case PERCEIVE:
				agent.perceive();
				break;

			case DECIDE_ACT:
				agent.decideAndAct();
				break;

			default:
				throw new RuntimeException("case not covered");
			}

		}

	}

	private final Map<ITwoStepsAgent, AgentWrapper> agentWrappers = new ConcurrentHashMap<ITwoStepsAgent, AgentWrapper>();

	// NOTE: BlockingQueue implem is thread-safe
	private final BlockingQueue<ITwoStepsAgent> pendingAddedAgents = new LinkedBlockingDeque<ITwoStepsAgent>();
	private final BlockingQueue<ITwoStepsAgent> pendingRemovedAgents = new LinkedBlockingDeque<ITwoStepsAgent>();

	private final AbstractSystemStrategy<IAgentStrategy> internalSystemStrategy;

	public TwoStepsSystemStrategy(Collection<ITwoStepsAgent> agents,
			ExecutorService agentExecutor) {
		super(agentExecutor);

		internalSystemStrategy = new SynchronizedSystemStrategy(
				new LinkedHashSet<IAgentStrategy>(), agentExecutor);
		this.addAgents(agents);

		// replace inherited shutdownRunnable by its own
		final Runnable inheritedShutdownRunnable = shutdownRunnable;
		shutdownRunnable = new Runnable() {

			@Override
			public void run() {
				// shutdown internalSystemStrategy
				//
				// NOTE: since internalSystemStrategy is only accessed by
				// ourself on a step-by-step basis, it is guaranteed that its
				// execution queue will be empty by the time we reach
				// shutdown(). Meaning that no lingering agent execution task
				// will be present in its queue.
				// Consequently we do not need to block to satisfy the interface
				// contract that now agent will be executed after shutdown() has
				// returned.
				internalSystemStrategy.shutdown();

				// propagate to inherited shutdown task
				inheritedShutdownRunnable.run();

			}
		};
	}

	public TwoStepsSystemStrategy(Collection<ITwoStepsAgent> agents) {
		// setting a "reasonable" default size for the thread pool of 2xNbCores
		// (according to e.g.
		// http://codeidol.com/java/java-concurrency/Applying-Thread-Pools/Sizing-Thread-Pools/)
		// user should probably override it to a more suitable value
		this(agents, Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * 2));

	}

	private void addPendingAgents() {

		Collection<ITwoStepsAgent> drainedAgents = new HashSet<ITwoStepsAgent>();
		pendingAddedAgents.drainTo(drainedAgents);

		for (ITwoStepsAgent agent : drainedAgents) {
			AgentWrapper wrapper = new AgentWrapper(agent);
			agentWrappers.put(agent, wrapper);
			internalSystemStrategy.addAgent(wrapper);
		}

	}

	private void removePendingAgents() {
		Collection<ITwoStepsAgent> drainedAgents = new HashSet<ITwoStepsAgent>();
		pendingRemovedAgents.drainTo(drainedAgents);

		for (ITwoStepsAgent agent : drainedAgents) {
			internalSystemStrategy.removeAgent(agentWrappers.get(agent));
			agentWrappers.remove(agent);

		}
	}

	@Override
	protected void doStep() {

		addPendingAgents();

		// start the perceive of the agents and block
		currentState = STATE.PERCEIVE;
		try {
			internalSystemStrategy.step().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		// start the decide-and-act of the agents
		currentState = STATE.DECIDE_ACT;
		try {
			internalSystemStrategy.step().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		removePendingAgents();

	}

	/*** Executor Handling ***/

	@Override
	public ExecutorService getExecutorService() {
		return internalSystemStrategy.getExecutorService();
	}

	@Override
	public void setExecutorService(ExecutorService executor) {
		internalSystemStrategy.setExecutorService(executor);
	}

	/*** Agent Handling ***/

	@Override
	public void addAgent(final ITwoStepsAgent agent) {
		if (!this.agents.contains(agent)) {
			super.addAgent(agent);
			pendingAddedAgents.add(agent);
		}
	}

	@Override
	public void removeAgent(ITwoStepsAgent agent) {
		if (this.agents.contains(agent)) {
			super.removeAgent(agent);
			pendingRemovedAgents.add(agent);

		}
	}

}
