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
 * TODO: document.
 *
 * @author jorquera
 */
public class TwoStepsSystemStrategy extends
    AbstractSystemStrategy<ITwoStepsAgent> {

    /**
     * The Enum EState.
     */
    private static enum EState {

        /** The perceive. */
        PERCEIVE,
        /** The decide act. */
        DECIDE_ACT
    }

    /** The current state. */
    private volatile EState currentState = EState.PERCEIVE;

    /**
     * The Class AgentWrapper.
     */
    protected class AgentWrapper implements IAgentStrategy {

        /** The agent. */
        private final ITwoStepsAgent agent;

        /**
         * Instantiates a new agent wrapper.
         *
         * @param agent
         *            the agent
         */
        public AgentWrapper(ITwoStepsAgent agent) {
            this.agent = agent;
        }

        /*
         * (non-Javadoc)
         * 
         * @see fr.irit.smac.libs.tooling.scheduling.IAgentStrategy#nextStep()
         */
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

    /** The agent wrappers. */
    private final Map<ITwoStepsAgent, AgentWrapper>      agentWrappers        = new ConcurrentHashMap<ITwoStepsAgent, AgentWrapper>();

    /** The pending added agents. */
    // NOTE: BlockingQueue implem is thread-safe
    private final BlockingQueue<ITwoStepsAgent>          pendingAddedAgents   = new LinkedBlockingDeque<ITwoStepsAgent>();

    /** The pending removed agents. */
    private final BlockingQueue<ITwoStepsAgent>          pendingRemovedAgents = new LinkedBlockingDeque<ITwoStepsAgent>();

    /** The internal system strategy. */
    private final AbstractSystemStrategy<IAgentStrategy> internalSystemStrategy;

    /**
     * Instantiates a new two steps system strategy.
     *
     * @param agents
     *            the agents
     * @param agentExecutor
     *            the agent executor
     */
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

    /**
     * Instantiates a new two steps system strategy.
     *
     * @param agents
     *            the agents
     */
    public TwoStepsSystemStrategy(Collection<ITwoStepsAgent> agents) {
        // setting a "reasonable" default size for the thread pool of 2xNbCores
        // (according to e.g.
        // http://codeidol.com/java/java-concurrency/Applying-Thread-Pools/Sizing-Thread-Pools/)
        // user should probably override it to a more suitable value
        this(agents, Executors.newFixedThreadPool(Runtime.getRuntime()
            .availableProcessors() * 2));

    }

    /**
     * Adds the pending agents.
     */
    private void addPendingAgents() {

        Collection<ITwoStepsAgent> drainedAgents = new HashSet<ITwoStepsAgent>();
        pendingAddedAgents.drainTo(drainedAgents);

        for (ITwoStepsAgent agent : drainedAgents) {
            AgentWrapper wrapper = new AgentWrapper(agent);
            agentWrappers.put(agent, wrapper);
            internalSystemStrategy.addAgent(wrapper);
        }

    }

    /**
     * Removes the pending agents.
     */
    private void removePendingAgents() {
        Collection<ITwoStepsAgent> drainedAgents = new HashSet<ITwoStepsAgent>();
        pendingRemovedAgents.drainTo(drainedAgents);

        for (ITwoStepsAgent agent : drainedAgents) {
            internalSystemStrategy.removeAgent(agentWrappers.get(agent));
            agentWrappers.remove(agent);

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.scheduling.impl.system.AbstractSystemStrategy
     * #doStep()
     */
    @Override
    protected void doStep() {

        addPendingAgents();

        // start the perceive of the agents and block
        currentState = EState.PERCEIVE;
        try {
            internalSystemStrategy.step().get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

        // start the decide-and-act of the agents
        currentState = EState.DECIDE_ACT;
        try {
            internalSystemStrategy.step().get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

        removePendingAgents();

    }

    /**
     * * Executor Handling **.
     *
     * @return the executor service
     */

    @Override
    public ExecutorService getExecutorService() {
        return internalSystemStrategy.getExecutorService();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.scheduling.impl.system.AbstractSystemStrategy
     * #setExecutorService(java.util.concurrent.ExecutorService)
     */
    @Override
    public void setExecutorService(ExecutorService executor) {
        internalSystemStrategy.setExecutorService(executor);
    }

    /**
     * * Agent Handling **.
     *
     * @param agent
     *            the agent
     */

    @Override
    public void addAgent(final ITwoStepsAgent agent) {
        if (!this.agents.contains(agent)) {
            super.addAgent(agent);
            pendingAddedAgents.add(agent);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.scheduling.impl.system.AbstractSystemStrategy
     * #removeAgent(java.lang.Object)
     */
    @Override
    public void removeAgent(ITwoStepsAgent agent) {
        if (this.agents.contains(agent)) {
            super.removeAgent(agent);
            pendingRemovedAgents.add(agent);

        }
    }

}
