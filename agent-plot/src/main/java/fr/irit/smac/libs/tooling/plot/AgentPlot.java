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
	private static IAgentPlotServer defaultServer;

	/**
	 * Server's map linking name and server
	 */
	private static Map<String, IAgentPlotServer> servers = new TreeMap<String, IAgentPlotServer>();

	/**
	 * Create a new server which will be accessible through network with the
	 * port _port
	 * 
	 * @param _port
	 */
	public static void createServer(int _port) {
		defaultServer = new AgentPlotServer("default", _port);
	}

	/**
	 * Create a new server with the name _name and which will be accessible
	 * through network with the port _port. The name is only used in the
	 * server's side
	 * 
	 * @param _name
	 * @param _port
	 */
	public static void createServer(String _name, int _port) {
		servers.put(_name, new AgentPlotServer(_name, _port));
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
	 * @param _name
	 * @return
	 */
	public static IAgentPlotServer getServer(String _name) {
		if (!servers.containsKey(_name)) {
			servers.put(_name, new AgentPlotServer(_name));
		}
		return servers.get(_name);
	}

	/**
	 * Get the chart with the given name if any. Otherwise, create it.
	 * 
	 * @param _name
	 * @return
	 */
	public static IAgentPlotChart getChart(String _name) {
		return getServer().getChart(_name);
	}

	/**
	 * Get the chart _name located on the server _serverName if any. Otherwise,
	 * create it.
	 * 
	 * @param _name
	 * @param _serverName
	 * @return
	 */
	public static IAgentPlotChart getChart(String _name, String _serverName) {
		return getServer(_serverName).getChart(_name);
	}

	/**
	 * Configure the connection to a server and set a local name. The name _name
	 * will be then used with the method getServer(String _name).
	 * 
	 * @param _name
	 * @param _host
	 * @param _port
	 */
	public static void configServer(String _name, String _host, int _port) {
		servers.put(_name, new AgentPlotDistantServer(_host, _port));
	}

	/**
	 * Configure the connection with the default server.
	 * 
	 * @param _host
	 * @param _port
	 */
	public static void configServer(String _host, int _port) {
		defaultServer = new AgentPlotDistantServer(_host, _port);
	}
}
