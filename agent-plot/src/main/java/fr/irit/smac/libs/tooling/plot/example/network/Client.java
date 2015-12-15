/*
 * #%L
 * agent-plot
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
package fr.irit.smac.libs.tooling.plot.example.network;

import fr.irit.smac.libs.tooling.plot.AgentPlot;
import fr.irit.smac.libs.tooling.plot.commons.ChartType;

/**
 * Example showing the distant connection to the server.
 * 
 * @author Alexandre Perles
 * 
 */
public class Client {

	public static void main(String[] args) {
		// Server's configuration
		AgentPlot.configServer("My server", "localhost", 6090);

		// Create "My chart" on "My server" and plot two points
		AgentPlot.getServer("My server").getChart("My chart").add(1, 2);
		AgentPlot.getServer("My server").getChart("My chart").add(2, 3);
		
		//Plot two points in serie "My serie"
		AgentPlot.getServer("My server").getChart("My chart").add("My serie", 3, 2.5);
		AgentPlot.getServer("My server").getChart("My chart").add("My serie", 5, 2);

		// Preconfigure chart by setting name and type
		AgentPlot.getServer("My server").configChart("My chart 2",
				ChartType.PLOT);

		// Plot 3 points
		AgentPlot.getServer("My server").getChart("My chart 2").add(1, 2);
		AgentPlot.getServer("My server").getChart("My chart 2").add(2, 3);
		AgentPlot.getServer("My server").getChart("My chart 2").add(3, 2.5);

		// Close charts
		// AgentPlot.getChart("My chart", "My server").close();
		// AgentPlot.getChart("My chart 2", "My server").close();
	}
}
