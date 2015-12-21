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
package fr.irit.smac.libs.tooling.plot.interfaces;

import fr.irit.smac.libs.tooling.plot.commons.EChartType;

/**
 * Common server's interface between the server and the client.
 * 
 * @author Alexandre Perles
 * 
 */
public interface IAgentPlotServer {
    /**
     * Get or create the chart which is called _name.
     * 
     * @param name
     * @return
     */
    public IAgentPlotChart getChart(String name);

    /**
     * Preconfigure the chart _name with a chart's type and a number of series
     * that will be displayed.
     * 
     * @param name
     * @param chartType
     */
    public void configChart(String name, EChartType chartType);
}
