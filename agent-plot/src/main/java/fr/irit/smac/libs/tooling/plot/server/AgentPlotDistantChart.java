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
package fr.irit.smac.libs.tooling.plot.server;

import java.io.PrintWriter;

import fr.irit.smac.libs.tooling.plot.commons.EChartType;
import fr.irit.smac.libs.tooling.plot.interfaces.IAgentPlotChart;

/**
 * Representation of a distant chart made by a client.
 * 
 * @author Alexandre Perles, Thomas Sontheimer
 * 
 */
public class AgentPlotDistantChart implements IAgentPlotChart {

    private String      name;
    private PrintWriter out;

    public AgentPlotDistantChart(String name, EChartType chartType, PrintWriter out) {
        this.name = name;
        this.out = out;
        this.out.println("config;" + name + ";" + chartType.toString());
        this.out.flush();
    }

    @Override
    public void add(double y) {
        add("", y);
    }

    @Override
    public void add(double x, double y) {
        add("", x, y);
    }

    @Override
    public void add(String serieName, double y) {
        out.println("add;" + name + ";" + serieName + ";;" + y);
        out.flush();
    }

    @Override
    public void add(String serieName, double x, double y) {
        out.println("add;" + name + ";" + serieName + ";" + x + ";" + y);
        out.flush();
    }

    @Override
    public void close() {
        out.println("close;" + name);
        out.flush();
    }

}
