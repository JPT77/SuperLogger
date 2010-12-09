package de.jpt.superlogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class LogSinkFile extends LogSinkConsole {
	
	private File parent;
	private final File logfile;
	private LockablePrintWriter writer;
	private Date created;
	private String prefix;

	LogSinkFile(String name, LogLevel loglevel, Properties props, String prefix) {
		super(name, loglevel, props);
		parent = new File(props.getProperty("longsink.file.filename.path", "." ));
		this.prefix = prefix; 
		logfile = new File(parent.getPath()+"/"+prefix+".log");
		created = new Date();
		writer = createLogFile(logfile, true);
	}

	public LogSinkFile(String name, LogLevel loglevel, Properties props) {
		this(name, loglevel, props, props.getProperty("longsink.file.filename.prefix", "logfile"));
	}

	LogSinkFile(String name, String thread, LogLevel loglevel, Properties props) {
		this(name, loglevel, props, props.getProperty("longsink.file.filename.prefix", "logfile")+"-"+thread);
	}

	private static LockablePrintWriter createLogFile(final File logfile, boolean append) {
		try {
			return new LockablePrintWriter(new BufferedWriter(new FileWriter(logfile, append)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}

	public void log(String message) {
		if (writer != null) {
			writer.println(message);
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
			synchronized (writer.getLock()) {
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
			writer.close();
			writer = null;	
		}
	}

	public void flush() {
		if (writer != null) {
			writer.flush();
		}
	}
	
	public void addLogRotate(String classname, String name, Properties props) {
		try {			
			Class<?> clazz = Class.forName(classname);
			Constructor<?> constructor = clazz.getConstructor(String.class, LogSinkFile.class, Properties.class);
			constructor.newInstance(new Object[] { name, this, props } );
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}	
	
}
