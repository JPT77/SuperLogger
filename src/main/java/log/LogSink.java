package log;

import java.util.Properties;

import util.SimpleQueue;

public interface LogSink {

	public void log(LogMessage message);

	public void log(String message);

	public String getName();

	public void setQueue(SimpleQueue<LogMessage> queue);

	public void clearQueue();

	public void addLogRotate(String classname, String name, Properties props);

}
