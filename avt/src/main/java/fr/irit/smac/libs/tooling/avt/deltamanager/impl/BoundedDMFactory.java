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

import fr.irit.smac.libs.tooling.avt.deltamanager.DeltaManager;
import fr.irit.smac.libs.tooling.avt.deltamanager.DeltaManagerFactory;
import fr.irit.smac.libs.tooling.avt.range.Range;

public class BoundedDMFactory implements DeltaManagerFactory<DeltaManager> {

	private final DeltaManagerFactory<?> nestedDeltaManagerFactory;

	public BoundedDMFactory(DeltaManagerFactory<?> nestedDeltaManagerFactory) {
		this.nestedDeltaManagerFactory = nestedDeltaManagerFactory;
	}

	@Override
	public DeltaManager createInstance(Range range) {
		return new BoundedDM(this.nestedDeltaManagerFactory.createInstance(range));
	}
}
