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
package fr.irit.smac.libs.tooling.avt;

import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManagerFactory;

/**
 * The Interface IAVTBuilder.
 *
 * @param <T> the generic type
 */
public interface IAVTBuilder<T extends IAVT> {

    /**
     * Checks if is hard bounds.
     *
     * @param isHardBounds the is hard bounds
     * @return the IAVT builder
     */
    public IAVTBuilder<T> isHardBounds(boolean isHardBounds);

    /**
     * Avt factory.
     *
     * @param avtFactory the avt factory
     * @return the IAVT builder
     */
    public IAVTBuilder<T> avtFactory(IAVTFactory<T> avtFactory);

    /**
     * Soft bounds memory.
     *
     * @param softBoundsMemory the soft bounds memory
     * @return the IAVT builder
     * @throws IllegalArgumentException             if softBoundsMemory < 0
     * @throws IllegalStateException             hardBounds are set to true
     */
    public IAVTBuilder<T> softBoundsMemory(int softBoundsMemory);

    /**
     * Checks if is delayed delta.
     *
     * @param isDelayed the is delayed
     * @return the IAVT builder
     */
    public IAVTBuilder<T> isDelayedDelta(boolean isDelayed);

    /**
     * Checks if is bounded delta.
     *
     * @param isBounded the is bounded
     * @return the IAVT builder
     */
    public IAVTBuilder<T> isBoundedDelta(boolean isBounded);

    /**
     * Checks if is deterministic delta.
     *
     * @param isDeterministic the is deterministic
     * @return the IAVT builder
     */
    public IAVTBuilder<T> isDeterministicDelta(boolean isDeterministic);

    /**
     * Delta increase factor.
     *
     * @param increaseFactor the increase factor
     * @return the IAVT builder
     * @throws IllegalArgumentException             if increaseFactor < 1.
     */
    public IAVTBuilder<T> deltaIncreaseFactor(double increaseFactor);

    /**
     * Delta decrease factor.
     *
     * @param decreaseFactor the decrease factor
     * @return the IAVT builder
     * @throws IllegalArgumentException             if decreaseFactor < 1.
     */
    public IAVTBuilder<T> deltaDecreaseFactor(double decreaseFactor);

    /**
     * Delta decrease noise.
     *
     * @param decreaseNoise the decrease noise
     * @return the IAVT builder
     * @throws IllegalStateException             if delta has been set to deterministic thanks to
     *             this.setDeterministic(true)
     * @throws IllegalArgumentException             if decreaseNoise <= 0
     */
    public IAVTBuilder<T> deltaDecreaseNoise(double decreaseNoise);

    /**
     * Sets the random seed that defines the non deterministic evolution of
     * delta decrease.
     *
     * @param seed the seed
     * @return the IAVT builder
     */
    public IAVTBuilder<T> deltaRandomSeed(long seed);

    /**
     * Delta increase delay.
     *
     * @param increaseDelay the increase delay
     * @return the IAVT builder
     * @throws IllegalStateException             if delta has not been set to delayed thanks to a call to
     *             this.setDelayedDelat(true)
     * @throws IllegalArgumentException             if increaseDelay < 0
     */
    public IAVTBuilder<T> deltaIncreaseDelay(int increaseDelay);

    /**
     * Delta decrease delay.
     *
     * @param decreaseDelay the decrease delay
     * @return the IAVT builder
     */
    public IAVTBuilder<T> deltaDecreaseDelay(int decreaseDelay);

    /**
     * Delta min.
     *
     * @param deltaMin the delta min
     * @return the IAVT builder
     */
    public IAVTBuilder<T> deltaMin(double deltaMin);

    /**
     * Delta max.
     *
     * @param deltaMax the delta max
     * @return the IAVT builder
     */
    public IAVTBuilder<T> deltaMax(double deltaMax);

    /**
     * Lower bound.
     *
     * @param lowerBound the lower bound
     * @return the IAVT builder
     */
    public IAVTBuilder<T> lowerBound(double lowerBound);

    /**
     * Upper bound.
     *
     * @param upperBound the upper bound
     * @return the IAVT builder
     */
    public IAVTBuilder<T> upperBound(double upperBound);

    /**
     * Start value.
     *
     * @param startValue the start value
     * @return the IAVT builder
     */
    public IAVTBuilder<T> startValue(double startValue);

    /**
     * Delta manager factory.
     *
     * @param deltaManagerFactory the delta manager factory
     * @return the IAVT builder
     * @throw new IllegalArgumentException if deltaManager == null
     */
    public IAVTBuilder<T> deltaManagerFactory(IDeltaManagerFactory<?> deltaManagerFactory);

    /**
     * Builds the.
     *
     * @return the t
     * @throws IllegalStateException             if this.deltaMin > this.deltaMax
     */
    public T build();

}