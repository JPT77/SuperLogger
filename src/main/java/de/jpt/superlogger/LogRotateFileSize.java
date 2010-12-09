package de.jpt.superlogger;

import java.text.ParseException;
import java.util.Properties;

public class LogRotateFileSize extends LogRotate {
	
	private final static long INTERVALL = 5*60*1000;
	
	private long filesize;

	public LogRotateFileSize(String name, LogSinkFile logger, Properties props) {
		super(name, logger, props);
		filesize = parseFilesize(props.getProperty("filesize"));
		
		createTimer(INTERVALL, false);
	}
	

	private static long parseFilesize(String filesize) {
		return 1024*1024;
	}
	
	protected void rotate() {
		System.out.println(logger.getFilesize() + ">" + filesize + " = " +(logger.getFilesize() > filesize));
		
		if (logger.getFilesize() > filesize) {
			super.rotate();
		}
	}
	
	public static void main(String[] args) throws ParseException, InterruptedException {
		LogSinkFile logger = new LogSinkFile("test", LogLevel.L3_WARN, new Properties());
		new LogRotateFileSize("fs", logger, new Properties());
		new LogRotateOnStartup("startup", logger, new Properties());
		
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
