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

import java.util.concurrent.ExecutorService;

/**
 * 
 * This interface specifies the capability of a system to change its executor
 * service.
 * <p>
 * An important assumption which should be followed by all implementations is
 * that an {@link ExecutorService} is automatically shut down when replaced or
 * if the system is shut down.
 * 
 * @author jorquera
 * 
 */
public interface IExecutorServiceHandler {

	/**
	 * 
	 * @return the currently used {@link ExecutorService}
	 */
	public ExecutorService getExecutorService();

	/**
	 * 
	 * Replace the current {@link ExecutorService} with a new one. The old
	 * {@link ExecutorService} is automatically shut down.
	 * 
	 * @param executor
	 *            the new executor service to use
	 */
	public void setExecutorService(ExecutorService executor);

}
