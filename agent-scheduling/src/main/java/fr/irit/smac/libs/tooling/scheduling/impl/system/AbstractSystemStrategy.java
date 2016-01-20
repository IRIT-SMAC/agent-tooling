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
 * TODO: Document (important).
 *
 * @author jorquera
 * @param <T>            the type of agents handled by the strategy
 */
public abstract class AbstractSystemStrategy<T> implements
    ISystemControlHandler, IAgentsHandler<T>,
    IExecutorServiceHandler, IHookHandler {

    /** The agents. */
    protected final Set<T>     agents         = Collections
                                                          .synchronizedSet(new HashSet<T>());

    /** The pre step hooks. */
    protected final Set<Runnable>      preStepHooks   = Collections
                                                          .synchronizedSet(new LinkedHashSet<Runnable>());

    /** The post step hooks. */
    protected final Set<Runnable>      postStepHooks  = Collections
                                                          .synchronizedSet(new LinkedHashSet<Runnable>());

    /** The agent executor. */
    protected volatile ExecutorService agentExecutor;
    
    /** The system executor. */
    protected final ExecutorService    systemExecutor = Executors
                                                          .newFixedThreadPool(1);

    /** The do run. */
    private volatile Boolean           doRun          = false;
    
    /** The delay. */
    private volatile long              delay          = 0L;

    /**
     * Instantiates a new abstract system strategy.
     *
     * @param agentExecutor the agent executor
     */
    public AbstractSystemStrategy(ExecutorService agentExecutor) {
        this.agentExecutor = agentExecutor;
    }

    /**
     * TODO: comment (very important).
     */
    protected abstract void doStep();

    /**
     * Pre step.
     */
    protected void preStep() {
        synchronized (preStepHooks) {
            for (Runnable hook : preStepHooks) {
                hook.run();
            }
        }
    }

    /**
     * Post step.
     */
    protected void postStep() {
        synchronized (postStepHooks) {
            for (Runnable hook : postStepHooks) {
                hook.run();
            }
        }
    }

    /**
     * Step and hooks.
     */
    protected void stepAndHooks() {

        preStep();
        doStep();
        postStep();

    }

    /** The looping runnable. */
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
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // plan next execution
                systemExecutor.execute(this);
            }
        }
    };

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.ISystemControlHandler#run(long)
     */
    @Override
    public void run(long milis) {
        delay = milis;
        if (!doRun) {
            doRun = true;
            systemExecutor.execute(loopingRunnable);
        }
    }

    /** The stepping runnable. */
    protected Runnable steppingRunnable = new Runnable() {
        @Override
        public void run() {
            if (!doRun) {
                stepAndHooks();
            }
        }
    };

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.ISystemControlHandler#step()
     */
    @Override
    public Future<?> step() {
        return systemExecutor.submit(steppingRunnable);
    }

    /** The pausing runnable. */
    protected Runnable pausingRunnable = new Runnable() {
        @Override
        public void run() {
            doRun = false;
            delay = 0L;
        }
    };

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.ISystemControlHandler#pause()
     */
    @Override
    public Future<?> pause() {
        return systemExecutor.submit(pausingRunnable);
    }

    /** The shutdown runnable. */
    protected Runnable shutdownRunnable = new Runnable() {
        @Override
        public void run() {
            doRun = false;
            agentExecutor.shutdown();
            systemExecutor.shutdown();
        }
    };

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.ISystemControlHandler#shutdown()
     */
    @Override
    public Future<?> shutdown() {
        return systemExecutor.submit(shutdownRunnable);
    }

    /**
     * * Executor Handling **.
     *
     * @return the executor service
     */
    @Override
    public ExecutorService getExecutorService() {
        return agentExecutor;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IExecutorServiceHandler#setExecutorService(java.util.concurrent.ExecutorService)
     */
    @Override
    public void setExecutorService(ExecutorService executor) {
        ExecutorService old = agentExecutor;
        agentExecutor = executor;
        old.shutdown();
    }

    /**
     * * Agent Handling **.
     *
     * @return the agents
     */

    @Override
    public Collection<T> getAgents() {
        return new HashSet<T>(agents);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IAgentsHandler#addAgent(java.lang.Object)
     */
    @Override
    public void addAgent(T agent) {
        this.agents.add(agent);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IAgentsHandler#removeAgent(java.lang.Object)
     */
    @Override
    public void removeAgent(T agent) {
        this.agents.remove(agent);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IAgentsHandler#addAgents(java.util.Collection)
     */
    @Override
    public void addAgents(Collection<T> agents) {
        for (T agent : agents) {
            addAgent(agent);
        }
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IAgentsHandler#removeAgents(java.util.Collection)
     */
    @Override
    public void removeAgents(Collection<T> agents) {
        for (T agent : agents) {
            removeAgent(agent);
        }
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IHookHandler#addPreStepHook(java.lang.Runnable)
     */
    @Override
    public void addPreStepHook(Runnable task) {
        preStepHooks.add(task);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IHookHandler#addPostStepHook(java.lang.Runnable)
     */
    @Override
    public void addPostStepHook(Runnable task) {
        postStepHooks.add(task);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IHookHandler#removePreStepHook(java.lang.Runnable)
     */
    @Override
    public void removePreStepHook(Runnable task) {
        preStepHooks.remove(task);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IHookHandler#removePostStepHook(java.lang.Runnable)
     */
    @Override
    public void removePostStepHook(Runnable task) {
        postStepHooks.remove(task);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IHookHandler#getPreStepHooks()
     */
    @Override
    public Set<Runnable> getPreStepHooks() {
        return new HashSet<Runnable>(preStepHooks);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.scheduling.IHookHandler#getPostStepHooks()
     */
    @Override
    public Set<Runnable> getPostStepHooks() {
        return new HashSet<Runnable>(postStepHooks);
    }

}
