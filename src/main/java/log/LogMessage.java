package log;

import java.util.Date;

public class LogMessage {

	protected static final char MESSAGE_SEPARATOR = '|';
	protected static final boolean debug = true;
	
	private static long count = 1;
	private static String fill = "00000000";

	private LogLevel level;
	private String message;
	private Date timestamp;
	private String classname;
	private long messageNumber;
	private StackTraceElement[] stacktrace;

	public LogMessage(LogLevel level, String classname, String message) {		
		this.level = level; 
		this.message = message;
		this.timestamp = new Date();
		this.classname = classname;
		synchronized (this) {
			this.messageNumber = count;
			count++;
		}
		
		if (debug) {
			stacktrace = Thread.currentThread().getStackTrace();
		}
	}

	public final String toString() {
		StringBuffer buffer = new StringBuffer();
		int len = (int)(Math.log(messageNumber)/Math.log(10.0));
//		System.out.println("log("+number+")/log(10)="+len);
		buffer.append(fill.substring(len));
		buffer.append(messageNumber);		
		buffer.append(MESSAGE_SEPARATOR);
		buffer.append(timestamp);
		buffer.append(MESSAGE_SEPARATOR);
		buffer.append(level.toString());
		buffer.append(MESSAGE_SEPARATOR);
		buffer.append(Thread.currentThread().getName());
		buffer.append(MESSAGE_SEPARATOR);
		if (stacktrace == null) {
			buffer.append(classname);
		} else {
			buffer.append(getCaller(stacktrace, classname));			
		}
		buffer.append(MESSAGE_SEPARATOR);
		buffer.append(getType());
		buffer.append(MESSAGE_SEPARATOR);
		buffer.append(message);
		appendParams(buffer);
		return buffer.toString();
	}

	private static String getCaller(StackTraceElement[] stacktrace, String classname) {
		for (int i = 1; i < stacktrace.length; i++) {
//			System.out.println(stacktrace[i]);
//			if (!stacktrace[i].getClassName().startsWith("org.grlea.log")) {
			if (stacktrace[i].getClassName().endsWith(classname)) {
				return " "+stacktrace[i].toString();
			}
		}
		return classname;
	}

	protected void appendParams(StringBuffer buffer) {
		// default: nothing
	}

	protected char getType() {
		return 'm';
	}
	
	protected void appendArrayContent(StringBuffer buffer, Object[] data) {
		for (int i = 0; i < data.length; i++) {
			if (data[i] instanceof Object[]) {
				buffer.append("[");
				appendArrayContent(buffer, (Object[]) data[i]);
				buffer.append(']');
			} else {
				buffer.append(data[i]);
			}
			if (i < data.length-1) {
				buffer.append(", ");
			}
		}
		
	}

	public long getMessageNumber() {
		return messageNumber;
	}
	
	public LogLevel getLevel() {
		return level;
	}
	
}
