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

import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.GeometricDE;
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.GeometricDEFactory;

public class DeterministicGDEFactory implements GeometricDEFactory {

	private final double increaseFactor;
	private final double decreaseFactor;

	public DeterministicGDEFactory(double increaseFactor, double decreaseFactor) {
		super();
		this.increaseFactor = increaseFactor;
		this.decreaseFactor = decreaseFactor;
	}

	@Override
	public GeometricDE createInstance() {
		return new DeterministicGDE(increaseFactor, decreaseFactor);
	}

}