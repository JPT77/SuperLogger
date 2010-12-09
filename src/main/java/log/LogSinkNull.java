package log;

import java.util.Properties;

import util.SimpleQueue;

/**
 * Null LogSink, does not log anything. 
 * @author jpt
 *
 */
public class LogSinkNull implements LogSink {

	private String name;

	public LogSinkNull(String name, LogLevel loglevel, Properties props) {
		this.name = name;
	}

	public void log(LogMessage message) {
	}
	
	public void log(String message) {
	}	
	
	public String getName() {
		return name;
	}

	public void setQueue(SimpleQueue<LogMessage> queue) {
	}

	public void clearQueue() {
	}

	public void addLogRotate(String classname, String name, Properties props) {
	}
	
}
