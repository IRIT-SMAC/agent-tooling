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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import fr.irit.smac.libs.tooling.plot.commons.EChartType;
import fr.irit.smac.libs.tooling.plot.interfaces.IAgentPlotChart;
import fr.irit.smac.libs.tooling.plot.interfaces.IAgentPlotServer;

/**
 * Server which is able to receive requests from local or distant client.
 * 
 * @author Alexandre Perles
 * 
 */
public class AgentPlotServer implements IAgentPlotServer, Runnable {

    private ServerSocket                 socket;
    private boolean                      running = false;
    private Map<String, IAgentPlotChart> charts  = new TreeMap<String, IAgentPlotChart>();

    /**
     * Create server NOT accessible through the network
     * 
     * @param name
     */
    public AgentPlotServer(String name) {
        AgentPlotChart.prepareWindow(name);
    }

    /**
     * Create server accessible through the network and locally
     * 
     * @param name
     * @param port
     */
    public AgentPlotServer(String name, int port) {
        this(name + " - localhost:" + port);
        try {
            socket = new ServerSocket(port);
            running = true;
            new Thread(this).start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IAgentPlotChart getChart(String name) {
        if (!charts.containsKey(name)) {
            charts.put(name, new AgentPlotChart(name));
        }
        return charts.get(name);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket clientSocket = socket.accept();
                new AgentPlotConnectedClient(clientSocket, this);
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    public void configChart(String name, EChartType chartType) {
        charts.put(name, new AgentPlotChart(name, chartType));
    }

}
