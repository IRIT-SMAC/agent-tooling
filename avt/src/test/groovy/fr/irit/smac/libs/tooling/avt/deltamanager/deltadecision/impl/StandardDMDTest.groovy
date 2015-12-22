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
package fr.irit.smac.libs.tooling.avt.deltamanager.deltadecision.impl;

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision.EDecision
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.StandardDMD

@Unroll
class StandardDMDTest extends Specification {

    @Shared StandardDMD standardDMD

    def setupSpec() {
        standardDMD = new StandardDMD()
    }

    def 'StandardDMD' () {

        given:
        StandardDMD standardDMDConstructor

        when:
        standardDMDConstructor = new StandardDMD()

        then:
        standardDMDConstructor != null
    }

    def 'resetState' () {

        given:
        standardDMD.lastDirection = EDirection.NONE

        when:
        standardDMD.resetState()

        then:
        standardDMD.lastDirection == null
    }

    def 'getNextDecision' () {

        given:
        standardDMD.lastDirection = EDirection.DIRECT

        when:
        EDecision newDecision = standardDMD.getNextDecision(EDirection.DIRECT)

        then:
        newDecision != null
    }

    def 'getNextDecision with a null direction should throw an IllegalArgumentException' () {

        given:
        standardDMD.lastDirection = EDirection.DIRECT

        when:
        standardDMD.getNextDecision(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getNextDecisionIf' (EDirection direction, EDirection lastDirection, EDecision decision) {

        given:
        standardDMD.lastDirection = lastDirection
        EDecision newDecision = standardDMD.getNextDecisionIf(direction)

        expect:
        newDecision == decision

        where:
        direction | lastDirection | decision
        EDirection.NONE | EDirection.NONE | EDecision.DECREASE_DELTA
        EDirection.DIRECT | EDirection.NONE | EDecision.INCREASE_DELTA
        EDirection.INDIRECT | EDirection.NONE | EDecision.INCREASE_DELTA
        EDirection.DIRECT | EDirection.INDIRECT | EDecision.DECREASE_DELTA
        EDirection.INDIRECT | EDirection.DIRECT | EDecision.DECREASE_DELTA
        EDirection.DIRECT | EDirection.DIRECT | EDecision.INCREASE_DELTA
        EDirection.INDIRECT | EDirection.INDIRECT | EDecision.INCREASE_DELTA
        EDirection.NONE | EDirection.INDIRECT | EDecision.DECREASE_DELTA
        EDirection.NONE | EDirection.DIRECT | EDecision.DECREASE_DELTA
        
    }
}
