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
 * An implementation that get ref in a directory to send messages.
 *
 * @author lemouzy
 * @param <T> the generic type
 */
public class BasicSender<T> implements ISender<T> {

    /** The directory. */
    private final IDirectory<T> directory;
    
    /** The broad cast msg box. */
    private final Ref<T>        broadCastMsgBox;

    /**
     * Instantiates a new basic sender.
     *
     * @param directory the directory
     */
    public BasicSender(IDirectory<T> directory) {
        this.directory = directory;
        this.broadCastMsgBox = this.directory.getGroupRef(IDirectory.ALL);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#getDirectory()
     */
    @Override
    public IDirectory<T> getDirectory() {
        return this.directory;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#send(java.lang.Object, java.lang.String)
     */
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

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#send(java.lang.Object, fr.irit.smac.libs.tooling.messaging.impl.Ref)
     */
    @Override
    public boolean send(T msg, Ref<T> receiverRef) {
        return receiverRef.getMsgSink().putMsg(msg);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#sendToGroup(java.lang.Object, java.lang.String)
     */
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

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#sendToGroup(java.lang.Object, fr.irit.smac.libs.tooling.messaging.impl.Ref)
     */
    @Override
    public boolean sendToGroup(T msg, Ref<T> groupRef) {
        return groupRef.getMsgSink().putMsg(msg);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#broadcast(java.lang.Object)
     */
    @Override
    public boolean broadcast(T msg) {
        return this.broadCastMsgBox.getMsgSink().putMsg(msg);
    }

}
