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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

import fr.irit.smac.libs.tooling.plot.commons.EChartType;
import fr.irit.smac.libs.tooling.plot.interfaces.IAgentPlotChart;
import fr.irit.smac.libs.tooling.plot.interfaces.IAgentPlotServer;

/**
 * Representation of the server made by a client
 * 
 * @author Alexandre Perles
 * 
 */
public class AgentPlotDistantServer implements IAgentPlotServer {
    private Map<String, IAgentPlotChart> charts = new TreeMap<String, IAgentPlotChart>();
    private Socket                       socket;
    private PrintWriter                  out;

    public AgentPlotDistantServer(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream());
        }
        catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public IAgentPlotChart getChart(String name) {
        if (!charts.containsKey(name)) {
            charts.put(name, new AgentPlotDistantChart(name, EChartType.LINE, out));
        }
        return charts.get(name);
    }

    @Override
    public void configChart(String name, EChartType chartType) {
        charts.put(name, new AgentPlotDistantChart(name, chartType, out));
    }

}
