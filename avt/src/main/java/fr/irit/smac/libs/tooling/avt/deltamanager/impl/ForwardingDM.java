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

import fr.irit.smac.libs.tooling.avt.deltamanager.AdvancedDM;
import fr.irit.smac.libs.tooling.avt.deltamanager.DeltaManager;

public class ForwardingDM implements AdvancedDM {

	protected final DeltaManager dm;

	/**
	 * @throws IllegalArgumentException
	 *             if dm == null
	 */
	public ForwardingDM(DeltaManager dm) {
		super();
		if (dm == null) {
			throw new IllegalArgumentException("dm == null");
		}

		this.dm = dm;
	}

	@Override
	public void adjustDelta(Direction direction) {
		this.dm.adjustDelta(direction);
	}

	@Override
	public double getDelta() {
		return this.dm.getDelta();
	}

	@Override
	public AdvancedDM getAdvancedDM() {
		return this; // caution, ensure return this and not the reference
						// of the forwarded object
	}

	@Override
	public double getDeltaMin() {
		return this.dm.getAdvancedDM().getDeltaMin();
	}

	@Override
	public double getDeltaMax() {
		return this.dm.getAdvancedDM().getDeltaMax();
	}

	@Override
	public void setDeltaMin(double deltaMin) {
		this.dm.getAdvancedDM().setDeltaMin(deltaMin);
	}

	@Override
	public void setDeltaMax(double deltaMax) {
		this.dm.getAdvancedDM().setDeltaMax(deltaMax);
	}

	@Override
	public void setDelta(double delta) {
		this.dm.getAdvancedDM().setDelta(delta);
	}

	@Override
	public int getGeometricStepNumber() {
		return this.dm.getAdvancedDM().getGeometricStepNumber();
	}

	@Override
	public int getNbGeometricSteps() {
		return this.dm.getAdvancedDM().getNbGeometricSteps();
	}

	@Override
	public void reconfigure(double deltaMin) {
		this.dm.getAdvancedDM().reconfigure(deltaMin);
	}

	@Override
	public void reconfigure(double deltaMin, BigDecimal range) {
		this.dm.getAdvancedDM().reconfigure(deltaMin, range);
	}

	@Override
	public void setGeometricStepNumber(int geometricStepNumber) {
		this.dm.getAdvancedDM().setGeometricStepNumber(geometricStepNumber);
	}

	@Override
	public void resetState() {
		this.dm.getAdvancedDM().resetState();
	}

	@Override
	public double getDeltaIf(Direction direction) {
		return this.dm.getAdvancedDM().getDeltaIf(direction);
	}

}
