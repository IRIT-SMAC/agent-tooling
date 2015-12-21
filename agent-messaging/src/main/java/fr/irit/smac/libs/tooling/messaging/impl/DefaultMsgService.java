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
import fr.irit.smac.libs.tooling.messaging.IMsgBox;
import fr.irit.smac.libs.tooling.messaging.IMsgService;

/**
 * A basic implementation of the message service
 * 
 * @author lemouzy
 *
 * @param <T>
 */
public class DefaultMsgService<T> implements IMsgService<T> {

    private final BasicMutableDirectory<T> directory;
    private final BasicSender<T>           sender;

    public DefaultMsgService() {
        super();
        this.directory = new BasicMutableDirectory<T>();
        this.sender = new BasicSender<T>(this.directory);
    }

    @Override
    public IDirectory<T> getDirectory() {
        return this.directory;
    }

    @Override
    public boolean send(T msg, String receiverId) {
        return this.sender.send(msg, receiverId);
    }

    @Override
    public boolean send(T msg, Ref<T> receiverRef) {
        return this.sender.send(msg, receiverRef);
    }

    @Override
    public boolean sendToGroup(T msg, String groupId) {
        return this.sender.sendToGroup(msg, groupId);
    }

    @Override
    public boolean sendToGroup(T msg, Ref<T> groupRef) {
        return this.sender.sendToGroup(msg, groupRef);
    }

    @Override
    public boolean broadcast(T msg) {
        return this.sender.broadcast(msg);
    }

    @Override
    public IMsgBox<T> getMsgBox(String agentId) {
        return this.directory.createAgentMsgBox(agentId);
    }

}
