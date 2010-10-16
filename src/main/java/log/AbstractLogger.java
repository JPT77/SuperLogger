package log;


// Copyright (c) 2004-2006 Graham Lea. All rights reserved.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

abstract class AbstractLogger {

	/**
	 * The log that this logger logs to.
	 */
	protected final SimpleLog log;

	/**
	 * The class that this SimpleLogger instance belongs to.
	 */
	private final Class sourceClass;

	/**
	 * The fully-qualified name of the class that this SimpleLogger instance
	 * belongs to.
	 */
	protected final String className;

	/**
	 * <p>
	 * The name used to specify and look up the configuration of this
	 * <code>SimpleLogger</code>.
	 * </p>
	 *
	 * <p>
	 * The config name is the fully-qualified name of the source class of the
	 * logger, plus, if the logger is an instance logger, a period followed by
	 * the string representation ({@link Object#toString() toString()}) of the
	 * logger's instance ID. Additionally, any dollar signs appearing in the
	 * name are changed to periods.
	 * </p>
	 */
	private String configName;

	/**
	 * The current debug level for this <code>SimpleLogger</code>.
	 */
	protected LogLevel debugLevel;

	/**
	 * Whether this logger is currently reporting tracing or not.
	 */
	private boolean tracing;

	private boolean isInstanceDebugger;

	/**
	 * <p>
	 * Creates a new <code>SimpleLogger</code> for the specified Class that will
	 * log to, and be configured by, the provided <code>SimpleLog</code>.
	 * </p>
	 *
	 * @param log
	 *            the <code>SimpleLog</code> instance that this
	 *            <code>SimpleLogger</code> should log to and be configured by.
	 *
	 * @param sourceClass
	 *            The class that this SimpleLogger object is for, the name of
	 *            which will be printed out as part of every message.
	 *
	 * @param instanceId
	 *            An object uniquely identifying the instance of
	 *            <code><i>sourceClass</i></code> that this
	 *            <code>SimpleLogger</code> is specifically for. Can be
	 *            <code>null</code> if this <code>SimpleLogger</code> instance
	 *            is for all instances of <code><i>sourceClass</i></code> rather
	 *            than for a particular instance (this is the norm).
	 *
	 * @param useLongName
	 *            A boolean indicating whether the fully-qualified name of the
	 *            specified class should be printed in each message (when set to
	 *            <code>true</code>), or just the name of the class within it's
	 *            package (ie. the package will not be printed when set to
	 *            <code>false</code>).
	 */
	protected AbstractLogger(SimpleLog log, Class sourceClass, Object instanceId) {
		this.log = log;
		this.sourceClass = sourceClass;
		String classname = sourceClass.getName();
		this.configName = classname;
		this.isInstanceDebugger = instanceId != null;
		if (isInstanceDebugger) {
			classname = classname + "[" + instanceId + "]";
		}
		this.className = classname;
		if (configName.indexOf('$') != -1) {
			configName = configName.replace('$', '.');
		}

		log.register(this);
	}

	protected boolean shouldCreateMessage() {
		return log.isOutputting();
	}


	/**
	 * <p>
	 * Returns this <code>SimpleLogger</code>'s config name, the name used to
	 * specify and look up its configuration.
	 * </p>
	 *
	 * <p>
	 * Currently, the config name is the fully-qualified name of the source
	 * class of the logger, plus, if the logger is an instance logger, a period
	 * ('.') followed by the string representation ({@link Object#toString()
	 * toString()}) of the logger's instance ID. Additionally, any dollar signs
	 * appearing in the name are changed to periods.
	 * </p>
	 */
	protected String getConfigName() {
		return configName;
	}

	/**
	 * Returns the current debug level of this <code>SimpleLogger</code>.
	 */
	protected LogLevel getDebugLevel() {
		return debugLevel;
	}

	/**
	 * Returns the source class of this <code>SimpleLogger</code>.
	 */
	protected Class getSourceClass() {
		return sourceClass;
	}

	/**
	 * Returns whether this <code>SimpleLogger</code> is currently reporting
	 * tracing or not.
	 *
	 * @return <code>true</code> if this logger is reporting tracing, false
	 *         otherwise.
	 */
	protected boolean isTracing() {
		return tracing;
	}

	/**
	 * A convenience method for testing whether a message logged at the
	 * specified level would be logged by this <code>SimpleLogger</code>.
	 *
	 * @param level
	 *            the level of the hypothetical message being logged
	 *
	 * @return <code>true</code> if a message at the specified level would be
	 *         logged given this <code>SimpleLogger</code>'s current log level,
	 *         <code>false</code> if it would not.
	 *
	 * @see #getDebugLevel
	 * @see DebugLevel#shouldLog
	 */
	// public boolean
	// wouldLog(DebugLevel level)
	// {
	// return log.isOutputting() && getDebugLevel().shouldLog(level);
	// }

	/**
	 * Sets the current debug level of this <code>SimpleLogger</code>.
	 *
	 * @param debugLevel
	 *            the new debug level
	 *
	 * @throws IllegalArgumentException
	 *             if <code>debugLevel</code> is <code>null</code>.
	 */
	void setDebugLevel(LogLevel debugLevel) {
		if (debugLevel == null) {
			throw new IllegalArgumentException("debugLevel cannot be null.");
		}
		this.debugLevel = debugLevel;
	}

	/**
	 * Sets this <code>SimpleLogger</code>'s tracing flag.
	 *
	 * @param tracing
	 *            <code>true</code> if this logger should report tracing, false
	 *            if it should not.
	 */
	void setTracing(boolean tracing) {
		this.tracing = tracing;
	}

	protected void logMessage(LogMessage msg) {
		log.println(msg.toString());
	}

	/**
	 * Returns <code>true</code> if this <code>SimpleLogger</code> is a logger for one instance of
	 * the source class rather than for the class as a whole.
	 */
	public boolean
	isInstanceDebugger()
	{
		return isInstanceDebugger;
	}

}
