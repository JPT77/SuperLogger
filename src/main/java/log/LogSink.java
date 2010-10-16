package log;

import java.util.Properties;

import util.SimpleQueue;

public class LogSink {

	private String name;
	private LogLevel loglevel;
	protected SimpleQueue<LogMessage> queue = null;

	public LogSink(String name, LogLevel loglevel, Properties props) {
		this.name = name;
		this.loglevel = loglevel;
	}

	public void log(LogMessage message) {
		if (shouldLog(message)) {
			logQueue(message);
			log(message.toString());
		} else {
			if (queue != null) {
				queue.add(message);
			}
		}
	}
	
	protected void logQueue(LogMessage message) {
		if (queue != null) {
			synchronized (queue.getLock()) {
				log("---- Logging Prolog to message "+message.getMessageNumber());
				while (!queue.isEmpty()) {
					log(queue.dequeue().toString());
				}
			}
		}
	}

	protected boolean shouldLog(LogMessage message) {
		return loglevel.shouldLog(message.getLevel());
	}

	public void log(String message) {
		System.out.println(message);		
	}	
	
	public String getName() {
		return name;
	}

	public void setQueue(SimpleQueue<LogMessage> queue) {
		this.queue = queue;		
	}
	
}
