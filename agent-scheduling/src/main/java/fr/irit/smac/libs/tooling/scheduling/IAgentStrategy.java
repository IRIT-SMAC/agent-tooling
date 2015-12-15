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

/**
 * Defines the execution strategy of an agent.
 * <p>
 * The strategy provides a {@link #nextStep} corresponding to the next step to
 * execute according to the execution strategy applied by the agent.
 * 
 * @author jorquera
 * 
 */
public interface IAgentStrategy {

	/**
	 * The next step to be executed according to the execution strategy of the
	 * agent.
	 */
	public void nextStep();

}
