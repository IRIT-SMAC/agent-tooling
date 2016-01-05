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

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import fr.irit.smac.libs.tooling.avt.deltamanager.IDeltaManager.EDirection
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision.EDecision
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.ForwardingDMD
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.impl.StandardDMD

@Unroll
public class ForwardingDMDTest extends Specification {

    @Shared ForwardingDMD forwardingDMD

    def setupSpec() {
        forwardingDMD = new ForwardingDMD(new StandardDMD())
        forwardingDMD.dmDecison.lastDirection = EDirection.NONE
    }

    def 'ForwardingDMD' () {

        given:
        ForwardingDMD forwardingDMDConstructor

        when:
        forwardingDMDConstructor = new ForwardingDMD(Mock(IDMDecision))

        then:
        forwardingDMDConstructor != null
    }

    def 'ForwardingDMD with a null argument should return an IllegalArgumentException' () {

        given:
        ForwardingDMD forwardingDMDConstructor

        when:
        forwardingDMDConstructor = new ForwardingDMD(null)

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
