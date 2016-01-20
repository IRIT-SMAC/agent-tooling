/*
 * #%L
 * agent-messaging
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
package fr.irit.smac.libs.tooling.messaging.impl.messagecontainer;

import java.util.List;

import fr.irit.smac.libs.tooling.messaging.ExceptionMessage;

/**
 * An implementation of a dummy msgContainer used when an AgentMsgBox is
 * disposed.
 *
 * @author lemouzy
 */
public class DummyMsgContainer implements IMsgContainer<Object> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSink#putMsg
     * (java.lang.Object)
     */
    @Override
    public boolean putMsg(Object msg) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSource#
     * getMsgs()
     */
    @Override
    public List<Object> getMsgs() {
        throw new IllegalStateException(
            ExceptionMessage.GET_MESSAGES_DISPOSED_MSG_BOX);
    }
}
