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
package fr.irit.smac.libs.tooling.messaging.impl.messagecontainer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Implementation of a message container based on a ConcurrentLinkedQueue
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
public class ConcurrentQueueMsgContainer<MsgType> implements
		IMsgContainer<MsgType> {

	private ConcurrentLinkedQueue<MsgType> msgQueue;

	private ConcurrentQueueMsgContainer() {
		this.msgQueue = new ConcurrentLinkedQueue<MsgType>();
	}

	@Override
	public boolean putMsg(MsgType msg) {
		return this.msgQueue.offer(msg);
	}

	@Override
	public List<MsgType> getMsgs() {
		List<MsgType> messages = new ArrayList<MsgType>(this.msgQueue.size());
		while (!this.msgQueue.isEmpty()) {
			messages.add(this.msgQueue.poll());
		}

		return messages;
	}

}