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

import java.util.Random;

import fr.irit.smac.libs.tooling.avt.EMessageException;

/**
 * The Class UndeterministicGDE.
 * 
 * <p>
 * Random is added to the computation of the evolution of the delta.
 * </p>
 * <p>
 * UndeterministicGDE is useful to break the cycles in which the system does not
 * converge.
 * </p>
 */
public class UndeterministicGDE extends DeterministicGDE {

    /** The decrease noise. */
    private final double decreaseNoise;

    /** The random. */
    private final Random random;

    /**
     * Instantiates a new undeterministic gde.
     *
     * @param increaseFactor
     *            the increase factor
     * @param decreaseFactor
     *            the decrease factor
     * @param decreaseNoise
     *            the decrease noise
     * @throws IllegalArgumentException
     *             if decreaseFactor - decreaseNoise < 1 in order to insure that
     *             the average decrease factor is near decreaseFactor
     */
    public UndeterministicGDE(double increaseFactor, double decreaseFactor, double decreaseNoise) {
        this(increaseFactor, decreaseFactor, decreaseNoise, null);
    }

    /**
     * Instantiates a new undeterministic gde.
     *
     * @param increaseFactor
     *            the increase factor
     * @param decreaseFactor
     *            the decrease factor
     * @param decreaseNoise
     *            the decrease noise
     * @param seed
     *            the seed
     * @throws IllegalArgumentException
     *             if decreaseFactor - decreaseNoise < 1 in order to insure that
     *             the average decrease factor is near decreaseFactor
     */
    public UndeterministicGDE(double increaseFactor, double decreaseFactor, double decreaseNoise, Long seed) {
        super(increaseFactor, decreaseFactor);

        if (decreaseNoise <= 0.) {
            throw new IllegalArgumentException(EMessageException.DECREASE_NOISE_LT_ET_0.toString());
        }

        if (Double.isNaN(decreaseNoise)) {
            throw new IllegalArgumentException(EMessageException.DECREASE_NOISE_NAN.toString());
        }

        if (decreaseFactor - decreaseNoise < 1.) {
            throw new IllegalArgumentException(EMessageException.DECREASE_FACTOR_SUB_DECREASE_NOISE_LT_1.toString());
        }

        this.decreaseNoise = decreaseNoise;
        this.random = seed != null ? new Random(seed) : new Random();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.impl.
     * DeterministicGDE#getDecreasedDelta(double)
     */
    @Override
    public double getDecreasedDelta(double delta) {

        if (Double.isNaN(delta)) {
            throw new IllegalArgumentException(EMessageException.DELTA_NAN.toString());
        }

        return delta / Math.max(1.0, this.decreaseFactor - decreaseNoise + random.nextDouble() * decreaseNoise * 2.);
    }

}
