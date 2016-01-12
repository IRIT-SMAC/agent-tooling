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
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDEFactory;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecisionFactory;
import fr.irit.smac.libs.tooling.avt.range.IRange;

/**
 * A factory for creating StandardDM objects.
 */
public class StandardDMFactory implements IDeltaManagerFactory<IDeltaManager> {

    /** The delta evolution factory. */
    private final IGeometricDEFactory deltaEvolutionFactory;
    
    /** The dm decision factory. */
    private final IDMDecisionFactory  dmDecisionFactory;
    
    /** The delta min. */
    private final double              deltaMin;
    
    /** The delta max. */
    private final double              deltaMax;

    /**
     * Instantiates a new standard dm factory.
     *
     * @param deltaEvolutionFactory the delta evolution factory
     * @param dmDecisionFactory the dm decision factory
     * @param deltaMin the delta min
     */
    public StandardDMFactory(IGeometricDEFactory deltaEvolutionFactory, IDMDecisionFactory dmDecisionFactory,
        double deltaMin) {
        this(deltaEvolutionFactory, dmDecisionFactory, deltaMin, Double.NaN);
    }

    /**
     * Instantiates a new standard dm factory.
     *
     * @param deltaEvolutionFatory the delta evolution fatory
     * @param dmDecisionFactory the dm decision factory
     * @param deltaMin the delta min
     * @param deltaMax the delta max
     */
    public StandardDMFactory(IGeometricDEFactory deltaEvolutionFatory, IDMDecisionFactory dmDecisionFactory,
        double deltaMin, double deltaMax) {
        super();
        this.deltaEvolutionFactory = deltaEvolutionFatory;
        this.dmDecisionFactory = dmDecisionFactory;
        this.deltaMin = deltaMin;
        this.deltaMax = deltaMax;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory#createInstance(fr.irit.smac.libs.tooling.avt.range.IRange)
     */
    @Override
    public IDeltaManager createInstance(IRange range) {
        if (Double.isNaN(this.deltaMax)) {
            return new StandardDM(deltaMin, range, this.deltaEvolutionFactory.createInstance(),
                this.dmDecisionFactory.createInstance());
        }
        else {
            return new StandardDM(deltaMin, deltaMax, range, this.deltaEvolutionFactory.createInstance(),
                this.dmDecisionFactory.createInstance());
        }
    }
}
