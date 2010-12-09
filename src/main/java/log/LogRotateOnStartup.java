package log;

import java.util.Properties;


public class LogRotateOnStartup extends LogRotate {
	
	public LogRotateOnStartup(String name, LogSinkFile logger, Properties props) {
		super(name, logger, props);
		rotate();
	}

	public static void main(String[] args) throws InterruptedException {
		LogSinkFile logger = new LogSinkFile("test", LogLevel.L3_WARN, new Properties());
		new LogRotateOnStartup("onstartup", logger, null);
		Thread.sleep(1000);
		logger.log("test");
		Thread.sleep(1000);
		logger.log("test1");
		Thread.sleep(1000);
		logger.log("test2");
		Thread.sleep(1000);
		logger.log("test3");
		Thread.sleep(1000);
		System.exit(0);
	}
	
}
