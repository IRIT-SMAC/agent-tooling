package fr.irit.smac.libs.tooling.logging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.Discriminator;

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
		return logEvent.getLoggerName();
	}

	@Override
	public String getKey() {
		return "loggerName";
	}

}
