package fr.irit.smac.libs.tooling.logging.example;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.Marker;

import ch.qos.logback.classic.Level;
import fr.irit.smac.libs.tooling.logging.AgentLog;

public class AgentLoggingExample {
	
	public static void main(String[] args) {
		
		// initisalisation of the logging system
		AgentLog.initializer() // all the above lines are optional except the last one : .initialize();
			.logFolderName("log")          // the name of the folder containing log files (default "log")
			.clearLogFolderOnStartup(true) // if true, all the content of the log folder  (default true)
										   // will be cleared at each initialization
//			.customLogbackConfigurationFile("./custom-conf-logback.xml") // if not set, the default configuration is set (default null)
															             // if not null then override all the following configuration lines
			.logEnabled(true)    // enable or disable logging (if disabled the log folder will not be cleared even if clearLogFolderOnStartup = true)
			.logLevel(Level.ALL) // sets the min log level to display
			                     // Level.OFF to disable logging
			                     // Level.ALL for all traces
			                     // by default Level.DEBUG
//			.logPatternLayout("%replace(%5property{amasStepNumber}){\"\\s\",\"0\"} > [%-6.6marker] %-7([%level]) %msg%n") // sets the output format of log lines (default value : null)
//																						         // if set to a non null value then override the
//																						         // behind configuration lines
//																						         // for more informations see http://logback.qos.ch/manual/layouts.html#ClassicPatternLayout
//			.logItemSeparator("...") // the separator between folowing elements
//			.stepNumberDisplay()
//				.minSize(6)
//				.align(Side.RIGHT) // justification side if the string length is lower than minSize
//				.displayed(true)   // whether it is displayed or not
//				.prefix("=>")      // sets the prefix to be displayed before the log element (by default prefix and suffix are empty strings)
//				.order(2)          // the order priority in the line (lower values are diplayed first)
//				.parent()		   // used to comme back to AgentLog.initializer() instance and configure other things
//				
//			.logLevelDisplay()
//				.align(Side.LEFT)
//				.displayed(true)
//				.minSize(5)
//				.order(1)
//				.prefix("(") // sets the prefix to be displayed before the log element
//				.suffix(")") // sets the suffix to be displayed after the log element
//				.parent()
//				
//			.markerDisplay() // markers are not displayed by default,
//					         // a marker is used to add more information, or a kind of log message type
//				.align(Side.LEFT)
//				.displayed(true)
//				.minSize(12)
//				.maxSize(12)
//				.truncatedSide(Side.LEFT) // truncation side when the string length to display is greater than maxSize
//				.prefix("{")
//				.suffix("}")
//				.order(3)
//				.parent()
//				
//			.messageDisplay()
//				.align(Side.LEFT)
//				.maxSize(70)
//				.truncatedSide(Side.RIGHT)
//				.order(4)
//				.parent()
				
			.initialize(); // initialize the logging system
		
		
		// initisalisation of the multi-agent system
		
		List<MyLoggingAgent> agents = new ArrayList<MyLoggingAgent>();
		agents.add(new MyLoggingAgent("A"));
		agents.add(new MyLoggingAgent("B"));
		
		// multi-agent system execution
		int nbSteps = 20;
		for (int stepNumber = 1 ; stepNumber <= nbSteps ; stepNumber++ ) {
			// set the step number to be displayed in the logs
			AgentLog.setAmasStepNumber(stepNumber);
			
			for (MyLoggingAgent agent : agents) {
				agent.perceiveDecideAndAct();
			}
		}
		
	}
	
	/**
	 * A Very simple Logging Agent
	 * 
	 * @author lemouzy
	 */
	public static class MyLoggingAgent {
		private final String name;
		private int traceLogCount;
		
		// logging stuff
		private final Logger logger;
		private final static Marker SNC1 = AgentLog.getMarker("SNC1");
		private final static Marker MY_NOMINAL_FUNCTION = AgentLog.getMarker("MY_NOMINAL_FUNCTION");
		
		public MyLoggingAgent(String name) {
			this.name = name;
			this.traceLogCount = 0;
			
			// Fetch the logger instance
			this.logger =  AgentLog.getLogger(name);
		}
		
		public void perceiveDecideAndAct() {
			
			this.logger.info("New step =======================================================");
			this.logger.info(SNC1, "My name is {} and \nI'm alive !!! \\o/",this.name);
			this.logger.warn(MY_NOMINAL_FUNCTION, "My nominal function seems to be useless :/");
			this.logger.debug("Executing the third log message");
			this.logger.trace(AgentLog.getMarker("ON_THE_FLY_MARKER"),"A trace log count : {}", this.traceLogCount++);
			this.logger.error("Trophoblastic malformed error in class {}", this.getClass());
		}
	}
}
