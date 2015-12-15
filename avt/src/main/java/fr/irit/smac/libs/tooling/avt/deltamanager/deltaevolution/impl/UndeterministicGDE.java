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

public class UndeterministicGDE extends DeterministicGDE {

	private final double decreaseNoise;
	private final Random random;

	/**
	 * 
	 * @param increaseFactor
	 * @param decreaseFactor
	 * @param decreaseNoise
	 * @throws IllegalArgumentException
	 *             if increaseFactor < 1
	 * @throws IllegalArgumentException
	 *             if decreaseFactor < 1
	 * @throws IllegalArgumentException
	 *             if decreaseNoise <= 0
	 * @throws IllegalArgumentException
	 *             if decreaseFactor - decreaseNoise < 1 in order to insure that the average decrease factor is near
	 *             decreaseFactor
	 */
	public UndeterministicGDE(double increaseFactor, double decreaseFactor, double decreaseNoise) {
		this(increaseFactor, decreaseFactor, decreaseNoise, null);
	}

	/**
	 * 
	 * @param increaseFactor
	 * @param decreaseFactor
	 * @param decreaseNoise
	 * @param seed
	 * @throws IllegalArgumentException
	 *             if increaseFactor < 1
	 * @throws IllegalArgumentException
	 *             if decreaseFactor < 1
	 * @throws IllegalArgumentException
	 *             if decreaseNoise <= 0
	 * @throws IllegalArgumentException
	 *             if decreaseFactor - decreaseNoise < 1 in order to insure that the average decrease factor is near
	 *             decreaseFactor
	 */
	public UndeterministicGDE(double increaseFactor, double decreaseFactor, double decreaseNoise, Long seed) {
		super(increaseFactor, decreaseFactor);

		if (decreaseNoise <= 0.) {
			throw new IllegalArgumentException("decreaseNoise <= 0");
		}

		if (Double.isNaN(decreaseNoise)) {
			throw new IllegalArgumentException("decreaseNoise isNaN");
		}

		if (decreaseFactor - decreaseNoise < 1.) {
			throw new IllegalArgumentException("decreaseFactor - decreaseNoise < 1");
		}

		this.decreaseNoise = decreaseNoise;
		this.random = seed != null ? new Random(seed) : new Random();
	}

	@Override
	public double getDecreasedDelta(double delta) {

		if (Double.isNaN(delta)) {
			throw new IllegalArgumentException("delta isNaN");
		}

		return delta / Math.max(1.0, this.decreaseFactor - decreaseNoise + random.nextDouble() * decreaseNoise * 2.);
	}

}
