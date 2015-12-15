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
package fr.irit.smac.libs.tooling.scheduling.impl.system;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.irit.smac.libs.tooling.scheduling.IAgentsHandler;
import fr.irit.smac.libs.tooling.scheduling.IExecutorServiceHandler;
import fr.irit.smac.libs.tooling.scheduling.IHookHandler;
import fr.irit.smac.libs.tooling.scheduling.ISystemControlHandler;

/**
 * 
 * TODO: Document (important)
 * 
 * @author jorquera
 * 
 * @param <AgentType>
 *            the type of agents handled by the strategy
 */
public abstract class AbstractSystemStrategy<AgentType> implements
		ISystemControlHandler, IAgentsHandler<AgentType>,
		IExecutorServiceHandler, IHookHandler {

	protected final Set<AgentType> agents = Collections
			.synchronizedSet(new HashSet<AgentType>());

	protected final Set<Runnable> preStepHooks = Collections
			.synchronizedSet(new LinkedHashSet<Runnable>());

	protected final Set<Runnable> postStepHooks = Collections
			.synchronizedSet(new LinkedHashSet<Runnable>());

	protected volatile ExecutorService agentExecutor;
	protected final ExecutorService systemExecutor = Executors
			.newFixedThreadPool(1);

	private volatile Boolean doRun = false;
	private volatile long delay = 0L;

	public AbstractSystemStrategy(ExecutorService agentExecutor) {
		this.agentExecutor = agentExecutor;
	}

	/**
	 * TODO: comment (very important)
	 */
	abstract protected void doStep();

	protected void preStep() {
		synchronized (preStepHooks) {
			for (Runnable hook : preStepHooks) {
				hook.run();
			}
		}
	}

	protected void postStep() {
		synchronized (postStepHooks) {
			for (Runnable hook : postStepHooks) {
				hook.run();
			}
		}
	}

	protected void stepAndHooks() {

		preStep();
		doStep();
		postStep();

	}

	protected Runnable loopingRunnable = new Runnable() {
		@Override
		public void run() {
			if (doRun) {

				stepAndHooks();

				// TODO: change it for the delay to be the *max* time waited
				//
				// if the agents take alpha milis (alpha < delay) to execute
				// the system should wait for an additional (delay - alpha)
				//
				// if the agents take beta milis (beta >= delay) to execute
				// the system should not wait any further
				//
				// (could do some optim when delay == 0)
				//
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// plan next execution
				systemExecutor.execute(this);
			}
		}
	};

	@Override
	public void run(long milis) {
		delay = milis;
		if (!doRun) {
			doRun = true;
			systemExecutor.execute(loopingRunnable);
		}
	}

	protected Runnable steppingRunnable = new Runnable() {
		@Override
		public void run() {
			if (!doRun) {
				stepAndHooks();
			}
		}
	};

	@Override
	public Future<?> step() {
		return systemExecutor.submit(steppingRunnable);
	}

	protected Runnable pausingRunnable = new Runnable() {
		@Override
		public void run() {
			doRun = false;
			delay = 0L;
		}
	};

	@Override
	public Future<?> pause() {
		return systemExecutor.submit(pausingRunnable);
	}

	protected Runnable shutdownRunnable = new Runnable() {
		@Override
		public void run() {
			doRun = false;
			agentExecutor.shutdown();
			systemExecutor.shutdown();
		}
	};

	@Override
	public Future<?> shutdown() {
		return systemExecutor.submit(shutdownRunnable);
	}

	/*** Executor Handling ***/
	@Override
	public ExecutorService getExecutorService() {
		return agentExecutor;
	}

	@Override
	public void setExecutorService(ExecutorService executor) {
		ExecutorService old = agentExecutor;
		agentExecutor = executor;
		old.shutdown();
	}

	/*** Agent Handling ***/

	@Override
	public Collection<AgentType> getAgents() {
		return new HashSet<AgentType>(agents);
	}

	@Override
	public void addAgent(AgentType agent) {
		this.agents.add(agent);
	}

	@Override
	public void removeAgent(AgentType agent) {
		this.agents.remove(agent);
	}

	@Override
	public void addAgents(Collection<AgentType> agents) {
		for (AgentType agent : agents) {
			addAgent(agent);
		}
	}

	@Override
	public void removeAgents(Collection<AgentType> agents) {
		for (AgentType agent : agents) {
			removeAgent(agent);
		}
	}

	@Override
	public void addPreStepHook(Runnable task) {
		preStepHooks.add(task);
	}

	@Override
	public void addPostStepHook(Runnable task) {
		postStepHooks.add(task);
	}

	@Override
	public void removePreStepHook(Runnable task) {
		preStepHooks.remove(task);
	}

	@Override
	public void removePostStepHook(Runnable task) {
		postStepHooks.remove(task);
	}

	@Override
	public Set<Runnable> getPreStepHooks() {
		return new HashSet<Runnable>(preStepHooks);
	}

	@Override
	public Set<Runnable> getPostStepHooks() {
		return new HashSet<Runnable>(postStepHooks);
	}

}
