package de.jpt.superlogger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class LogRotateIntervall extends LogRotate {
	
	public LogRotateIntervall(String name, LogSinkFile logger, Properties props) throws ParseException {
		super(name, logger, props);
		long intervall = parseTime(props.getProperty("intervall", "01:00"));
		createTimer(intervall, false);		
	}

	private static long parseTime(String time) throws ParseException {
		DateFormat format = new SimpleDateFormat("HH:mm");
		return format.parse(time).getTime();
	}

	public static void main(String[] args) throws ParseException, InterruptedException {
		new LogRotateIntervall("intervall", new LogSinkFile("test", LogLevel.L3_WARN, new Properties()), new Properties());
		Thread.sleep(10000);
	}
	
}
