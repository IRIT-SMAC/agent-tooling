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

import fr.irit.smac.libs.tooling.messaging.impl.messagecontainer.IMsgSink;

/**
 * A representation of a kind of address used to point to message boxes.
 * 
 * This implementation hides to external package a direct access to a message
 * sink.
 *
 * @author lemouzy
 * @param <T> the generic type
 */
public abstract class Ref<T> implements Comparable<Ref<T>> {
    
    /** The id. */
    private final String id;

    /**
     * Instantiates a new ref.
     *
     * @param id the id
     */
    public Ref(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "@" + id;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Ref<T> ref) {
        return this.getId().compareTo(ref.getId());
    }

    /**
     * Gets the msg sink.
     *
     * @return the external package hidden message sink
     */
    abstract IMsgSink<T> getMsgSink();
}
