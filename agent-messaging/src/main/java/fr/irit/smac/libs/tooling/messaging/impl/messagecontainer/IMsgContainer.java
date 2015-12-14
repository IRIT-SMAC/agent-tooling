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

/**
 * Interface of a kind of message pipe object.
 * 
 * It can receive messages (IMsgSink<MsgType>) and it can provide the messages
 * it received before (IMsgSource<MsgType>)
 * 
 * TODO: correct generics in comments
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
public interface IMsgContainer<MsgType> extends IMsgSink<MsgType>,
		IMsgSource<MsgType> {

}
