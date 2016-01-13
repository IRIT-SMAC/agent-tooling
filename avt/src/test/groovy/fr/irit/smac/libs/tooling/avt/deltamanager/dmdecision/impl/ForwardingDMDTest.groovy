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
package fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision.EDecision

@Unroll
public class ForwardingDMDTest extends Specification {

    @Shared ForwardingDMD forwardingDMD

    def setupSpec() {
        forwardingDMD = new ForwardingDMD(new StandardDMD())
        forwardingDMD.dmDecison.lastDirection = EDirection.NONE
    }

    def 'ForwardingDMD' () {

        given:
        ForwardingDMD forwardingDMD2

        when:
        forwardingDMD2 = new ForwardingDMD(Mock(IDMDecision))

        then:
        forwardingDMD2 != null
    }

    def 'ForwardingDMD with a null argument should return an IllegalArgumentException' () {

        given:
        ForwardingDMD forwardingDMD2

        when:
        forwardingDMD2 = new ForwardingDMD(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getNextDecision' () {

        when:
        EDecision decision = forwardingDMD.getNextDecision(EDirection.DIRECT)

        then:
        decision != null
    }

    def 'getNextDecisionIf' () {

        when:
        EDecision decision = forwardingDMD.getNextDecisionIf(EDirection.DIRECT)

        then:
        decision != null
    }
}
