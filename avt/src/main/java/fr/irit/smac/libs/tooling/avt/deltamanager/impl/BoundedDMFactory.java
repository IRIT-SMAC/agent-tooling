/*
 * #%L
 * avt
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
package fr.irit.smac.libs.tooling.avt.deltamanager.impl;

import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager;
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory;
import fr.irit.smac.libs.tooling.avt.range.IRange;

/**
 * A factory for creating BoundedDM objects.
 */
public class BoundedDMFactory implements IDeltaManagerFactory<IDeltaManager> {

    /** The nested delta manager factory. */
    private final IDeltaManagerFactory<?> nestedDeltaManagerFactory;

    /**
     * Instantiates a new bounded dm factory.
     *
     * @param nestedDeltaManagerFactory the nested delta manager factory
     */
    public BoundedDMFactory(IDeltaManagerFactory<?> nestedDeltaManagerFactory) {
        this.nestedDeltaManagerFactory = nestedDeltaManagerFactory;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory#createInstance(fr.irit.smac.libs.tooling.avt.range.IRange)
     */
    @Override
    public IDeltaManager createInstance(IRange range) {
        return new BoundedDM(this.nestedDeltaManagerFactory.createInstance(range));
    }
}
