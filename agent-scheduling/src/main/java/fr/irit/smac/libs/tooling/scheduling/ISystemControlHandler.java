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

import java.util.concurrent.Future;

/**
 * This interface controls the execution of a system.
 * <p>
 * The execution of the system can be controlled using the provided methods. The
 * exact effects of the methods depend on the implemented strategy. However some
 * guidelines should be enforced when possible:
 * <p>
 * The {@link #run} method should need to be called only once in order for the
 * system to continuously run. The <code>milis</code> parameter of this method
 * is optional and provided to allow finer control of the execution speed.
 * <p>
 * The {@link #step} method should be as atomic as possible, finishing as soon
 * as it makes sense regarding the strategy.
 * <p>
 * The {@link #pause} method should stop the system on a best effort basis. The
 * system strategy should try to reach a consistent pausing state as soon as
 * possible.
 * <p>
 * The {@link #shutdown} method should shutdown the system should stop the
 * system on a best effort basis. The system strategy should try to reach a
 * consistent halting state as soon as possible.
 * <p>
 * The {@link #step}, {@link #pause} and {@link #shutdown} methods should be
 * non-blocking. They return a {@link Future} which should complete when the
 * desired state is reached. The {@link Future} can be used to block until
 * completion. The {@link #run} should not block in any case, as the system
 * should continue its execution until the {@link #pause} or {@link #shutdown}
 * method is called.
 * <p>
 * However the "desired state" mainly concerns agent execution, meaning that,
 * e.g., it is considered acceptable for the Future returned by
 * {@link #shutdown} to complete as soon as it is guaranteed that no agent will
 * be executed, even if some internals components are still in the process of
 * shutting down.
 * <p>
 * Overall, these requirements should be considered from the <bold>agent
 * execution</bold> point of view, and not necessarily from the actual low-level
 * system point of view (threads and so on).
 * 
 * @author jorquera
 * 
 */
public interface ISystemControlHandler {

	/**
	 * Launches the continuous execution of the system. The system will execute
	 * according to the implemented strategy, until the {@link #pause} method is
	 * called.
	 * <p>
	 * This method should return immediately.
	 * <p>
	 * The parameter <code>milis</code> can be used by the various
	 * implementations in order to introduce specific effects.
	 * 
	 * @param milis
	 *            an optional parameter for controlling the execution speed,
	 *            whose exact effect depends on the implementation
	 */
	public void run(long milis);

	/**
	 * Launches an atomic execution of the system. What is exactly an atomic
	 * execution is defined by the implemented strategy.
	 * 
	 * Implementations should ensure that this method is non-blocking, with the
	 * effect of waiting on the returned {@link Future} similar to a blocking
	 * method.
	 * 
	 * @return a {@link Future} which will complete when the step finishes
	 */
	public Future<?> step();

	/**
	 * Pause the execution of the system if it is currently running. Otherwise
	 * it should have no effect.
	 * 
	 * Implementations should ensure that this method is non-blocking, with the
	 * effect of waiting on the returned {@link Future} similar to a blocking
	 * method.
	 * 
	 * @return a {@link Future} which will complete when the system is
	 *         effectively paused
	 */
	public Future<?> pause();

	/**
	 * Shutdown the system definitively.
	 * 
	 * Implementations should ensure that this method is non-blocking, with the
	 * effect of waiting on the returned {@link Future} similar to a blocking
	 * method.
	 * 
	 * @return a {@link Future} which will complete when the system is
	 *         effectively stopped
	 */
	public Future<?> shutdown();

}
