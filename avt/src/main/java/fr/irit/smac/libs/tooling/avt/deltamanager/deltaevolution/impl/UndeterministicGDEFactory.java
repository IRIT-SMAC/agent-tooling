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
package fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl;

import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDE;
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDEFactory;

/**
 * A factory for creating UndeterministicGDE objects.
 */
public class UndeterministicGDEFactory implements IGeometricDEFactory {

    /** The increase factor. */
    private final double increaseFactor;
    
    /** The decrease factor. */
    private final double decreaseFactor;
    
    /** The decrease noise. */
    private final double decreaseNoise;
    
    /** The random seed. */
    private final Long   randomSeed;

    /**
     * Instantiates a new undeterministic gde factory.
     *
     * @param increaseFactor the increase factor
     * @param decreaseFactor the decrease factor
     * @param decreaseNoise the decrease noise
     */
    public UndeterministicGDEFactory(double increaseFactor, double decreaseFactor, double decreaseNoise) {
        this(increaseFactor, decreaseFactor, decreaseNoise, null);
    }

    /**
     * Instantiates a new undeterministic gde factory.
     *
     * @param increaseFactor the increase factor
     * @param decreaseFactor the decrease factor
     * @param decreaseNoise the decrease noise
     * @param randomSeed the random seed
     */
    public UndeterministicGDEFactory(double increaseFactor, double decreaseFactor, double decreaseNoise,
        Long randomSeed) {
        super();
        this.increaseFactor = increaseFactor;
        this.decreaseFactor = decreaseFactor;
        this.decreaseNoise = decreaseNoise;
        this.randomSeed = randomSeed;
    }

    /* (non-Javadoc)
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDEFactory#createInstance()
     */
    @Override
    public IGeometricDE createInstance() {
        if (this.randomSeed == null) {
            return new UndeterministicGDE(increaseFactor, decreaseFactor, decreaseNoise);
        }
        else {
            return new UndeterministicGDE(increaseFactor, decreaseFactor, decreaseNoise, randomSeed);
        }
    }

}
