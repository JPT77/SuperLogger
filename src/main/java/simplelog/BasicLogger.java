package simplelog;

import de.jpt.superlogger.LogLevel;
import de.jpt.superlogger.LogMessage;
import de.jpt.superlogger.LogMessageException;
import de.jpt.superlogger.LogMessageObject;
import de.jpt.superlogger.LogMessageTrace;
import de.jpt.superlogger.typewrapper.ByteArray;
import de.jpt.superlogger.typewrapper.CharArray;
import de.jpt.superlogger.typewrapper.IntArray;
import de.jpt.superlogger.typewrapper.LongArray;
import de.jpt.superlogger.util.StringUtils;

public class BasicLogger extends AbstractLogger {

    public BasicLogger(SimpleLog log, Class sourceClass, Object instanceId) {
        super(log, sourceClass, instanceId);
    }

    /**
     * <p>Logs a message containing a <code>boolean</code>'s name and value.</p>
     *
     * @see #dbo(LogLevel,String,Object)
     */
    public final long dbo(LogLevel level, String objectName, boolean val) {
        if (!shouldCreateMessage()) { return -1; }
        LogMessage msg = new LogMessageObject(level, className, objectName, Boolean.toString(val));
        logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs a message containing a <code>byte</code>'s name and value.</p>
     *
     * @see #dbo(LogLevel,String,Object)
     */
    public final long dbo(LogLevel level, String objectName, byte val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, StringUtils.toString(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs a message containing a <code>byte</code> array's name and value.</p>
     *
     * @see #dbo(LogLevel,String,Object)
     */
    public final long dbo(LogLevel level, String objectName, byte[] val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, new ByteArray(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs a message containing a <code>char</code>'s name and value.</p>
     *
     * @see #dbo(LogLevel,String,Object)
     */
    public final long dbo(LogLevel level, String objectName, char val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, String.valueOf(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs a message containing a <code>char</code> array's name and value.</p>
     *
     * @see #dbo(LogLevel,String,Object)
     */
    public final long dbo(LogLevel level, String objectName, char[] val) {
        if (!shouldCreateMessage()) { return -1;}
		LogMessage msg = new LogMessageObject(level, className, objectName, new CharArray(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs a message containing a <code>double</code>'s name and value.</p>
     *
     * @see #dbo(LogLevel,String,Object)
     */
    public final long dbo(LogLevel level, String objectName, double val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, String.valueOf(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs a message containing a <code>float</code>'s name and value.</p>
     *
     * @see #dbo(LogLevel,String,Object)
     */
    public final long dbo(LogLevel level, String objectName, float val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, String.valueOf(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs a message containing an <code>int</code>'s name and value.</p>
     *
     * @see #dbo(LogLevel,String,Object)
     */
    public final long dbo(LogLevel level, String objectName, int val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, String.valueOf(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    public final long dbo(LogLevel level, String objectName, int[] val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, new IntArray(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    public final long dbo(LogLevel level, String objectName, long[] val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, new LongArray(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs a message containing a <code>long</code>'s name and value.</p>
     *
     * @see #dbo(LogLevel,String,Object)
     */
    public final long dbo(LogLevel level, String objectName, long val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, String.valueOf(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs a message containing an object's name and value.</p>
     *
     * <p>The object name and value will be printed only if the given debug level is less than or
     * equal to the current debug level of this <code>SimpleLogger</code>.</p>
     *
     * <p>Note that, if the given object is an instance of <code>Object[]</code>, <code>byte[]</code>
     * or <code>char[]</code>, this method will route the call to the corresponding variation of
     * <code>dbo()</code> (each of which performs special formatting for the particualr type),
     * eliminating the need to perform pre-logging type checks and casts.</p>
     *
     * <p>Note that passing a {@link Throwable} to this method does not behave in the same way as
     * {@link #dbe}. I.e. passing an exception in as the object value will not result in a stack
     * trace being printed.</p>
     *
     * @param objectName The name of the object whose value is being given.
     *
     * @param value The value of the object.
     */
    public long dbo(LogLevel level, String objectName, Object value) {
        if (!shouldCreateMessage()) { return -1; }

        if (value instanceof Object[]) {
        	return dbo(level, objectName, (Object[])value);
        } else if (value instanceof byte[]) {
            return dbo(level, objectName, (byte[])value);
        } else if (value instanceof char[]) {
        	return dbo(level, objectName, (char[])value);
        } else if (value instanceof int[]) {
        	return dbo(level, objectName, (int[])value);
        } else if (value instanceof long[]) {
        	return dbo(level, objectName, (long[])value);
        } else {
    		LogMessage msg = new LogMessageObject(level, className, objectName, value);
    		logMessage(msg);
    		return msg.getMessageNumber();
        }
    }

    /**
     * <p>Logs a message containing an object array's name and value.</p>
     *
     * <p>The array name and value will be printed only if the given debug level is less than or
     * equal to the current debug level of this <code>SimpleLogger</code>.</p>
     *
     * @param objectName The name of the array whose value is being given.
     *
     * @param val The array.
     */
    public final long dbo(LogLevel level, String objectName, Object[] val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, val);
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs a message containing a <code>short</code>'s name and value.</p>
     *
     * @see #dbo(LogLevel,String,Object)
     */
    public final long dbo(LogLevel level, String objectName, short val) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageObject(level, className, objectName, String.valueOf(val));
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>Logs an entry message.</p>
     *
     * <p>The message will be printed only if the this <code>SimpleLogger</code>'s tracing flag is
     * set to <code>true</code>.</p>

     * @param methodName The method name to include in the entry message.
     */
    public final long entry(String methodName) {
    	return entry(methodName, null);
    }

    /**
     * <p>Logs an entry message.</p>
     *
     * <p>The message will be printed only if the this <code>SimpleLogger</code>'s tracing flag is
     * set to <code>true</code>.</p>

     * @param methodName The method name to include in the entry message.
     */
    public final long entry(String methodName, Object[] params) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageTrace(LogMessageTrace.ENTRY, className, methodName, params);
		logMessage(msg);
		return msg.getMessageNumber();
    }

    /**
     * <p>
     * Logs an exit message.
     * </p>
     *
     * <p>
     * The message will be printed only if the this <code>SimpleLogger</code>'s
     * tracing flag is set to <code>true</code>.
     * </p>
     */
    public final long exit(String methodName) {
    	return exit(methodName, null);
    }

    /**
     * <p>
     * Logs an exit message.
     * </p>
     *
     * <p>
     * The message will be printed only if the this <code>SimpleLogger</code>'s
     * tracing flag is set to <code>true</code>.
     * </p>
     */
    public final long exit(String methodName, Object result) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageTrace(LogMessageTrace.EXIT, className, methodName, result);
		logMessage(msg);
		return msg.getMessageNumber();
    }


    public final long start(String methodName) {
    	return start(methodName, null);
    }

    public long start(String methodName, Object[] params) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageTrace(LogMessageTrace.START, className, methodName, params);
		logMessage(msg);
		return msg.getMessageNumber();
    }


    public final long finish(String methodName) {
    	return finish(methodName, null);
    }

    public long finish(String methodName, Object result) {
        if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageTrace(LogMessageTrace.FINISH, className, methodName, result);
		logMessage(msg);
		return msg.getMessageNumber();
    }


	/**
	 * <p>
	 * Logs a message containing an exception (or throwable).
	 * </p>
	 *
	 * <p>
	 * The exception will be printed only if the given debug level is less than
	 * or equal to the current debug level of this <code>SimpleLogger</code>.
	 * This method will result in the stack trace of the exception being printed
	 * if this option is turned on in the properties (which it is by default).
	 * </p>
	 *
	 * @param t
	 *            the throwable to log.
	 */
	public final long dbe(LogLevel level, Throwable t) {
		if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessageException(level, className, t);
		logMessage(msg);
		return msg.getMessageNumber();
	}

	/**
	 * <p>
	 * Logs a simple debug message.
	 * </p>
	 *
	 * <p>
	 * The message will be printed if the given debug level is less than or
	 * equal to the current debug level of this <code>SimpleLogger</code>.
	 * </p>
	 *
	 * @param message
	 *            The debug message to print.
	 *
	 * @see #fatal
	 * @see #error
	 * @see #warn
	 * @see #info
	 * @see #debug
	 * @see #verbose
	 */
	public final long db(LogLevel level, String message) {
		if (!shouldCreateMessage()) { return -1; }
		LogMessage msg = new LogMessage(level, className, message);
		logMessage(msg);
		return msg.getMessageNumber();
	}

}
