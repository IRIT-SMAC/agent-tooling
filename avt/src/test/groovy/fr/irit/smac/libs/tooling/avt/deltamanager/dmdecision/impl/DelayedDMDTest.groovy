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
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecisionFactory
import fr.irit.smac.libs.tooling.avt.deltamanager.dmdecision.IDMDecision.EDecision

@Unroll
class DelayedDMDTest extends Specification {

    @Shared DelayedDMD delayedDMD

    def 'DelayedDMD' () {

        when:
        delayedDMD = new DelayedDMDFactory(new StandardDMDFactory(), 1, 1).createInstance()

        then:
        delayedDMD != null
    }

    def 'isIncreaseAllowed' (int increaseDelay, int decreaseDelay, boolean allowed) {

        given:
        delayedDMD = new DelayedDMDFactory(new StandardDMDFactory(), increaseDelay, decreaseDelay).createInstance()

        boolean allowedOrNot = delayedDMD.isIncreaseAllowed()

        expect:
        allowedOrNot == allowed

        where:
        increaseDelay | decreaseDelay | allowed
        4 | 1 | false
        -1 | 1 | true
    }

    def 'isDecreaseAllowed' (int increaseDelay, int decreaseDelay, boolean allowed) {

        given:
        delayedDMD = new DelayedDMDFactory(new StandardDMDFactory(), increaseDelay, decreaseDelay).createInstance()
        boolean allowedOrNot = delayedDMD.isDecreaseAllowed()

        expect:
        allowedOrNot == allowed

        where:
        increaseDelay | decreaseDelay | allowed
        1 | 4 | false
        1 | -1 | true
    }

    def 'isIncreaseAllowedIf' (int increaseDelay, int decreaseDelay, boolean allowed) {

        given:
        delayedDMD = new DelayedDMDFactory(new StandardDMDFactory(), increaseDelay, decreaseDelay).createInstance()

        boolean allowedOrNot = delayedDMD.isIncreaseAllowedIf()

        expect:
        allowedOrNot == allowed

        where:
        increaseDelay | decreaseDelay | allowed
        4 | 1 | false
        -1 | 1 | true
    }

    def 'isDecreaseAllowedIf' (int increaseDelay, int decreaseDelay, boolean allowed) {

        given:
        delayedDMD = new DelayedDMDFactory(new StandardDMDFactory(), increaseDelay, decreaseDelay).createInstance()

        boolean allowedOrNot = delayedDMD.isDecreaseAllowedIf()

        expect:
        allowedOrNot == allowed

        where:
        increaseDelay | decreaseDelay | allowed
        1 | 4 | false
        1 | -1 | true
    }

    def 'getNextDecision' (EDirection direction, EDirection lastDirection, EDecision decision, int increaseDelay, int decreaseDelay) {

        given:
        StandardDMD nestedDmd = new StandardDMD()
        delayedDMD = new DelayedDMD(nestedDmd, increaseDelay, decreaseDelay)
        nestedDmd.lastDirection = lastDirection
        EDecision newDecision = delayedDMD.getNextDecision(direction)

        expect:
        newDecision == decision

        where:
        direction | lastDirection | decision | increaseDelay | decreaseDelay
        EDirection.NONE | EDirection.NONE | EDecision.SAME_DELTA | 1 | 1
        EDirection.DIRECT | EDirection.DIRECT | EDecision.INCREASE_DELTA | 0 | 1
        EDirection.DIRECT | EDirection.INDIRECT | EDecision.DECREASE_DELTA | 1 | 0
        EDirection.DIRECT | EDirection.DIRECT | EDecision.SAME_DELTA | 1 | 1
        EDirection.DIRECT | EDirection.INDIRECT | EDecision.SAME_DELTA | 1 | 1
    }

    def 'getNextDecision returns SAME_DELTA' () {

        given:
        StandardDMD nestedDmd = Spy( StandardDMD)
        delayedDMD = Spy (DelayedDMD, constructorArgs: [nestedDmd, 1, 1])

        when:
        nestedDmd.getNextDecision(EDirection.DIRECT) >> EDecision.SAME_DELTA

        then:
        delayedDMD.getNextDecision(EDirection.DIRECT) == EDecision.SAME_DELTA
    }

    def 'getNextDecision should throw an IllegalArgumentException' () {

        given:
        delayedDMD = new DelayedDMDFactory(new StandardDMDFactory(), 1, 1).createInstance()

        when:
        delayedDMD.getNextDecision(null)

        then:
        thrown(IllegalArgumentException)
    }

    def 'getNextDecisionIf' (EDirection direction, EDirection lastDirection, EDecision decision, int increaseDelay, int decreaseDelay, int requestCount) {

        given:
        StandardDMD nestedDmd = new StandardDMD()
        delayedDMD = new DelayedDMD(nestedDmd, increaseDelay, decreaseDelay)
        nestedDmd.lastDirection = lastDirection
        delayedDMD.decreaseRequestCount = requestCount
        delayedDMD.increaseRequestCount = requestCount
        EDecision newDecision = delayedDMD.getNextDecisionIf(direction)

        expect:
        newDecision == decision

        where:
        direction | lastDirection | decision | increaseDelay | decreaseDelay | requestCount
        EDirection.DIRECT | EDirection.DIRECT | EDecision.INCREASE_DELTA | 0 | 1 | 0
        EDirection.DIRECT | EDirection.INDIRECT | EDecision.DECREASE_DELTA | 1 | 0 | 0
        EDirection.DIRECT | EDirection.DIRECT | EDecision.SAME_DELTA | 1 | 0 | -5
        EDirection.DIRECT | EDirection.INDIRECT | EDecision.SAME_DELTA | 1 | 0 | -5
    }

    def 'getNextDecisionIf should throw an IllegalArgumentException' () {

        given:
        delayedDMD = new DelayedDMDFactory(new StandardDMDFactory(), 1, 1).createInstance()

        when:
        delayedDMD.getNextDecisionIf(null)

        then:
        thrown(IllegalArgumentException)
    }
}
