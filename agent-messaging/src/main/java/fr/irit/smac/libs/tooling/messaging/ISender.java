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

import fr.irit.smac.libs.tooling.messaging.impl.Ref;

/**
 * Interface of a service that permits to send messages.
 * 
 * TODO: correct generics in comments
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
public interface ISender<MsgType> {

	/**
	 * Returns the directory used for sending messages
	 * 
	 * @return
	 */
	public IDirectory<MsgType> getDirectory();

	/**
	 * Sends a message to the given id Note : this method is slower than the
	 * send(MsgType msg, Ref<MsgType> receiverRef) method we recommend to use
	 * this other method for performance reasons
	 * 
	 * @param msg
	 * @param receiverId
	 * @return true if the message has been actually sent, false otherwise
	 */
	public boolean send(MsgType msg, String receiverId);

	/**
	 * Sends a message to the given ref Note : this method is faster than the
	 * send(MsgType msg, String receiverId) method
	 * 
	 * @param msg
	 * @param receiverRef
	 * @return true if the message has been actually sent, false otherwise
	 */
	public boolean send(MsgType msg, Ref<MsgType> receiverRef);

	/**
	 * Sends a message to all message boxes inscribed to the given group id Note
	 * : this method is slower than the sendToGroup(MsgType msg, Ref<MsgType>
	 * receiverRef) method we recommend to use this other method for performance
	 * reasons
	 * 
	 * @param msg
	 * @param groupId
	 * @return true if all the messages have been actually sent, false otherwise
	 */
	public boolean sendToGroup(MsgType msg, String groupId);

	/**
	 * Sends a message to all message boxes inscribed to the given group ref
	 * Note : this method is faster than the sendToGroup(MsgType msg, String
	 * receiverId) method
	 * 
	 * @param msg
	 * @param groupId
	 * @return true if all the messages have been actually sent, false otherwise
	 */
	public boolean sendToGroup(MsgType msg, Ref<MsgType> groupRef);

	/**
	 * Sends a message to all the existing message boxes (created with the
	 * current message service).
	 * 
	 * @param msg
	 * @return
	 */
	public boolean broadcast(MsgType msg);
}
