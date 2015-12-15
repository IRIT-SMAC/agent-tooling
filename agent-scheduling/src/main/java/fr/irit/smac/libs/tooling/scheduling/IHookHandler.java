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

import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 
 * This interface provides capabilities to add "hooks", {@link Runnable} to be
 * executed before or after a system step.
 * <p>
 * The exact way a hook is executed is implementation dependent. A good default
 * could be to execute the hooks on the same {@link Thread} or {@link Executor}
 * than the system executor itself, or to have a specific {@link Executor} for
 * the hooks
 * 
 * @author jorquera
 * 
 */
public interface IHookHandler {

	/**
	 * 
	 * Add a new task to be executed before each step
	 * 
	 * @param task
	 *            the new task to be added to pre-step hooks
	 */
	public void addPreStepHook(Runnable task);

	/**
	 * 
	 * Add a new task to be executed after each step
	 * 
	 * @param task
	 *            the new task to be added to post-step hooks
	 */
	public void addPostStepHook(Runnable task);

	/**
	 * 
	 * Remove a task from pre-step hooks. The task will no longer be executed
	 * before each step.
	 * 
	 * @param task
	 *            the task to be removed from pre-step hooks.
	 */
	public void removePreStepHook(Runnable task);

	/**
	 * 
	 * Remove a task from post-step hooks. The task will no longer be executed
	 * after each step.
	 * 
	 * @param task
	 *            the task to be removed from post-step hooks.
	 */
	public void removePostStepHook(Runnable task);

	/**
	 * 
	 * @return the currently registered pre-step hooks
	 */
	public Set<Runnable> getPreStepHooks();

	/**
	 * 
	 * @return the currently registered post-step hooks
	 */
	public Set<Runnable> getPostStepHooks();

}
