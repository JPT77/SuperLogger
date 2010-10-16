package log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class LogSinkFile extends LogSink {
	
	private File parent;
	private final File logfile;
	private PrintWriter writer;
	private Date created;
	private String prefix;

	public LogSinkFile(String name, LogLevel loglevel, Properties props) {
		super(name, loglevel, props);
		parent = new File(props.getProperty("longsink.file.filename.path", "." ));
		prefix = props.getProperty("longsink.file.filename.prefix", "logfile");
		logfile = new File(parent.getPath()+"/"+prefix+".log");
		created = new Date();
		writer = createLogFile(logfile, true);
	}

	private static PrintWriter createLogFile(final File logfile, boolean append) {
		try {
			return new PrintWriter(new BufferedWriter(new FileWriter(logfile, append)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}

	public void log(String message) {
		if (writer != null) {
			synchronized (logfile) {
				writer.println(message);
			}
		} else {
			super.log(message);
		}				
	}	
	
	protected void rotateNow() {
		if (logfile.exists()) {
			DateFormat format = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
			System.out.println(format.format(new Date())+" rotateNow");
			File target = new File(parent.getPath()+"/"+prefix+"-"+format.format(created)+".log");
			System.out.println("Rotate to "+target.getAbsolutePath());
			synchronized (logfile) {
				close();
				if (!logfile.renameTo(target)) {
					System.out.println("Could not rename logfile to "+target.getAbsolutePath());
				}
				writer = createLogFile(logfile, false);
			}
			if (target.exists() && target.length() == 0) {
				target.delete();
				System.out.println("delete "+target.getName());
			} else {
				
			}
		}
		created = new Date();
	}
	
	public long getFilesize() {
		flush();
		return logfile.length();
	}
	
	public void close() {
		if (writer != null) {
			synchronized (logfile) {
				writer.close();
				writer = null;	
			}
		}
	}

	public void flush() {
		if (writer != null) {
			synchronized (logfile) {
				writer.flush();
			}
		}
	}
	
}
