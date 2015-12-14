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

import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSink;

/**
 * A representation of a kind of address used to point to message boxes.
 * 
 * This implementation hides to external package a direct access to a message
 * sink.
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
abstract public class Ref<MsgType> implements Comparable<Ref<MsgType>> {
	private final String id;

	public Ref(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "@" + id;
	}

	public String getId() {
		return id;
	}

	@Override
	public int compareTo(Ref<MsgType> ref) {
		return this.getId().compareTo(ref.getId());
	}

	/**
	 * @return the external package hidden message sink
	 */
	abstract IMsgSink<MsgType> getMsgSink();
}
