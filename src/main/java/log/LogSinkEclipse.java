package log;

import java.util.List;
import java.util.Properties;

public class LogSinkEclipse extends LogSinkTable {

	public LogSinkEclipse(String name, LogLevel loglevel, Properties props) {
		super(name, loglevel, props);
	}
	
	protected void logToTable(LogMessage message) {
		String[] header = message.getHeader();
		
		List<Object> values = message.getValues();
	}

}
