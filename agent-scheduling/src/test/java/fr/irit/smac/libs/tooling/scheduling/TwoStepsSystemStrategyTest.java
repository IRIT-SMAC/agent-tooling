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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;

import fr.irit.smac.libs.tooling.scheduling.contrib.gui.SystemControllerPanel;
import fr.irit.smac.libs.tooling.scheduling.contrib.twosteps.ITwoStepsAgent;
import fr.irit.smac.libs.tooling.scheduling.contrib.twosteps.TwoStepsSystemStrategy;

/**
 * TODO: should transform these into unit tests
 * 
 * @author jorquera
 *
 */
public class TwoStepsSystemStrategyTest {

	private static class AgentMock implements ITwoStepsAgent {

		private int step = 0;

		@Override
		public void perceive() {
			System.out.println("Agent " + this + " is perceiving " + step);
		}

		@Override
		public void decideAndAct() {
			System.out.println("Agent " + this + " is acting " + step++);
		}

	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		Set<ITwoStepsAgent> agents = new HashSet<ITwoStepsAgent>();

		for (int i = 0; i < 3; i++) {
			agents.add(new AgentMock());
		}

		final TwoStepsSystemStrategy testedStrat = new TwoStepsSystemStrategy(
				agents);

		testedStrat.run(2000);

		Thread.sleep(8000);
		testedStrat.pause();
		System.out.println("======= Testing steps");
		Thread.sleep(1000);
		testedStrat.step();
		testedStrat.step();
		testedStrat.step();
		testedStrat.step();
		Thread.sleep(5000);
		System.out.println("=======  Testing blocking step");
		testedStrat.step().get();
		System.out.println("======= done");
		Thread.sleep(3000);
		System.out.println("=======  Testing non-blocking step");
		testedStrat.step();
		System.out.println("======= done");

		System.out.println("GUI testing...");
		@SuppressWarnings("serial")
		JFrame frame = new JFrame() {
			{
				this.add(new SystemControllerPanel(testedStrat, 500));
				this.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						testedStrat.shutdown();
					}
				});
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		};
		frame.pack();
		frame.setVisible(true);

		// testedStrat.shutdown();

	}

}
