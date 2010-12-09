package log;

import java.util.Properties;

import util.SimpleQueue;

/**
 * Root LogSink for most standard Loggers. 
 * @author jtisje
 *
 */
public abstract class LogSinkAbstract implements LogSink {

	private String name;
	private LogLevel loglevel;
	protected SimpleQueue<LogMessage> queue = null;
	private boolean logReport = false;
	private boolean logTrace = false;

	public LogSinkAbstract(String name, LogLevel loglevel, Properties props) {
		this.name = name;
		this.loglevel = loglevel;
		this.logReport = Boolean.parseBoolean(props.getProperty("logreport"));
		this.logTrace = Boolean.parseBoolean(props.getProperty("logtrace"));
	}


	protected boolean shouldPrintQueue(LogMessage message) {
		// TODO Auto-generated method stub
		return false;
	}

	protected boolean shouldLog(LogMessage message) {
		switch (message.getLevel().getLevel()) {
		case LogLevel.REPORT_INT:
			return logReport;
		case LogLevel.TRACE_INT:
			return logTrace;
		default:
			return loglevel.shouldLog(message.getLevel());
		}
	}

	public String getName() {
		return name;
	}

	public void setQueue(SimpleQueue<LogMessage> queue) {
		this.queue = queue;		
	}

	public void clearQueue() {
		if (queue != null) {
			queue.clear();
		}		
	}

	public void addLogRotate(String classname, String name, Properties props) {
		// ignore, nothing to rotate
		System.err.println(this.getClass().getName()+" does not know how to logrotate.");
	}
	
}
