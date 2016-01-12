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
package fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl;

import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecisionFactory;

/**
 * A factory for creating DelayedDMD objects.
 */
public class DelayedDMDFactory implements IDMDecisionFactory {

    /** The nested dm decison factory. */
    private final IDMDecisionFactory nestedDMDecisonFactory;
    
    /** The increase delay. */
    private final int                increaseDelay;
    
    /** The decrease delay. */
    private final int                decreaseDelay;

    /**
     * Instantiates a new delayed dmd factory.
     *
     * @param nestedDMDecisonFactory the nested dm decison factory
     * @param increaseDelay the increase delay
     * @param decreaseDelay the decrease delay
     */
    public DelayedDMDFactory(IDMDecisionFactory nestedDMDecisonFactory, int increaseDelay, int decreaseDelay) {
        super();
        this.nestedDMDecisonFactory = nestedDMDecisonFactory;
        this.increaseDelay = increaseDelay;
        this.decreaseDelay = decreaseDelay;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecisionFactory#createInstance()
     */
    @Override
    public IDMDecision createInstance() {
        return new DelayedDMD(this.nestedDMDecisonFactory.createInstance(), this.increaseDelay, this.decreaseDelay);
    }

}
