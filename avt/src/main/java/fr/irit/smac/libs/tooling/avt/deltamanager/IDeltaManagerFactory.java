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
package fr.irit.smac.libs.tooling.avt.deltamanager;

import fr.irit.smac.libs.tooling.avt.range.IRange;

/**
 * A factory for creating IDeltaManager objects.
 *
 * @param <T> the generic type
 */
public interface IDeltaManagerFactory<T extends IDeltaManager> {

    /**
     * Instanciate a DeltaManager that will be used for the given AVT.
     *
     * @param range the range
     * @return the t
     */
    T createInstance(IRange range);

}
