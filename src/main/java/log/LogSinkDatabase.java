package log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class LogSinkDatabase extends LogSinkTable {
	
	Connection conn = new com.olution.test.stubs.jdbc.TestConnection();

	public LogSinkDatabase(String name, LogLevel loglevel, Properties props) {
		super(name, loglevel, props);
	}

	protected void logToTable(LogMessage message) {
		try {
			PreparedStatement stmt = conn.prepareStatement("insert into test", message.getHeader());
			List<Object> values = message.getValues();
			int i = 0;
			for (Object object : values) {				
				stmt.setObject(i, object);
				i++;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
