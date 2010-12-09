package log;

import java.util.Properties;

public abstract class LogSinkTable extends LogSinkAbstract {

	public LogSinkTable(String name, LogLevel loglevel, Properties props) {
		super(name, loglevel, props);
	}
	public void log(LogMessage message) {
		if (shouldLog(message)) {			
			logQueue(message);
			logToTable(message);
		} else {
			if (queue != null) {
				queue.add(message);
			}
		}
	}
	
	protected abstract void logToTable(LogMessage message);	
	
	protected void logQueue(LogMessage message) {
		if (queue != null && shouldPrintQueue(message)) {
			synchronized (queue) {
//				logToTable("---- Logging Prolog to message "+message.getMessageNumber());
				while (!queue.isEmpty()) {
					logToTable(queue.dequeue());
				}
			}
		}
	}
	
	public void log(String message) {		
		// TODO mhh... do nothing?		
	}
	
}
