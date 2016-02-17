/*
 * #%L
 * agent-logging
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
package fr.irit.smac.libs.tooling.logging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.Discriminator;
import fr.irit.smac.libs.tooling.logging.AgentLog;

public class LoggerNameDiscriminator implements Discriminator<ILoggingEvent> {

    private boolean started = false;

    @Override
    public boolean isStarted() {
        return this.started;
    }

    @Override
    public void start() {
        this.started = true;
    }

    @Override
    public void stop() {
        this.started = false;
    }

    @Override
    public String getDiscriminatingValue(ILoggingEvent logEvent) {
        return logEvent.getLoggerName().substring(AgentLog.ROOT_AGENT_LOGGER.length()+1);
    }

    @Override
    public String getKey() {
        return "loggerName";
    }

}
