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
package fr.irit.smac.libs.tooling.messaging.impl;

import fr.irit.smac.libs.tooling.messaging.IDirectory;
import fr.irit.smac.libs.tooling.messaging.ISender;

/**
 * An implementation that get ref in a directory to send messages
 * 
 * @author lemouzy
 *
 * @param <T>
 */
public class BasicSender<T> implements ISender<T> {

    private final IDirectory<T> directory;
    private final Ref<T>        broadCastMsgBox;

    public BasicSender(IDirectory<T> directory) {
        this.directory = directory;
        this.broadCastMsgBox = this.directory.getGroupRef(IDirectory.ALL);
    }

    @Override
    public IDirectory<T> getDirectory() {
        return this.directory;
    }

    @Override
    public boolean send(T msg, String receiverId) {
        try {
            return this.directory.getAgentRef(receiverId).getMsgSink()
                .putMsg(msg);
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException(
                "Trying to send a message to an unknown receiver : "
                    + receiverId);
        }
    }

    @Override
    public boolean send(T msg, Ref<T> receiverRef) {
        return receiverRef.getMsgSink().putMsg(msg);
    }

    @Override
    public boolean sendToGroup(T msg, String groupId) {
        try {
            return this.directory.getGroupRef(groupId).getMsgSink().putMsg(msg);
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException(
                "Trying to send a message to an unknown group : " + groupId);
        }
    }

    @Override
    public boolean sendToGroup(T msg, Ref<T> groupRef) {
        return groupRef.getMsgSink().putMsg(msg);
    }

    @Override
    public boolean broadcast(T msg) {
        return this.broadCastMsgBox.getMsgSink().putMsg(msg);
    }

}
