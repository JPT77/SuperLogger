package simplelog;

import java.text.MessageFormat;
import java.util.Date;

import log.LogLevel;


import util.SimpleQueue;
import util.ThreadLocalQueue;

public class QueuedLogger extends SimpleLogger {
	
	private static int HISTORY_SIZE = 20;

	private static ThreadLocalQueue queue = new ThreadLocalQueue(HISTORY_SIZE);
	
	public QueuedLogger(Class class1) {
		super(class1);
	}

	public QueuedLogger(Class class1, Object obj) {
		super(class1, obj);
	}

	public QueuedLogger(SimpleLog simplelog, Class class1, Object obj) {
		super(simplelog, class1, obj);
	}

	public QueuedLogger(SimpleLog simplelog, Class class1) {
		super(simplelog, class1);
	}


	protected Object[] createData(Integer format, LogLevel level, Object param0, Object extraArgs) {
		Object[] result = super.createData(format, level, param0, extraArgs);

		if (!getDebugLevel().shouldLog(level)) {
			// TODO if: delayed logging enabled, level between Up and low
			queue.add(result);
		}
		return result;
	}
	
	protected void logMessageObject(LogLevel level, String message, Object value) {
		logQueue(level);
		super.logMessageObject(level, message, value);
	}
	
	protected void logMessage(LogLevel level, String message) {
		logQueue(level);
		super.logMessage(level, message);
	}
	
	protected void logException(LogLevel level, Throwable t) {
		logQueue(level);
		super.logException(level, t);
	}
	

	/**
	 * Depending on current DebugLevel, the queue is written to log. 
	 * @param level
	 */
	private void logQueue(LogLevel level) {
		SimpleQueue q = queue.getQueue();
		if (LogLevel.L3_WARN.shouldLog(level) && !q.isEmpty()) { // if WARN or ERROR we log the content of the queue
			try {
				log.println(log.getDateFormat().format(new Date()) +"|--- Logging Prolog to a message of level "+level.toString());
				while (!q.isEmpty()) {
					logQueueEntry((Object[])q.dequeue());
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Clears this thread's logmessage queue. Might be used for example on start of processing a webservice request, 
	 * since this is stateless and does not depend on a request processed earlier. 
	 */
	public void clearQueue() {
		queue.clear();		
	}

	private void logQueueEntry(Object data) { 
		Object[] d = (Object[])data;
		MessageFormat format = getFormat((Integer) d[5]);
		log.println(format.format(data));
	}

}
