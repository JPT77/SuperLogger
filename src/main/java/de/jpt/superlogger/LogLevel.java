package de.jpt.superlogger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>A Java enum defining all possible debug levels. There are seven levels, from "Fatal" (most
 * important) to "Ludicrous" (most verbose).</p>
 * <p>Note that tracing is not related to the debug levels, as tracing is controlled
 * independently.</p>
 *
 * @version $Revision: 1.9 $
 * @author $Author: grlea $
 */
public final class LogLevel {
	/**
	 * The "Fatal" level. This should be used when an unexpected error occurs that is serious enough
	 * to cause or warrant the halting of the current process or even the whole application.
	 */
	public static final LogLevel L1_FATAL = new LogLevel(1, "FATAL ");

	/**
	 * The "Error" level. This should be used when an unexpected error occurs that cannot be
	 * recovered from.
	 */
	public static final LogLevel L2_ERROR = new LogLevel(2, "ERROR ");

	/**
	 * The "Warning" level. This should be used when an unexpected error occurs that CAN be
	 * recovered from.
	 */
	public static final LogLevel L3_WARN = new LogLevel(3, "WARN  ");

	/**
	 * The "Info" level. This should be used for reporting information to the end user.
	 */
	public static final LogLevel L4_INFO = new LogLevel(4, "INFO  ");

	/**
	 * The "Debug" level. This should be used for the most basic debugging statements. This may
	 * include the direction taken in a control structure, events fired or received and the state of
	 * important objects or variables.
	 */
	public static final LogLevel L5_DEBUG = new LogLevel(5, "DEBUG ");

	/**
	 * The "Verbose" level. This should be used for debugging statements that produce a large amount
	 * of output, are called often, or that don't need to be seen when performing high-level
	 * debugging. This could include extraneous events (e.g. keyboard or mouse events), the state of
	 * not-so-important objects or variables, and statements that exist within a loop. It is
	 * recommended that method parameters and return values be logged at this level (unless the
	 * amount of output they produce is "ludicrous").
	 */
	public static final LogLevel L6_VERBOSE = new LogLevel(6, "VERBOS");

	/**
	 * The "Ludicrous" level. This should be used for debugging statements that produce a
	 * ridiculous wealth of output, e.g. printing a line for every pixel in an image.
	 */
	public static final LogLevel L7_LUDICROUS = new LogLevel(7, "LUDICR");

	static final int TRACE_INT = Integer.MAX_VALUE;
	/**
	 * The "Trace" "level". This exists only to provide a name for output patterns that want to print
	 * the name of the debug level. It should <b>never</b> be used outside of the Simple Log package.
	 */
	static final LogLevel FAKE_TRACE = new LogLevel(TRACE_INT, "TRACE " );


	static final int REPORT_INT = Integer.MIN_VALUE;
	public static final LogLevel REPORT = new LogLevel(REPORT_INT, "REPORT");

	/**
	 * A map of all {@link LogLevel}s, keyed by the lower-case version of their
	 * name (without the Lx_ prefix).
	 */
	private static final Map<String, LogLevel> levelsByName;

	static {
		HashMap<String, LogLevel> levels = new HashMap<String, LogLevel>(10, 0.8F);
		levels.put("fatal", L1_FATAL);
		levels.put("error", L2_ERROR);
		levels.put("warn", L3_WARN);
		levels.put("info", L4_INFO);
		levels.put("debug", L5_DEBUG);
		levels.put("verbose", L6_VERBOSE);
		levels.put("ludicrous", L7_LUDICROUS);

		levelsByName = Collections.unmodifiableMap(levels);
	}

	/**
	 * The level of this particular <code>DebugLevel</code> instance.
	 */
	private final int level;

	/**
	 * The name of this particular <code>DebugLevel</code> instance.
	 */
	private final String name;

	/**
	 * Creates a new <code>DebugLevel</code> with the given level. This is private to preven external
	 * instantiation.
	 *
	 * @param level the level for the new <code>DebugLevel</code>.
	 *
	 * @param name the name of the new <code>DebugLevel</code>.
	 */
	private LogLevel(int level, String name) {
		this.level = level;
		this.name = name;
	}

	/**
	 * Returns <code>true</code> if a {@link SimpleLogger} that has <b>this</b> level should log a
	 * message that has the <b>given</b> level.
	 *
	 * @param logLevel the level of the message to be considered
	 *
	 * @return <code>true</code> if the message should be logged, <code>false</code> if it should be
	 * ignored.
	 */
	public boolean shouldLog(LogLevel logLevel) {
		return logLevel.level <= this.level;
	}

	/**
	 * Returns the <code>DebugLevel</code> object associated with the given level. If the given level
	 * is above or below the defined levels, the closest level will be returned.
	 *
	 * @param level the level value whose corresponding <code>DebugLevel</code> is to be returned.
	 *
	 * @return The matching or closest matching <code>DebugLevel</code>.
	 */
	public static LogLevel fromInt(int level) {
		switch (level) {
		case 1: return L1_FATAL;
		case 2: return L2_ERROR;
		case 3: return L3_WARN;
		case 4: return L4_INFO;
		case 5: return L5_DEBUG;
		case 6: return L6_VERBOSE;
		case 7: return L7_LUDICROUS;

		default:
			if (level < 1)
				return L1_FATAL;
			else
				return L7_LUDICROUS;
		}
	}

	/**
	 * Returns the <code>DebugLevel</code> object associated with the given name. The name of the
	 * debug levels is equivalent to their name in the class except with the "Lx_" prefix. E.g. the
	 * name of the {@link #L1_FATAL} level is "Fatal". The name is treated case-insensitively.
	 *
	 * @param name the name of the <code>DebugLevel</code> to return. This name is not
	 * case-sensitive, but <b>will not</b> be automatically trimmed by this method.
	 *
	 * @return The <code>DebugLevel</code> matching the given name.
	 *
	 * @throws IllegalArgumentException if <code>name</code> is <code>null</code> or if there is no
	 * <code>DebugLevel</code> associated with the specified name.
	 */
	static LogLevel fromName(String name) throws IllegalArgumentException {
		if (name == null)
			throw new IllegalArgumentException("name cannot be null.");

		LogLevel level = (LogLevel) levelsByName.get(name.toLowerCase());
		if (level == null)
			throw new IllegalArgumentException("Unrecognised DebugLevel name: '" + name + "'");

		return level;
	}

	/**
	 * Returns this <code>DebugLevel</code>'s level.
	 */
	public int hashCode() {
		return level;
	}

	/**
	 * Returns <code>true</code> if the given object is a <code>DebugLevel</code> with the same level
	 * value as this <code>DebugLevel</code>.
	 */
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (o == null || !(o.getClass().equals(this.getClass())))
			return false;

		return this.level == ((LogLevel) o).level;
	}

	/**
	 * Returns a string representation of this object in its current state.
	 */
	public String toString() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public static LogLevel parse(String level) {
		if (level.length() > 1) {
			return fromName(level);
		} else {
			return fromInt(Integer.parseInt(level));
		}
	}

}