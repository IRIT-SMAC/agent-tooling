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
package fr.irit.smac.libs.tooling.plot.interfaces;

/**
 * Common chart's interface between the server and the client.
 * 
 * @author Alexandre Perles, Thomas Sontheimer
 * 
 */
public interface IAgentPlotChart {
	/**
	 * Add the point (xMax+1, _y) to the first serie of the chart.
	 * 
	 * @param _y
	 */
	public void add(double _y);

	/**
	 * Add the point (_x,_y) to the first serie of the chart.
	 * 
	 * @param _x
	 * @param _y
	 */
	public void add(double _x, double _y);

	/**
	 * Add the point (xMax+1, _y) to the _serie serie of the chart.
	 * 
	 * @param _serieName
	 * @param _y
	 */
	public void add(String _serieName, double _y);

	/**
	 * Add the point (_x, _y) to the _serie serie of the chart.
	 * 
	 * @param _serieName
	 * @param _x
	 * @param _y
	 */
	public void add(String _serieName, double _x, double _y);

	/**
	 * Close the chart on the server.
	 */
	public void close();
}
