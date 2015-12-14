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
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSource;

/**
 * Interface of a service that permits to receive messages
 * 
 * @author lemouzy
 *
 * @param <MsgType>
 */
public interface IReceiver<MsgType> extends IMsgSource<MsgType> {

	/**
	 * Add the receiver to the given group. If the corresponding group doesn't
	 * exists, then creates the group. Do noting if the current instance is
	 * already inscribed
	 * 
	 * @param groupId
	 * @return the group it has been added to.
	 */
	public Ref<MsgType> subscribeToGroup(String groupId);

	/**
	 * Add the receiver to the given group Do noting if the current instance is
	 * already inscribed
	 * 
	 * @param groupRef
	 */
	public void subscribeToGroup(Ref<MsgType> groupRef);

	/**
	 * Remove the receiver to the given group Do noting if the current instance
	 * is not actually inscribed
	 * 
	 * @param groupId
	 * @return
	 */
	public void unsubscribeToGroup(String groupId);

	/**
	 * Remove the receiver to the given group Do noting if the current instance
	 * is not actually inscribed
	 * 
	 * @param groupRef
	 * @return
	 */
	public void unsubscribeToGroup(Ref<MsgType> groupRef);
}
