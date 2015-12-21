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

public class StandardDMFactory implements IDeltaManagerFactory<IDeltaManager> {

    private final IGeometricDEFactory deltaEvolutionFactory;
    private final IDMDecisionFactory  dmDecisionFactory;
    private final double              deltaMin;
    private final double              deltaMax;

    public StandardDMFactory(IGeometricDEFactory deltaEvolutionFactory, IDMDecisionFactory dmDecisionFactory,
        double deltaMin) {
        this(deltaEvolutionFactory, dmDecisionFactory, deltaMin, Double.NaN);
    }

    public StandardDMFactory(IGeometricDEFactory deltaEvolutionFatory, IDMDecisionFactory dmDecisionFactory,
        double deltaMin, double deltaMax) {
        super();
        this.deltaEvolutionFactory = deltaEvolutionFatory;
        this.dmDecisionFactory = dmDecisionFactory;
        this.deltaMin = deltaMin;
        this.deltaMax = deltaMax;
    }

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
