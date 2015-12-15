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
package fr.irit.smac.libs.tooling.plot.example.local;

import org.jfree.chart.JFreeChart;

import fr.irit.smac.libs.tooling.plot.AgentPlot;
import fr.irit.smac.libs.tooling.plot.commons.ChartType;
import fr.irit.smac.libs.tooling.plot.server.AgentPlotChart;

/**
 * Example showing how to locally display charts.
 * 
 * @author Alexandre Perles
 * 
 */
public class LocalChart {
	public static void main(String[] args) {

		// First, we inform the server that the chart "Layer" must be of type
		// PLOT
		AgentPlot.getServer().configChart("Layer", ChartType.PLOT);

		// Add plots to the chart "Layer" for serie #0
		AgentPlot.getChart("Layer").add(1, 2);
		AgentPlot.getChart("Layer").add(2, 2.5);
		AgentPlot.getChart("Layer").add(2, 3);

		// Add plots to the chart "Layer" for serie "My serie"
		AgentPlot.getChart("Layer").add("My serie", 21, 2);
		AgentPlot.getChart("Layer").add("My serie", 12, 2.5);

		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Add plots to the chart "My Chart" which will be created during this
		// call
		AgentPlot.getChart("My chart").add(2, 3);
		AgentPlot.getChart("My chart").add(3, 3);
		AgentPlot.getChart("My chart").add(3, 4);
				
		
		
		//Access the JFreeChart object (only from the server side)
		JFreeChart chart = ((AgentPlotChart)(AgentPlot.getChart("My chart"))).getJFreeChart();
		//Change its title
		chart.getTitle().setText("New title");

		// Close chart
		// AgentPlot.getChart("My chart").close();
		// AgentPlot.getChart("Layer").close();
	}

}
