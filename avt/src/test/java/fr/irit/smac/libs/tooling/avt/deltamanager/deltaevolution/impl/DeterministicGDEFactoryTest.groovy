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

import spock.lang.Specification
import fr.irit.smac.libs.tooling.avt.deltamanager.deltaevolution.IGeometricDE

class DeterministicGDEFactoryTest extends Specification{

    def 'UndeterministicGDEFactory' () {

        when:
        DeterministicGDEFactory deterministicGDEFactory = new DeterministicGDEFactory(1, 3)

        then:
        deterministicGDEFactory != null
    }

    def 'createInstance' () {

        given:
        DeterministicGDEFactory deterministicGDEFactory = new DeterministicGDEFactory(1, 3)

        when:
        IGeometricDE deterministicGDE = deterministicGDEFactory.createInstance()

        then:
        deterministicGDE != null
    }
}
