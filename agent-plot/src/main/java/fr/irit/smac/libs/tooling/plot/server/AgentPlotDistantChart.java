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
package fr.irit.smac.libs.tooling.plot.server;

import java.io.PrintWriter;

import fr.irit.smac.libs.tooling.plot.commons.ChartType;
import fr.irit.smac.libs.tooling.plot.interfaces.IAgentPlotChart;

/**
 * Representation of a distant chart made by a client.
 * 
 * @author Alexandre Perles, Thomas Sontheimer
 * 
 */
public class AgentPlotDistantChart implements IAgentPlotChart {

	private String name;
	private PrintWriter out;

	public AgentPlotDistantChart(String _name, ChartType _chartType, PrintWriter _out) {
		name = _name;
		out = _out;
		out.println("config;" + _name + ";" + _chartType.toString());
		out.flush();
	}

	@Override
	public void add(double _y) {
		add("", _y);
	}

	@Override
	public void add(double _x, double _y) {
		add("", _x, _y);
	}

	@Override
	public void add(String _serieName, double _y) {
		out.println("add;" + name + ";" + _serieName + ";;" + _y);
		out.flush();
	}

	@Override
	public void add(String _serieName, double _x, double _y) {
		out.println("add;" + name + ";" + _serieName + ";" + _x + ";" + _y);
		out.flush();
	}

	@Override
	public void close() {
		out.println("close;" + name);
		out.flush();
	}

}
