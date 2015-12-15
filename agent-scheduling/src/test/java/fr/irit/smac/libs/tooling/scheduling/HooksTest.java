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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy;
import fr.irit.smac.libs.tooling.scheduling.impl.system.SynchronizedSystemStrategy;

/**
 * TODO: should transform these into unit tests
 * 
 * @author jorquera
 *
 */
public class HooksTest {

	private static class Hook implements Runnable {

		private final String message;

		public Hook(String message) {
			super();
			this.message = message;
		}

		@Override
		public void run() {
			System.out.println("\n-- Hook: " + message);
		}

		@Override
		public String toString() {
			return "Hook[" + message + "]";
		}

	}

	private static class AgentMock implements IAgentStrategy {

		private int step = 0;

		@Override
		public void nextStep() {
			System.out.println("Agent " + this + " is executing on step "
					+ step++);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		Set<IAgentStrategy> agents = new HashSet<IAgentStrategy>();

		for (int i = 0; i < 3; i++) {
			agents.add(new AgentMock());
		}

		final SynchronizedSystemStrategy testedStrat = new SynchronizedSystemStrategy(
				agents, Executors.newFixedThreadPool(2));

		System.out.println("###############");
		System.out.println("testedStrat.getPreStepHooks() "
				+ testedStrat.getPreStepHooks());
		System.out.println("testedStrat.getPostStepHooks() "
				+ testedStrat.getPostStepHooks());
		System.out.println("###############");

		Hook prehook1 = new Hook("Prehook 1 run");
		Hook posthook1 = new Hook("Posthook 1 run");

		testedStrat.addPreStepHook(prehook1);
		testedStrat.addPostStepHook(posthook1);

		System.out.println("###############");
		System.out.println("testedStrat.getPreStepHooks() "
				+ testedStrat.getPreStepHooks());
		System.out.println("testedStrat.getPostStepHooks() "
				+ testedStrat.getPostStepHooks());
		System.out.println("###############");

		testedStrat.run(500);

		Thread.sleep(5000);

		Hook prehook2 = new Hook("Prehook 2 run");
		testedStrat.addPreStepHook(prehook2);

		System.out.println("###############");
		System.out.println("testedStrat.getPreStepHooks() "
				+ testedStrat.getPreStepHooks());
		System.out.println("testedStrat.getPostStepHooks() "
				+ testedStrat.getPostStepHooks());
		System.out.println("###############");

		Thread.sleep(5000);

		testedStrat.removePreStepHook(prehook1);

		System.out.println("###############");
		System.out.println("testedStrat.getPreStepHooks() "
				+ testedStrat.getPreStepHooks());
		System.out.println("testedStrat.getPostStepHooks() "
				+ testedStrat.getPostStepHooks());
		System.out.println("###############");

		Thread.sleep(5000);

		testedStrat.shutdown();

	}

}
