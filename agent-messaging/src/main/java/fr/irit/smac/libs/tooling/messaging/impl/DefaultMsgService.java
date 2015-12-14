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
import fr.irit.smac.libs.tooling.messaging.IMsgService;

/**
 * A basic implementation of the message service
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
public class DefaultMsgService<MsgType> implements IMsgService<MsgType> {

	private final BasicMutableDirectory<MsgType> directory;
	private final BasicSender<MsgType> sender;

	public DefaultMsgService() {
		super();
		this.directory = new BasicMutableDirectory<MsgType>();
		this.sender = new BasicSender<MsgType>(this.directory);
	}

	@Override
	public IDirectory<MsgType> getDirectory() {
		return this.directory;
	}

	@Override
	public boolean send(MsgType msg, String receiverId) {
		return this.sender.send(msg, receiverId);
	}

	@Override
	public boolean send(MsgType msg, Ref<MsgType> receiverRef) {
		return this.sender.send(msg, receiverRef);
	}

	@Override
	public boolean sendToGroup(MsgType msg, String groupId) {
		return this.sender.sendToGroup(msg, groupId);
	}

	@Override
	public boolean sendToGroup(MsgType msg, Ref<MsgType> groupRef) {
		return this.sender.sendToGroup(msg, groupRef);
	}

	@Override
	public boolean broadcast(MsgType msg) {
		return this.sender.broadcast(msg);
	}

	@Override
	public IMsgBox<MsgType> getMsgBox(String agentId) {
		return this.directory.createAgentMsgBox(agentId);
	}

}
