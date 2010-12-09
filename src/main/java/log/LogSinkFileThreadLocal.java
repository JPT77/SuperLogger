package log;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import util.SimpleQueue;

/**
 * Wraps LogSinkFile to have a single logfile per thread. 
 * @author jpt
 */
public class LogSinkFileThreadLocal extends ThreadLocal<LogSinkFile> implements LogSink {

	private final String name;
	private final LogLevel loglevel;
	private final Properties props; // not threadsafe, but we access readonly...
	private List<Object[]> logrotates; // not threadsafe, but we access readonly...

	public LogSinkFileThreadLocal(String name, LogLevel loglevel, Properties props) {
		this.name = name;
		this.loglevel = loglevel;
		this.props = props;
		this.logrotates = new ArrayList<Object[]>();
	}

	private static LogSinkFile createLogSink(String name, String thread, LogLevel loglevel, Properties props, List<Object[]> logrotates) {
		return new LogSinkFile(name+"-"+thread, thread, loglevel, props);
	}

	public LogSinkFile get() {
		LogSinkFile logsink = super.get();
		if (logsink == null) {
			String thread = Thread.currentThread().getName();

			logsink = new LogSinkFile(name+"-"+thread, thread, loglevel, props);
			for (Object[] l : logrotates) {
				logsink.addLogRotate((String)l[0],(String)l[1],(Properties)l[2]);
			}

			logsink = createLogSink(name, thread, loglevel, props, logrotates);
			set(logsink);
		}
		return logsink;
	}

	public void log(LogMessage message) {
		get().log(message);
	}

	public void log(String message) {
		get().log(message);
	}

	public String getName() {
		return get().getName();
	}

	public void setQueue(SimpleQueue<LogMessage> queue) {
		get().setQueue(queue);
	}

	public void clearQueue() {
		get().clearQueue();
	}

	public void addLogRotate(String classname, String name, Properties props) {
		logrotates.add(new Object[] { classname, name, props });
	}

}
