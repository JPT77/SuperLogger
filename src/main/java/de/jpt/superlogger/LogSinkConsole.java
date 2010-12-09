package de.jpt.superlogger;

import java.util.Properties;

/**
 * Root LogSink for most standard Loggers. 
 * @author jpt
 *
 */
public class LogSinkConsole extends LogSinkAbstract {

	public LogSinkConsole(String name, LogLevel loglevel, Properties props) {
		super(name, loglevel, props);
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
		if (queue != null && shouldPrintQueue(message)) {
			synchronized (queue) {
				log("---- Logging Prolog to message "+message.getMessageNumber());
				while (!queue.isEmpty()) {
					log(queue.dequeue().toString());
				}
			}
		}
	}


	public void log(String message) {
		System.out.println(message);		
	}	
	
}
