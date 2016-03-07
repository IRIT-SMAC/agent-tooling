/*
 * #%L
 * agent-scheduling
 * %%
 * Copyright (C) 2014 - 2016 IRIT - SMAC Team
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
package fr.irit.smac.libs.tooling.scheduling.contrib.twosteps;

/**
 * The Class BadStepRuntimeException.
 */
public class BadStepRuntimeException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new bad step runtime exception.
     *
     * @param message the message
     */
    public BadStepRuntimeException(String message) {
        super(message);
    }

}