package de.jpt.superlogger;

import java.util.Properties;

public class LogSinkHtml extends LogSinkTable {

	public LogSinkHtml(String name, LogLevel loglevel, Properties props) {
		super(name, loglevel, props);
	}

	@Override
	protected void logToTable(LogMessage message) {
		// TODO Auto-generated method stub
		
	}

}
