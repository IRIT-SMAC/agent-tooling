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
package fr.irit.smac.libs.tooling.logging;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.sift.SiftingAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.sift.AppenderFactory;
import fr.irit.smac.libs.tooling.logging.AgentLog.ILogItemDisplayData.ESide;
import fr.irit.smac.libs.tooling.logging.logback.LoggerNameDiscriminator;

/**
 * Class designed to satisfy simple agent logging needs. Actually, it uses the
 * logback library.
 * 
 * @author lemouzy, perles
 * 
 */
public class AgentLog {

    private AgentLog() {

    }

    /**
     * Interface that helps to configure the way elements of logs are displayed
     * (format, order, justification, size, etc.)
     * 
     * @author lemouzy, perles
     */
    public static interface ILogItemDisplayData {
        public enum ESide {
            LEFT, RIGHT
        }

        /**
         * Set if the element is displayed or not
         * 
         * @param displayed
         * @return the log item display configuration helper
         */
        public ILogItemDisplayData displayed(boolean displayed);

        /**
         * Set the priority oder of the element, the elements with small numbers
         * are displayed at first (at the beginning of the line)
         * 
         * @param order
         * @return the log item display configuration helper
         */
        public ILogItemDisplayData order(int order);

        /**
         * Set the prefix to be added before the logItem value
         * 
         * @param prefix
         * @return the log item display configuration helper
         */
        public ILogItemDisplayData prefix(String prefix);

        /**
         * Set the suffix to be added after the logItem value
         * 
         * @param suffix
         * @return the log item display configuration helper
         */
        public ILogItemDisplayData suffix(String suffix);

        /**
         * Sets the minimal number of characters to display, if the element size
         * is less than minSize then the logger will fill the remaining
         * characters with spaces See align to configure whether the spaces are
         * added at the beginning of the string (right justification) or at the
         * end (left justification)
         * 
         * @param minSize
         * @return the log item display configuration helper
         */
        public ILogItemDisplayData minSize(Integer minSize);

        /**
         * Sets the maximal number of characters to display, if the element size
         * is greater than maxSize then the logger will trucate the remaining
         * characters See truncatedSide to configure whether the caracters are
         * truncated at the left side or at the right side of the string
         * 
         * @param maxSize
         * @return the log item display configuration helper
         */
        public ILogItemDisplayData maxSize(Integer maxSize);

        /**
         * Sets the text justification side when minSide is greater than the
         * actual string lenght to display
         * 
         * @param align
         * @return
         */
        public ILogItemDisplayData align(ESide align);

        /**
         * Sets the text truncation side when the string length to display is
         * greater than maxSize eg. "trophoblaste" with maxSize of 6 will be
         * displayed as follows : - with Side.RIGHT "tropho" - with Side.LEFT
         * "blaste"
         * 
         * @param align
         * @return
         */
        public ILogItemDisplayData truncatedSide(ESide align);

        /**
         * Convenience method to get the parent logger initializer to ease
         * chaining method call for initialization
         * 
         * @return
         */
        public Initializer parent();
    }

    public static class Initializer {

        private boolean                      clearLogFolderOnStartup;
        private String                       logFolderName;
        private String                       customLogbackConfigurationFile;
        private Level                        logLevel;
        private boolean                      logEnabled;

        private String                       logPatternLayout;
        private String                       logItemSeparator;

        private final LogItemDisplayDataImpl cycleNumberDisplayData;
        private final LogItemDisplayDataImpl logLevelDisplayData;
        private final LogItemDisplayDataImpl markerDisplayData;
        private final LogItemDisplayDataImpl messageDisplayData;

        private Initializer() {
            this.clearLogFolderOnStartup = false;
            this.logFolderName = "log";
            this.customLogbackConfigurationFile = null;
            this.logLevel = Level.DEBUG;
            this.logEnabled = true;
            this.logPatternLayout = null;
            this.logItemSeparator = " ";

            this.cycleNumberDisplayData = new LogItemDisplayDataImpl(
                "property{amasStepNumber}", true, 1, 3, null, ESide.RIGHT, ESide.RIGHT, "", "", this);

            this.logLevelDisplayData = new LogItemDisplayDataImpl(
                "level", true, 2, 5, null, ESide.LEFT, ESide.RIGHT, "", "", this);

            this.markerDisplayData = new LogItemDisplayDataImpl(
                "marker", false, 3, 6, null, ESide.LEFT, ESide.RIGHT, "", "", this);

            this.messageDisplayData = new LogItemDisplayDataImpl(
                "msg", true, 4, null, null, ESide.LEFT, ESide.RIGHT, "", "", this);
        }

        /**
         * Default value: false
         * 
         * @param clearLogFolderOnStartup
         *            if true then the log folder and all its content is deleted
         *            (Use with caution !)
         * @return
         */
        public Initializer clearLogFolderOnStartup(boolean clearLogFolderOnStartup) {
            this.clearLogFolderOnStartup = clearLogFolderOnStartup;
            return this;
        }

        /**
         * Sets up the log folder name Default value: "log"
         * 
         * @param logFolderName
         * @return
         */
        public Initializer logFolderName(String logFolderName) {
            this.logFolderName = logFolderName;
            return this;
        }

        /**
         * Sets up the minimum log level to be rendered in log files. Here is
         * the log levels rendered with : - Level.ALL => Everything - Level.OFF
         * => Nothing - Level.ERROR => Level.ERROR - Level.WARN => Level.ERROR,
         * Level.WARN - Level.INFO => Level.ERROR, Level.WARN, Level.INFO -
         * Level.DEBUG => Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG -
         * Level.TRACE => Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG,
         * Level.TRACE
         * 
         * (if Level.OFF the log folder will not be cleared even if
         * clearLogFolderOnStartup = true) Default value : Level.DEBUG
         * 
         * @param logLevel
         * @return
         */
        public Initializer logLevel(Level logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        /**
         * Default value : true Enable/Disable logging (if false the log folder
         * will not be cleared even if clearLogFolderOnStartup = true)
         * 
         * @param logEnabled
         * @return
         */
        public Initializer logEnabled(boolean logEnabled) {
            this.logEnabled = logEnabled;
            return this;
        }

        /**
         * Default value : null
         * 
         * Sets a custom log pattern layout used to generate log lines If null,
         * then the pattern layout is generated from informations contained in -
         * AgentLog.initilizer().cycleNumber() -
         * AgentLog.initilizer().logLevel() - AgentLog.initilizer().marker() -
         * AgentLog.initilizer().message()
         * 
         * If not null then the above informations are ignored an loggers will
         * use directly the logPatternLayout value
         * 
         * For example a logPatternLayout could be: %property{amasStepNumber}
         * %level %marker %msg
         * 
         * see http://logback.qos.ch/manual/layouts.html#ClassicPatternLayout
         * for more information about pattern Layouts
         * 
         * @param logPatternLayout
         * @return
         */
        public Initializer logPatternLayout(String logPatternLayout) {
            this.logPatternLayout = logPatternLayout;
            return this;
        }

        /**
         * Default value : null
         * 
         * Sets a custom logback configuration file If not null then logback
         * will be only configured from the given configuration file All the
         * other configuration are ignored but the clear log folder parameter
         * 
         * @param customLogbackConfigurationFile
         * @return
         */
        public Initializer customLogbackConfigurationFile(String customLogbackConfigurationFile) {
            this.customLogbackConfigurationFile = customLogbackConfigurationFile;
            return this;
        }

        /**
         * Sets the sequence of characters that are used to separate the log
         * items e.g. - if logItemSeparator = "-" then the log output will be
         * 10-ERROR-Don't pass ! - if logItemSeparator = " " then the log output
         * will be 10 ERROR Don't pass !
         * 
         * @param logItemSeparator
         * @return
         */
        public Initializer logItemSeparator(String logItemSeparator) {
            // pay caution to escape special characters used by PatternLayout
            this.logItemSeparator = escapePatternLayoutSpecialCharacters(logItemSeparator);
            return this;
        }

        /**
         * @return the configuration of the step number display format
         */
        public ILogItemDisplayData stepNumberDisplay() {
            return this.cycleNumberDisplayData;
        }

        /**
         * @return the configuration of the logLevel display format
         */
        public ILogItemDisplayData logLevelDisplay() {
            return this.logLevelDisplayData;
        }

        /**
         * @return the configuration of the marker display format
         */
        public ILogItemDisplayData markerDisplay() {
            return this.markerDisplayData;
        }

        /**
         * @return the configuration of the agent message display format
         */
        public ILogItemDisplayData messageDisplay() {
            return this.messageDisplayData;
        }

        /**
         * Method called in order to effectivelly initialize and configure the
         * logging system
         */
        public void initialize() {
            this.checkParameters();

            if (this.logPatternLayout == null || this.logPatternLayout.isEmpty()) {
                this.logPatternLayout = this.computeLogPatternLayout();
            }

            if (this.clearLogFolderOnStartup && this.logEnabled && this.logLevel != Level.OFF) {
                AgentLog.clearFolderContent(logFolderName);
            }

            if (this.customLogbackConfigurationFile != null && !this.customLogbackConfigurationFile.isEmpty()) {
                AgentLog.initialiseLogback(this.customLogbackConfigurationFile);
            }
            else {
                AgentLog.initialiseLogback(this.logFolderName, this.logLevel, this.logPatternLayout);
            }

            AgentLog.configureLoggerContextProperties(this.logFolderName);
        }

        /**
         * Method used to check the consistency of the configuration parameters
         */
        private void checkParameters() {
            if (this.logFolderName.isEmpty()) {
                throw new IllegalArgumentException("Log folder name is empty !");
            }

            if (this.customLogbackConfigurationFile != null) {
                File f = new File(this.customLogbackConfigurationFile);
                if (!f.exists() || !f.isFile() || !f.canRead()) {
                    throw new IllegalArgumentException("Invalid custom Logback configuration file !");
                }
            }

            if (this.logLevel == Level.OFF) {
                this.logEnabled = false;
            }

            if (!this.logEnabled) {
                this.logLevel = Level.OFF;
            }
        }

        /**
         * @return the log pattern layout computed from infomations contained
         *         in: - AgentLog.initilizer().cycleNumber() -
         *         AgentLog.initilizer().logLevel() -
         *         AgentLog.initilizer().marker() -
         *         AgentLog.initilizer().message()
         */
        private String computeLogPatternLayout() {
            StringBuilder sb = new StringBuilder();

            for (LogItemDisplayDataImpl logItemData : this.getSortedLogItemDisplayData()) {
                if (logItemData.displayed) {
                    if (sb.length() != 0) {
                        sb.append(this.logItemSeparator);
                    }
                    sb.append(logItemData.generatePatternLayoutString());
                }
            }
            sb.append("%n");
            return sb.toString();
        }

        private List<LogItemDisplayDataImpl> getSortedLogItemDisplayData() {
            List<LogItemDisplayDataImpl> logItemDatas = Arrays.asList(new LogItemDisplayDataImpl[] {
                this.cycleNumberDisplayData, this.markerDisplayData, this.logLevelDisplayData, this.messageDisplayData
            });

            Collections.sort(logItemDatas, new Comparator<LogItemDisplayDataImpl>() {
                @Override
                public int compare(LogItemDisplayDataImpl o1,
                    LogItemDisplayDataImpl o2) {
                    return o1.order - o2.order;
                }
            });

            return logItemDatas;
        }
    }

    /**
     * @return the initializer that helps configure the logging system
     */
    public static Initializer initializer() {
        return new Initializer();
    }

    /**
     * Sets the amasStepNumber
     * 
     * @param stepNumber
     */
    public static void setAmasStepNumber(int stepNumber) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.putProperty("amasStepNumber", Integer.toString(stepNumber));
    }

    /**
     * Sets up the minimum log level to be rendered in log files. Here is the
     * log levels rendered with : - Level.ALL => Everything - Level.OFF =>
     * Nothing - Level.ERROR => Level.ERROR - Level.WARN => Level.ERROR,
     * Level.WARN - Level.INFO => Level.ERROR, Level.WARN, Level.INFO -
     * Level.DEBUG => Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG -
     * Level.TRACE => Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG,
     * Level.TRACE
     * 
     * @param logLevel
     *            the logLevel
     */
    private static void setLogLevel(Level logLevel) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(
            ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

        root.setLevel(logLevel);
    }

    /**
     * Return the logger instance corresponding to the given name
     * 
     * @param loggerName
     * @return
     */
    public static Logger getLogger(String loggerName) {
        return LoggerFactory.getLogger(loggerName);
    }

    /**
     * Return the marker corresponding to the given name
     * 
     * @param name
     * @return
     */
    public static Marker getMarker(String name) {
        return MarkerFactory.getMarker(name);
    }

    /**
     * Sets up the following properties in the main LoggerContext -
     * ${loggerName} - ${logFolderName}
     * 
     * @param logFolderName
     */
    private static void configureLoggerContextProperties(final String logFolderName) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.putProperty("logFolderName", logFolderName);
        lc.putProperty("amasStepNumber", "");
    }

    /**
     * Initialise logback just form a custom configuration file the folowing
     * properties can be used in this configuration file : - ${loggerName} -
     * ${logFolderName}
     * 
     * @param customLogbackConfigurationFile
     */
    private static void initialiseLogback(final String customLogbackConfigurationFile) {
        System.setProperty("logback.configurationFile", customLogbackConfigurationFile);
    }

    /**
     * the initialization method that configure the file appender
     * 
     * @param logFolderName
     * @param clearFolderContent
     * @param customLogbackConfigurationFile
     * @param logLevel
     * @param logTemplate
     */
    private static void initialiseLogback(
        final String logFolderName,
        final Level logLevel,
        final String logTemplate) {
        // get root logger and clear all existing appenders
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory
            .getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.detachAndStopAllAppenders();

        // Configure and create a sifting encoder

        LoggerNameDiscriminator loggerNameDiscriminator = new LoggerNameDiscriminator();
        loggerNameDiscriminator.start();

        SiftingAppender appender = new SiftingAppender();
        appender.setDiscriminator(loggerNameDiscriminator);
        appender.setContext(lc);
        appender.setAppenderFactory(new AgentFilesAppenderFactory(logFolderName, logTemplate));
        appender.start();

        // add the created appender
        rootLogger.addAppender(appender);

        // set log level
        setLogLevel(logLevel);
    }

    /**
     * Factory used by the sfting appender used by default by the logging system
     * 
     * @author lemouzy
     */
    private static class AgentFilesAppenderFactory implements AppenderFactory<ILoggingEvent> {

        private final String logFolderName;
        private final String logTemplate;

        private AgentFilesAppenderFactory(String logFolderName, String logTemplate) {
            this.logFolderName = logFolderName;
            this.logTemplate = logTemplate;
        }

        @Override
        public Appender<ILoggingEvent> buildAppender(Context logcingContext, String fileName) throws JoranException {
            // first sets up the paterns for log layout
            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setPattern(logTemplate);
            encoder.setContext(logcingContext);
            encoder.start();
            // then create a file appender for the given filename
            FileAppender<ILoggingEvent> appender = new FileAppender<ILoggingEvent>();
            appender.setContext(logcingContext);
            appender.setAppend(false);
            appender.setFile(logFolderName + "/" + fileName + ".log");
            appender.setEncoder(encoder);
            appender.start();

            return appender;
        }
    }

    /**
     * Clear log folder content
     * 
     * @param folder
     */
    private static void clearFolderContent(String folder) {
        File f = new File(folder);
        if (!f.isDirectory()) {
            f.delete();
        }
        if (f.exists() && f.isDirectory()) {
            deleteAllIn(f);
            f.delete();
        }
    }

    private static void deleteAllIn(File dir) {
        for (File f : dir.listFiles()) {
            if (!f.isHidden()) {
                if (f.isDirectory()) {
                    deleteAllIn(f);
                    if (f.listFiles().length <= 2) {
                        f.delete();
                    }
                }
                else {
                    f.delete();
                }
            }
        }
    }

    private static String escapePatternLayoutSpecialCharacters(String stringToEscape) {
        return stringToEscape.replaceAll("([%\\(\\)])", "\\\\$1");
    }

    /**
     * The class that implements logItemDisplayData and which is used to create
     * the formating template string for a log item
     * 
     * @author lemouzy
     */
    private static class LogItemDisplayDataImpl implements ILogItemDisplayData {

        private final String      key;
        private boolean           displayed;
        private int               order;
        private Integer           minSize;       // can be null
        private Integer           maxSize;       // can be null
        private ESide             align;         // can be null
        private ESide             truncatedSide; // can be null
        private String            prefix;
        private String            suffix;
        private final Initializer initialiser;

        private LogItemDisplayDataImpl(String key, boolean displayed, int order,
            Integer minSize, Integer maxSize, ESide align, ESide truncatedSide, String prefix, String suffix,
            Initializer initializer) {
            super();
            this.key = key;
            this.displayed = displayed;
            this.order = order;
            this.minSize = minSize;
            this.maxSize = maxSize;
            this.align = align;
            this.truncatedSide = truncatedSide;
            this.prefix = prefix;
            this.suffix = suffix;
            this.initialiser = initializer;
        }

        @Override
        public ILogItemDisplayData displayed(boolean displayed) {
            this.displayed = displayed;
            return this;
        }

        @Override
        public ILogItemDisplayData order(int order) {
            this.order = order;
            return this;
        }

        @Override
        public ILogItemDisplayData minSize(Integer minSize) {
            this.minSize = minSize;
            return this;
        }

        @Override
        public ILogItemDisplayData maxSize(Integer maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        @Override
        public ILogItemDisplayData align(ESide align) {
            this.align = align;
            return this;
        }

        @Override
        public ILogItemDisplayData truncatedSide(ESide truncatedSide) {
            this.truncatedSide = truncatedSide;
            return this;
        }

        @Override
        public ILogItemDisplayData prefix(String prefix) {
            this.prefix = AgentLog.escapePatternLayoutSpecialCharacters(prefix);
            return this;
        }

        @Override
        public ILogItemDisplayData suffix(String suffix) {
            this.suffix = AgentLog.escapePatternLayoutSpecialCharacters(suffix);
            return this;
        }

        /**
         * generate a formating pattern layout string from the information in
         * this instance (see
         * http://logback.qos.ch/manual/layouts.html#ClassicPatternLayout)
         * 
         * @return
         */
        private String generatePatternLayoutString() {
            return this.prefix + "%"
                + (this.minSize != null ? (this.align == ESide.LEFT || this.align == null ? "-" : "") + this.minSize
                    : "")
                + (this.maxSize != null ? "." + (this.truncatedSide == ESide.RIGHT ? "-" : "") + this.maxSize : "")
                + this.key + (this.key.contains("{") || this.key.contains("}") ? "" : "{}")
                + this.suffix;
        }

        @Override
        public Initializer parent() {
            return this.initialiser;
        }

    }
}
