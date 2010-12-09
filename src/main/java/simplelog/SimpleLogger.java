package simplelog;

import de.jpt.superlogger.LogLevel;


// $Id: SimpleLogger.java,v 1.12 2006/07/13 12:40:06 grlea Exp $
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


/**
 * <p>Used to create log messages for a single class or instance of a class.</p>
 *
 * <p><code>SimpleLogger</code> is where it all happens, from a client's perspective.<br>
 * The easiest way to use Simple Log is to create one <code>SimpleLogger</code> for each class and
 * then log to it!</p>
 *
 * <p>1. Create a logger like this:<pre>
 *    private static final SimpleLogger log = new SimpleLogger(HelloWorld.class);
 * </pre></p>
 *
 * <p>2. And then use it like this:<pre>
 *       log.entry("main()");
 *       log.debugObject("argv", argv);
 *       if (argv.length == 0)
 *       {
 *          log.info("No arguments. Using defaults.");
 *          ...
 *       }
 *       ...
 *       log.exit("main()");
 * </pre></p>
 *
 * <p>
 * <code>SimpleLogger</code> provides for four types of log messages:
 * <ul>
 *    <li>
 *       "db" = Debug (see {@link #db SimpleLogger.db()})<br>
 *       Lets you log a simple log message, e.g. "Got to the point where you thought it wasn't
 *       getting to."
 *    </li><br><br>
 *    <li>
 *       "dbo" = Debug Object
 *       (see {@link #dbo(LogLevel,String,Object) SimpleLogger.dbo()})
 *       <br>
 *       Debugs the name and value of an object. Specially handled variants exist for all primitives,
 *       Object[], byte[] and char[].
 *    </li><br><br>
 *    <li>
 *       "dbe" = Debug Exception (see {@link #dbe SimpleLogger.dbe()})<br>
 *       Special handling of exceptions that will print a stack trace (can be turned off).
 *    </li><br><br>
 *    <li>
 *       Tracing (see {@link #entry SimpleLogger.entry()} and {@link #exit SimpleLogger.exit()})<br>
 *       Logs entry to and exit from a method. Can be turned on/off independent of debug level.<br>
 *    </li>
 * </ul>
 *
 * as well as convenience methods, named after the various levels, as shortcuts to the above methods.
 * </p>
 *
 * <p>
 * Instance-based <code>SimpleLogger</code>s can be used to determine, from the log output,
 * exactly which object a log statement is coming from. This is useful when many objects of the same
 * class are all performing logging and it is crucial to analysis to know which object logged any
 * given message.<br><br>
 *
 * Here is an example of how an instance-based logger would be used:<pre>
 * public class
 * Test
 * {
 *    private final SimpleLogger log;
 *
 *    private final String id;
 *
 *    public
 *    Test(String id)
 *    {
 *       log = new SimpleLogger(Test.class, id);
 *       this.id = id;
 *    }
 * }</pre>
 * Note the following important features of this pattern and instance-based logs in general:
 * <ul>
 * <li>The <code>log</code> field is <i>not</i> static. There is one per instance.</li>
 * <li><code>&lt;ClassName&gt;.class</code> is used rather than <code>getClass()</code>. Otherwise,
 * if this class were subclassed, logging statements would appear with the subclass's name as the
 * source.</li>
 * <li>There are separate log formats for instance-based logs in the properties file.</li>
 * </ul>
 * </p>
 *
 * @version $Revision: 1.12 $
 * @author $Author: grlea $
 */
public class SimpleLogger extends BasicLogger {
	
	/**
	 * <p>Creates a new <code>SimpleLogger</code> for the specified Class using the default
	 * <code>SimpleLog</code>.</p>
	 *
	 * <p>This constructor is the equivalent of calling:<br>
	 * {@link #SimpleLogger(SimpleLog,Class) new SimpleLogger(SimpleLog.defaultInstance(), sourceClass)}
	 * </p>
	 *
	 * @param sourceClass The class that this SimpleLogger object is for, the name of which will be
	 * printed out as part of every message.
	 */
	public SimpleLogger(Class sourceClass) {
		this(SimpleLog.defaultInstance(), sourceClass);
	}

	/**
	 * <p>Creates a new <code>SimpleLogger</code> for the specified Class using the default
	 * <code>SimpleLog</code>.</p>
	 *
	 * <p>This constructor is the equivalent of calling:<br>
	 * {@link #SimpleLogger(SimpleLog,Class,Object)
	 * new SimpleLogger(SimpleLog.defaultInstance(), sourceClass, instanceId)}
	 * </p>
	 *
	 * @param sourceClass The class that this SimpleLogger object is for, the name of which will be
	 * printed out as part of every message.
	 *
	 * @param instanceId An object uniquely identifying the instance of <code><i>sourceClass</i></code>
	 * that this <code>SimpleLogger</code> is specifically for. Can be <code>null</code> if this
	 * <code>SimpleLogger</code> instance is for all instances of <code><i>sourceClass</i></code>
	 * rather than for a particular instance (this is the norm).
	 */
	public SimpleLogger(Class sourceClass, Object instanceId) {
		this(SimpleLog.defaultInstance(), sourceClass, instanceId);
	}

	/**
	 * <p>Creates a new <code>SimpleLogger</code> for the specified Class that will log to, and be
	 * configured by, the provided <code>SimpleLog</code>.</p>
	 *
	 * <p>This constructor is the equivalent of calling:<br>
	 * {@link #SimpleLogger(SimpleLog,Class,boolean)
	 * new SimpleLogger(log, sourceClass, false)}
	 * </p>
	 *
	 * @param log the <code>SimpleLog</code> instance that this <code>SimpleLogger</code> should log
	 * to and be configured by.
	 *
	 * @param sourceClass The class that this SimpleLogger object is for, the name of which will be
	 * printed out as part of every message.
	 */
	public SimpleLogger(SimpleLog log, Class sourceClass) {
		this(log, sourceClass, null);
	}

	/**
	 * <p>Creates a new <code>SimpleLogger</code> for the specified Class that will log to, and be
	 * configured by, the provided <code>SimpleLog</code>.</p>
	 *
	 * <p>This constructor is the equivalent of calling:<br>
	 * {@link #SimpleLogger(SimpleLog,Class,Object,boolean)
	 * new SimpleLogger(SimpleLog.defaultInstance(), sourceClass, instanceId, false)}
	 * </p>
	 *
	 * @param log the <code>SimpleLog</code> instance that this <code>SimpleLogger</code> should log
	 * to and be configured by.
	 *
	 * @param sourceClass The class that this SimpleLogger object is for, the name of which will be
	 * printed out as part of every message.
	 *
	 * @param instanceId An object uniquely identifying the instance of <code><i>sourceClass</i></code>
	 * that this <code>SimpleLogger</code> is specifically for. Can be <code>null</code> if this
	 * <code>SimpleLogger</code> instance is for all instances of <code><i>sourceClass</i></code>
	 * rather than for a particular instance (this is the norm).
	 */
	public SimpleLogger(SimpleLog log, Class sourceClass, Object instanceId) {
		super(log, sourceClass, instanceId);
	}

	/**
	 * Logs a debug message at the {@link LogLevel#L1_FATAL Fatal} level.
	 *
	 * @param message the message to log.
	 *
	 * @see #db
	 */
	public void fatal(String message) {
		db(LogLevel.L1_FATAL, message);
	}

	/**
	 * Logs a debug message at the {@link LogLevel#L2_ERROR Error} level.
	 *
	 * @param message the message to log.
	 *
	 * @see #db
	 */
	public void error(String message) {
		db(LogLevel.L2_ERROR, message);
	}

	/**
	 * Logs a debug message at the {@link LogLevel#L3_WARN Warn} level.
	 *
	 * @param message the message to log.
	 *
	 * @see #db
	 */
	public void warn(String message) {
		db(LogLevel.L3_WARN, message);
	}

	/**
	 * Logs a debug message at the {@link LogLevel#L4_INFO Info} level.
	 *
	 * @param message the message to log.
	 *
	 * @see #db
	 */
	public void info(String message) {
		db(LogLevel.L4_INFO, message);
	}

	/**
	 * Logs a debug message at the {@link LogLevel#L5_DEBUG Debug} level.
	 *
	 * @param message the message to log.
	 *
	 * @see #db
	 */
	public void debug(String message) {
		db(LogLevel.L5_DEBUG, message);
	}

	/**
	 * Logs a debug message at the {@link LogLevel#L6_VERBOSE Verbose} level.
	 *
	 * @param message the message to log.
	 *
	 * @see #db
	 */
	public void verbose(String message) {
		db(LogLevel.L6_VERBOSE, message);
	}

	/**
	 * Logs a debug message at the {@link LogLevel#L7_LUDICROUS Ludicrous} level.
	 *
	 * @param message the message to log.
	 *
	 * @see #db
	 */
	public void ludicrous(String message) {
		db(LogLevel.L7_LUDICROUS, message);
	}

	/**
	 * Convenience method for logging an object's name and value at the {@link LogLevel#L4_INFO
	 * Info} level.
	 *
	 * @see #dbo(LogLevel,String,Object)
	 */
	public void infoObject(String objectName, Object val) {
		dbo(LogLevel.L4_INFO, objectName, val);
	}

	/**
	 * Convenience method for logging a <code>boolean</code>'s name and value at the {@link
	 * LogLevel#L4_INFO Info} level.
	 *
	 * @see #dbo(LogLevel,String,boolean)
	 */
	public void infoObject(String objectName, boolean val) {
		dbo(LogLevel.L4_INFO, objectName, val);
	}

	/**
	 * Convenience method for logging an <code>int</code>'s name and value at the
	 * {@link LogLevel#L4_INFO Info} level.
	 *
	 * @see #dbo(LogLevel,String,int)
	 */
	public void infoObject(String objectName, int val) {
		dbo(LogLevel.L4_INFO, objectName, val);
	}

	/**
	 * Convenience method for logging an object's name and value at the
	 * {@link LogLevel#L5_DEBUG Debug} level.
	 *
	 * @see #dbo(LogLevel,String,Object)
	 */
	public void debugObject(String objectName, Object val) {
		dbo(LogLevel.L5_DEBUG, objectName, val);
	}

	/**
	 * Convenience method for logging a <code>boolean</code>'s name and value at the
	 * {@link LogLevel#L5_DEBUG Debug} level.
	 *
	 * @see #dbo(LogLevel,String,boolean)
	 */
	public void debugObject(String objectName, boolean val) {
		dbo(LogLevel.L5_DEBUG, objectName, val);
	}

	/**
	 * Convenience method for logging an <code>int</code>'s name and value at the
	 * {@link LogLevel#L5_DEBUG Debug} level.
	 *
	 * @see #dbo(LogLevel,String,int)
	 */
	public void debugObject(String objectName, int val) {
		dbo(LogLevel.L5_DEBUG, objectName, val);
	}

	/**
	 * Convenience method for logging an object's name and value at the
	 * {@link LogLevel#L6_VERBOSE Verbose} level.
	 *
	 * @see #dbo(LogLevel,String,Object)
	 */
	public void verboseObject(String objectName, Object val) {
		dbo(LogLevel.L6_VERBOSE, objectName, val);
	}

	/**
	 * Convenience method for logging a <code>boolean</code>'s name and value at the
	 * {@link LogLevel#L6_VERBOSE Verbose} level.
	 *
	 * @see #dbo(LogLevel,String,boolean)
	 */
	public void verboseObject(String objectName, boolean val) {
		dbo(LogLevel.L6_VERBOSE, objectName, val);
	}

	/**
	 * Convenience method for logging an <code>int</code>'s name and value at the
	 * {@link LogLevel#L6_VERBOSE Verbose} level.
	 *
	 * @see #dbo(LogLevel,String,int)
	 */
	public void verboseObject(String objectName, int val) {
		dbo(LogLevel.L6_VERBOSE, objectName, val);
	}

	/**
	 * Convenience method for logging an object's name and value at the
	 * {@link LogLevel#L7_LUDICROUS Ludicrous} level.
	 *
	 * @see #dbo(LogLevel,String,Object)
	 */
	public void ludicrousObject(String objectName, Object val) {
		dbo(LogLevel.L7_LUDICROUS, objectName, val);
	}

	/**
	 * Convenience method for logging a <code>boolean</code>'s name and value at the
	 * {@link LogLevel#L7_LUDICROUS Ludicrous} level.
	 *
	 * @see #dbo(LogLevel,String,boolean)
	 */
	public void ludicrousObject(String objectName, boolean val) {
		dbo(LogLevel.L7_LUDICROUS, objectName, val);
	}

	/**
	 * Convenience method for logging an <code>int</code>'s name and value at the
	 * {@link LogLevel#L7_LUDICROUS Ludicrous} level.
	 *
	 * @see #dbo(LogLevel,String,int)
	 */
	public void ludicrousObject(String objectName, int val) {
		dbo(LogLevel.L7_LUDICROUS, objectName, val);
	}

	/**
	 * Convenience method for logging an exception (or throwable) at the
	 * {@link LogLevel#L1_FATAL Fatal} level.
	 *
	 * @see #dbe(LogLevel,Throwable)
	 */
	public void fatalException(Throwable t) {
		dbe(LogLevel.L1_FATAL, t);
	}

	/**
	 * Convenience method for logging an exception (or throwable) at the
	 * {@link LogLevel#L2_ERROR Error} level.
	 *
	 * @see #dbe(LogLevel,Throwable)
	 */
	public void errorException(Throwable t) {
		dbe(LogLevel.L2_ERROR, t);
	}

	/**
	 * Convenience method for logging an exception (or throwable) at the
	 * {@link LogLevel#L3_WARN Warn} level.
	 *
	 * @see #dbe(LogLevel,Throwable)
	 */
	public void warnException(Throwable t) {
		dbe(LogLevel.L3_WARN, t);
	}   
}