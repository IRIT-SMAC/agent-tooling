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
package fr.irit.smac.libs.tooling.messaging;

import java.util.HashMap;
import java.util.Map;

import fr.irit.smac.libs.tooling.messaging.impl.DefaultMsgService;

/**
 * Main agent-messaging access interface.
 * 
 * Permits to get directly - an IMsgBox<T> - an IMsgService<T>
 * 
 * For example :
 * 
 * - an agent "a1" access a message box sending Strings messages with the
 * following lines:
 * 
 * IMsgBox<String> msgBox = AgentMessaging.getMsgBox("a1", String.class);
 * 
 * - access to the message service associated with String message sending:
 * 
 * IMsgService<String> msgService = AgentMessaging.getMsgService(String.class);
 * 
 * Note : all the created and returned instances are associated to a given
 * message type class. Hence, in the following example, msgBox1 and msgBox2 are
 * different even though they are associated to the same string id.
 * 
 * IMsgBox<String> msgBox1 = AgentMessaging.getMsgBox("a1", String.class);
 * IMsgBox<String> msgBox2 = AgentMessaging.getMsgBox("a1", Integer.class);
 * 
 * This remark is also valid for AgentMessaging.getMsgService
 * 
 * TODO: correct generics in comments
 * 
 * @author lemouzy
 */
public class AgentMessaging {

	// the storage of all message service instance, associated to the message
	// type class the handle
	private static Map<Class<?>, IMsgService<?>> instanciedMsgServices = new HashMap<Class<?>, IMsgService<?>>();

	/**
	 * Get or create a message box for the given agentId, associated to the
	 * given class message service type.
	 * 
	 * @param agentId
	 *            the agent
	 * @param messageClassType
	 *            the class type or the messages handled by the message box
	 * @return
	 */
	public static <MsgType> IMsgBox<MsgType> getMsgBox(String agentId,
			Class<MsgType> messageClassType) {
		return AgentMessaging.<MsgType> getMsgService(messageClassType)
				.getMsgBox(agentId);
	}

	/**
	 * Get or create a message service associated to the given class message
	 * service type.
	 * 
	 * @param messageClassType
	 * @return
	 */
	public static <MsgType> IMsgService<MsgType> getMsgService(
			Class<MsgType> messageClassType) {
		@SuppressWarnings("unchecked")
		IMsgService<MsgType> msgService = (IMsgService<MsgType>) instanciedMsgServices
				.get(messageClassType);
		if (msgService == null) {
			msgService = new DefaultMsgService<MsgType>();
			instanciedMsgServices.put(messageClassType, msgService);
		}

		return msgService;
	}
}
