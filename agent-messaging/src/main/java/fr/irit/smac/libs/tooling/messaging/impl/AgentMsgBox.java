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

import java.util.List;

import fr.irit.smac.libs.tooling.messaging.IDirectory;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.BlockingQueueMsgContainer;
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.DummyMsgContainer;
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgContainer;
import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSink;

/**
 * Base implementation of the MsgBox used by agents.
 *
 * @author lemouzy
 * @param <T> the generic type
 */
class AgentMsgBox<T> extends Ref<T> implements IMsgBox<T> {

    /** The Constant dummyMsgContainer. */
    private static final DummyMsgContainer dummyMsgContainer = new DummyMsgContainer();

    /** The directory. */
    private final IMutableDirectory<T>     directory;
    
    /** The msg container. */
    private volatile IMsgContainer<T>      msgContainer;
    
    /** The sender. */
    private final BasicSender<T>           sender;

    /**
     * Instantiates a new agent msg box.
     *
     * @param agentId the agent id
     * @param directory the directory
     */
    public AgentMsgBox(String agentId, IMutableDirectory<T> directory) {
        super(agentId);
        this.directory = directory;
        this.msgContainer = new BlockingQueueMsgContainer<T>();
        this.sender = new BasicSender<T>(this.directory);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.IMsgBox#getRef()
     */
    @Override
    public Ref<T> getRef() {
        return this;
    }

    // /////////////////////////////////////////////////////////////////////////////
    // Directory Concerns
    // /////////////////////////////////////////////////////////////////////////////

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#getDirectory()
     */
    @Override
    public IDirectory<T> getDirectory() {
        return this.directory;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.IReceiver#subscribeToGroup(java.lang.String)
     */
    @Override
    public Ref<T> subscribeToGroup(String groupId) {
        return this.directory.subscribeAgentToGroup(this, groupId);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.IReceiver#subscribeToGroup(fr.irit.smac.libs.tooling.messaging.impl.Ref)
     */
    @Override
    public void subscribeToGroup(Ref<T> groupRef) {
        this.directory.subscribeAgentToGroup(this, groupRef);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.IReceiver#unsubscribeToGroup(java.lang.String)
     */
    @Override
    public void unsubscribeToGroup(String groupId) {
        this.directory.unsubscribeAgentFromGroup(this, groupId);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.IReceiver#unsubscribeToGroup(fr.irit.smac.libs.tooling.messaging.impl.Ref)
     */
    @Override
    public void unsubscribeToGroup(Ref<T> groupRef) {
        this.directory.unsubscribeAgentFromGroup(this, groupRef);
    }

    // /////////////////////////////////////////////////////////////////////////////
    // Sending concerns
    // /////////////////////////////////////////////////////////////////////////////

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#send(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean send(T msg, String receiverId) {
        return this.sender.send(msg, receiverId);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#send(java.lang.Object, fr.irit.smac.libs.tooling.messaging.impl.Ref)
     */
    @Override
    public boolean send(T msg, Ref<T> receiverRef) {
        return this.sender.send(msg, receiverRef);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#sendToGroup(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean sendToGroup(T msg, String groupId) {
        return this.sender.sendToGroup(msg, groupId);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#sendToGroup(java.lang.Object, fr.irit.smac.libs.tooling.messaging.impl.Ref)
     */
    @Override
    public boolean sendToGroup(T msg, Ref<T> groupRef) {
        return this.sender.sendToGroup(msg, groupRef);
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.ISender#broadcast(java.lang.Object)
     */
    @Override
    public boolean broadcast(T msg) {
        return this.sender.broadcast(msg);
    }

    // /////////////////////////////////////////////////////////////////////////////
    // MsgContainer Concerns
    // /////////////////////////////////////////////////////////////////////////////

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSource#getMsgs()
     */
    @Override
    public List<T> getMsgs() {
        return this.msgContainer.getMsgs();
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.impl.Ref#getMsgSink()
     */
    @Override
    IMsgSink<T> getMsgSink() {
        return this.msgContainer;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.messaging.IMsgBox#dispose()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void dispose() {
        this.directory.removeAgentMsgBox(this);
        this.msgContainer = (IMsgContainer<T>) AgentMsgBox.dummyMsgContainer;
    }

}
