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
package fr.irit.smac.libs.tooling.scheduling.example;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;

import javax.swing.JFrame;

import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy;
import fr.irit.smac.libs.tooling.scheduling.contrib.gui.SystemControllerPanel;
import fr.irit.smac.libs.tooling.scheduling.impl.system.SynchronizedSystemStrategy;

public class AllInOneSystem extends SynchronizedSystemStrategy {

	@SuppressWarnings("serial")
	JFrame frame = new JFrame() {
		{
			this.add(new SystemControllerPanel(AllInOneSystem.this, 500));
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					AllInOneSystem.this.shutdown();
				}
			});
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	};

	public AllInOneSystem() {
		super(new HashSet<IAgentStrategy>());
		frame.pack();
		frame.setVisible(true);
	}

}
