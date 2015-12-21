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

public interface IAVTBuilder<T extends IAVT> {

    public IAVTBuilder<T> isHardBounds(boolean isHardBounds);

    public IAVTBuilder<T> avtFactory(IAVTFactory<T> avtFactory);

    /**
     * 
     * @param softBoundsMemory
     * @return
     * @throws IllegalArgumentException
     *             if softBoundsMemory < 0
     * @throws IllegalStateException
     *             hardBounds are set to true
     */
    public IAVTBuilder<T> softBoundsMemory(int softBoundsMemory);

    public IAVTBuilder<T> isDelayedDelta(boolean isDelayed);

    public IAVTBuilder<T> isBoundedDelta(boolean isBounded);

    public IAVTBuilder<T> isDeterministicDelta(boolean isDeterministic);

    /**
     * 
     * @param increaseFactor
     * @return
     * @throws IllegalArgumentException
     *             if increaseFactor < 1.
     */
    public IAVTBuilder<T> deltaIncreaseFactor(double increaseFactor);

    /**
     * 
     * @param decreaseFactor
     * @return
     * @throws IllegalArgumentException
     *             if decreaseFactor < 1.
     */
    public IAVTBuilder<T> deltaDecreaseFactor(double decreaseFactor);

    /**
     * 
     * @param decreaseNoise
     * @return
     * @throws IllegalStateException
     *             if delta has been set to deterministic thanks to
     *             this.setDeterministic(true)
     * @throws IllegalArgumentException
     *             if decreaseNoise <= 0
     */
    public IAVTBuilder<T> deltaDecreaseNoise(double decreaseNoise);

    /**
     * Sets the random seed that defines the non deterministic evolution of
     * delta decrease
     * 
     * @param seed
     * @return
     */
    public IAVTBuilder<T> deltaRandomSeed(long seed);

    /**
     * 
     * @param increaseDelay
     * @return
     * @throws IllegalStateException
     *             if delta has not been set to delayed thanks to a call to
     *             this.setDelayedDelat(true)
     * @throws IllegalArgumentException
     *             if increaseDelay < 0
     */
    public IAVTBuilder<T> deltaIncreaseDelay(int increaseDelay);

    /**
     * 
     * @param increaseDelay
     * @return
     */
    public IAVTBuilder<T> deltaDecreaseDelay(int decreaseDelay);

    public IAVTBuilder<T> deltaMin(double deltaMin);

    public IAVTBuilder<T> deltaMax(double deltaMax);

    public IAVTBuilder<T> lowerBound(double lowerBound);

    public IAVTBuilder<T> upperBound(double upperBound);

    public IAVTBuilder<T> startValue(double startValue);

    /**
     * 
     * @param deltaManagerFactory
     * @return
     * @throw new IllegalArgumentException if deltaManager == null
     */
    public IAVTBuilder<T> deltaManagerFactory(IDeltaManagerFactory<?> deltaManagerFactory);

    /**
     * @throws IllegalStateException
     *             if this.lowerBound > this.startValue
     * @throws IllegalStateException
     *             if this.startValue > this.upperBound
     * @throws IllegalStateException
     *             if this.lowerBound >= this.upperBound
     * @throws IllegalStateException
     *             if this.deltaMin < 0
     * @throws IllegalStateException
     *             if this.deltaMin > this.deltaMax
     */
    public T build();

}