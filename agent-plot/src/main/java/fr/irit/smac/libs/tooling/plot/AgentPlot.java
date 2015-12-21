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
package fr.irit.smac.libs.tooling.plot;

import java.util.Map;
import java.util.TreeMap;

import fr.irit.smac.libs.tooling.plot.interfaces.IAgentPlotChart;
import fr.irit.smac.libs.tooling.plot.interfaces.IAgentPlotServer;
import fr.irit.smac.libs.tooling.plot.server.AgentPlotDistantServer;
import fr.irit.smac.libs.tooling.plot.server.AgentPlotServer;

/**
 * Main class giving access to all the functionnalities of the library
 * 
 * @author Alexandre Perles
 * 
 */
public class AgentPlot {

    /**
     * If no name is given for the server, this one will be used
     */
    private static IAgentPlotServer              defaultServer;

    /**
     * Server's map linking name and server
     */
    private static Map<String, IAgentPlotServer> servers = new TreeMap<String, IAgentPlotServer>();

    private AgentPlot() {

    }
    
    /**
     * Create a new server which will be accessible through network with the
     * port _port
     * 
     * @param port
     */
    public static void createServer(int port) {
        defaultServer = new AgentPlotServer("default", port);
    }

    /**
     * Create a new server with the name _name and which will be accessible
     * through network with the port _port. The name is only used in the
     * server's side
     * 
     * @param name
     * @param port
     */
    public static void createServer(String name, int port) {
        servers.put(name, new AgentPlotServer(name, port));
    }

    /**
     * Get default server if any. Otherwise create it.
     * 
     * @return
     */
    public static IAgentPlotServer getServer() {
        if (defaultServer == null)
            defaultServer = new AgentPlotServer("default");
        return defaultServer;
    }

    /**
     * Get server with the given name if it exists. Otherwise create a
     * connection to it.
     * 
     * @param name
     * @return
     */
    public static IAgentPlotServer getServer(String name) {
        if (!servers.containsKey(name)) {
            servers.put(name, new AgentPlotServer(name));
        }
        return servers.get(name);
    }

    /**
     * Get the chart with the given name if any. Otherwise, create it.
     * 
     * @param name
     * @return
     */
    public static IAgentPlotChart getChart(String name) {
        return getServer().getChart(name);
    }

    /**
     * Get the chart _name located on the server _serverName if any. Otherwise,
     * create it.
     * 
     * @param name
     * @param serverName
     * @return
     */
    public static IAgentPlotChart getChart(String name, String serverName) {
        return getServer(serverName).getChart(name);
    }

    /**
     * Configure the connection to a server and set a local name. The name _name
     * will be then used with the method getServer(String _name).
     * 
     * @param name
     * @param host
     * @param port
     */
    public static void configServer(String name, String host, int port) {
        servers.put(name, new AgentPlotDistantServer(host, port));
    }

    /**
     * Configure the connection with the default server.
     * 
     * @param host
     * @param port
     */
    public static void configServer(String host, int port) {
        defaultServer = new AgentPlotDistantServer(host, port);
    }
}
