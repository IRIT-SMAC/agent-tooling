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
package fr.irit.smac.libs.tooling.messaging.impl;

import fr.irit.smac.libs.tooling.messaging.IDirectory;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;

/**
 * Interface of a directory that can be modified, and allow add/remove of msgBox
 * and groups
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
interface IMutableDirectory<MsgType> extends IDirectory<MsgType> {

	public Ref<MsgType> subscribeAgentToGroup(Ref<MsgType> agentRef,
			String groupId);

	public void subscribeAgentToGroup(Ref<MsgType> agentRef,
			Ref<MsgType> groupRef);

	public void unsubscribeAgentFromGroup(Ref<MsgType> agentRef, String groupId);

	public void unsubscribeAgentFromGroup(Ref<MsgType> agentRef,
			Ref<MsgType> groupRef);

	public IMsgBox<MsgType> createAgentMsgBox(String agentId);

	public void removeAgentMsgBox(Ref<MsgType> agentRef);
}
