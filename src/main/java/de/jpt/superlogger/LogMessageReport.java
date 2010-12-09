package de.jpt.superlogger;

/**
 * This class logs more than one line. 
 * You may use it to log the result of a batch for example. 
 * Then add an LogSinkEmail, that sends the report.
 * @author jpt
 *
 */
public class LogMessageReport extends LogMessage {

	public LogMessageReport(String classname, String title) {
		super(LogLevel.REPORT, classname, title);
	}

	protected char getType() {
		return 'r';
	}
}
