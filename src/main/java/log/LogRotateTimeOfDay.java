package log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class LogRotateTimeOfDay extends LogRotate {
	
	public LogRotateTimeOfDay(String name, LogSinkFile logger, Properties props) throws ParseException {
		super(name, logger, props);
		
		long millisOfDay = parseTime(props.getProperty("timeofday", "00:00"));
		
		System.out.println(new Date(millisOfDay));
		long now = System.currentTimeMillis();
		System.out.println(new Date(now));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		now = now - calendar.getTimeInMillis();
		System.out.println(new Date(now));
		
		if (millisOfDay < now) {
			// today in past already
			millisOfDay = millisOfDay + (24*60*60*1000);			
		}
		long next = millisOfDay - now; 
		next = 500;
		System.out.println(new Date(next));
		assert (next > 0);
		createTimer(next, true);		
	}

	private static long parseTime(String time) throws ParseException {
		DateFormat format = new SimpleDateFormat("HH:mm");
		return format.parse(time).getTime();
	}

	public static void main(String[] args) throws ParseException, InterruptedException {
		new LogRotateTimeOfDay("tod", new LogSinkFile("test", LogLevel.L3_WARN, new Properties()), new Properties());
		Thread.sleep(10000);
	}
	
}
