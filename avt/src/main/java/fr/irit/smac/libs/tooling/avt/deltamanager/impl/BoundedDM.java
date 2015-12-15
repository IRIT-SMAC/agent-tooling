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

import java.math.BigDecimal;

import fr.irit.smac.libs.tooling.avt.deltamanager.DeltaManager;

public class BoundedDM extends ForwardingDM {

	/**
	 * @param dm
	 * @throws IllegalArgumentException
	 *             if dm == null
	 */
	public BoundedDM(DeltaManager dm) {
		super(dm);
	}

	@Override
	public void adjustDelta(Direction direction) {
		super.adjustDelta(direction);
		this.ensureBoundedDelta();
	}

	@Override
	public void reconfigure(double deltaMin, BigDecimal range) {
		super.reconfigure(deltaMin, range);
		this.ensureBoundedDelta();
	}

	private void ensureBoundedDelta() {
		double currentDelta = this.getBoundedDelta(this.dm.getDelta());
		if (currentDelta != this.dm.getDelta()) {
			this.dm.getAdvancedDM().setDelta(currentDelta);
		}
	}

	private double getBoundedDelta(double delta) {
		if (Double.isNaN(delta)) {
			throw new IllegalArgumentException("delta isNaN");
		}
		double currentDelta = delta;
		if (currentDelta < this.dm.getAdvancedDM().getDeltaMin()) {
			currentDelta = this.dm.getAdvancedDM().getDeltaMin();
		}
		return currentDelta;
	}

	@Override
	public double getDeltaIf(Direction direction) {
		double delta = super.getDeltaIf(direction);
		return this.getBoundedDelta(delta);
	}

}
