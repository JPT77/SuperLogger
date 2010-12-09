package de.jpt.superlogger;

public class LogMessageException extends LogMessage {

	private Throwable exception;

	public LogMessageException(LogLevel level, String classname, Throwable exception) {
		super(level, classname, " "+exception.toString());
		this.exception = exception;
	}

	protected char getType() {
		return '!';
	}

	protected void appendParams(StringBuffer buffer) {
		StackTraceElement[] stacktrace = exception.getStackTrace();
		for (int i = 0; i < stacktrace.length; i++) {
			buffer.append("\n\tat ");
			buffer.append(stacktrace[i]);
		}
	}
	
	
}
