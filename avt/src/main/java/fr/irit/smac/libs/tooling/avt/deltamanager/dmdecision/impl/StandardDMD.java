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
package fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl;

import fr.irit.smac.libs.tooling.avt.deltamanager.DeltaManager.Direction;
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.DMDecision;

public class StandardDMD implements DMDecision {

	private Direction lastDirection;

	public StandardDMD() {
		this.resetState();
	}

	@Override
	public Decision getNextDecision(Direction direction) {

		Decision decision = this.getNextDecisionIf(direction);
		this.lastDirection = direction;

		return decision;
	}

	@Override
	public void resetState() {
		this.lastDirection = null;
	}

	@Override
	public Decision getNextDecisionIf(Direction direction) {
		if (direction == null) {
			throw new IllegalArgumentException("direction is null");
		}

		Decision decision = null;

		if (this.lastDirection == Direction.NONE) {
			if (direction == Direction.NONE) {
				decision = Decision.DECREASE_DELTA;
			} else {
				decision = Decision.INCREASE_DELTA;
			}
		} else if (this.lastDirection != null) {
			if (this.lastDirection != direction) {
				decision = Decision.DECREASE_DELTA;
			} else {
				decision = Decision.INCREASE_DELTA;
			}
		}

		return decision;
	}

}
