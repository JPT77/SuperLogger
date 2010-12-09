package de.jpt.superlogger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.jpt.superlogger.util.SimpleQueue;



public class LogSingleton {

	private static LogSingleton instance = new LogSingleton();

	private Map<String, LogSink> logsinks = new HashMap<String, LogSink>();
	private Constructor<Logger> loggerConstructor = null;	
	
	public LogSingleton() {
		try {
			init("logger.xml");
		} catch (Throwable e) {
			e.printStackTrace();
		}			
	}
	
	public static Logger getLogger(Class<Logger> clazz) {
		return instance.getLoggerInstance(clazz.getName());		
	}

	public static Logger getLogger(String name) {
		return instance.getLoggerInstance(name);		
	}
	
	public Logger getLoggerInstance(String name) {	
		if (loggerConstructor != null) {
			try {
				return loggerConstructor.newInstance(this, name);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return new Logger(this, name);
	}


	
	private void init(String filename) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fac.newDocumentBuilder();
		Document doc = builder.parse(new File("logger.xml"));		
		Element root = doc.getDocumentElement();
		parseGlobals(root);
		createSinks(root);		
	}
	
	private void parseGlobals(Element elem) {
		NodeList elems = elem.getElementsByTagName("global");
		if (elems.getLength() > 0) {
			loggerConstructor = getLoggerClass((Element) elems.item(0));
		}
	}

	private Constructor<Logger> getLoggerClass(Element elem) {
		NodeList elems = elem.getElementsByTagName("logger");
		if (elems.getLength() > 0) {
			try {
				Element logger = (Element) elems.item(0);
				@SuppressWarnings("unchecked")
				Class<Logger> clazz = (Class<Logger>) Class.forName(logger.getAttribute("class"));
				return clazz.getConstructor(LogSingleton.class, String.class);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Parse Configfile, Part Logsinks
	 */
	private void createSinks(Element elem) {
		NodeList elems = elem.getElementsByTagName("logsink");
		if (elems != null && elems.getLength() > 0) {
			for (int i = 0; i < elems.getLength(); i++) {
				register(createSink((Element) elems.item(i)));
			}
		} else {
			register(new LogSinkConsole("default", LogLevel.L3_WARN, new Properties()));
		}
	}

	private void register(LogSink sink) {
		logsinks.put(sink.getName(), sink);		
	}

	/**
	 * Parse Configfile, Part Logsinks
	 */
	private LogSink createSink(Element elem) {
		Properties props = getProperties(elem);
		String classname = getAttribute(elem, "class", LogSink.class.getName());
		String name = getAttribute(elem, "name", "default");
		LogLevel loglevel = LogLevel.parse(getAttribute(elem, "loglevel", LogLevel.L3_WARN.toString()));

		LogSink sink = (LogSink) createInstance(classname, LogSink.class, new Object[] { name, loglevel, props });
		createRotates(elem, sink);	
		sink.setQueue(createQueue(elem));
		return sink;
	}

	private SimpleQueue<LogMessage> createQueue(Element elem) {
		NodeList list = elem.getElementsByTagName("logqueue");
		if (list.getLength() > 0) {
			Element item = (Element) list.item(0);
			String classname = item.getAttribute("class");
			String size = getAttribute(item, "queuesize", "20");
			return (SimpleQueue<LogMessage>) createInstance(classname, null, new Object[] { size });
		}
		return null;
	}

	/**
	 * Create new Object. 
	 * @param classname Class, may be null
	 * @param defaultClass Class in case of error, may be null
	 * @param params for constructor
	 * @return new instance
	 */
	private Object createInstance(String classname, Class<?> defaultClass, Object[] params) {
		Class<?>[] paramClasses = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			paramClasses[i] = params[i].getClass();
		}
		try {			
			Class<?> clazz = Class.forName(classname);
			Constructor<?> constructor = clazz.getConstructor(paramClasses);
			return constructor.newInstance(params);
		} catch (Throwable e) {
			e.printStackTrace();
			if (defaultClass != null) {
				return createInstance(defaultClass.getName(), null, params);
			} else {
				return null;
			}
		}		
	}

	/**
	 * Parse Configfile, Part LogRotate
	 */
	private void createRotates(Element root, LogSink logsink) {
		NodeList elems = root.getElementsByTagName("logrotate");
		for (int i = 0; i < elems.getLength(); i++) {
			createRotate((Element) elems.item(i), logsink);			
		}
	}

	/**
	 * Parse Configfile, Part LogRotate
	 */
	private void createRotate(Element elem, LogSink logsink) {		
		Properties props = getProperties(elem);
		String classname = elem.getAttribute("class");
		String name = getAttribute(elem, "name", "default");
		logsink.addLogRotate(classname, name, props);
//		return (LogRotate) createInstance(classname, null, new Object[] { name, logsink, props });
	}

	/**
	 * Parse Configfile, Part Properties
	 */
	private Properties getProperties(Element root) {
		NodeList elems = root.getElementsByTagName("property");
		Properties result = new Properties();
		for (int i = 0; i < elems.getLength(); i++) {
			Element elem = (Element) elems.item(i);
			String key = elem.getAttribute("name");
			String value = elem.getAttribute("value");
			result.put(key, value);
		}
		return result;
	}
	
	/**
	 * Get Attribute with default value
	 * @param elem
	 * @param name
	 * @param def in case the attrbute was not found.
	 * @return
	 */
	private String getAttribute(Element elem, String name, String def) {
		String att = elem.getAttribute(name);
		if (att == null) {
			return def;
		} else {
			return att;
		}		
	}
	
	public void log(LogMessage message) {
		for (LogSink sink : logsinks.values()) {
			sink.log(message);
		}		
	}
	
	public static void main(String[] args) {
		
		Thread thread = new Thread("thread") {			
			public void run() {
				instance.log(new LogMessage(LogLevel.L4_INFO,  "LogBootup", "message1"));
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						instance.log(new LogMessageException(LogLevel.L4_INFO,  "LogBootup", e));
					}
					instance.log(new LogMessage(LogLevel.L4_INFO,  "LogBootup", "message1"));
				}
				instance.log(new LogMessage(LogLevel.L4_INFO,  "LogBootup", "message1"));
			}
		};

		thread.start();
		
		instance.log(new LogMessageTrace(LogMessageTrace.ENTRY, "LogBootup", "message", null));		
		instance.log(new LogMessage(LogLevel.L4_INFO,  "LogBootup", "message"));		
		instance.log(new LogMessage(LogLevel.L4_INFO,  "LogBootup", "message1"));		
		instance.log(new LogMessage(LogLevel.L4_INFO,  "LogBootup", "message2"));		
		instance.log(new LogMessage(LogLevel.L5_DEBUG, "LogBootup", "message3"));		
		instance.log(new LogMessage(LogLevel.L5_DEBUG, "LogBootup", "message4"));		
		instance.log(new LogMessage(LogLevel.L5_DEBUG, "LogBootup", "message5"));		
		instance.log(new LogMessage(LogLevel.L5_DEBUG, "LogBootup", "message6"));		
		instance.log(new LogMessage(LogLevel.L3_WARN,  "LogBootup", "message7"));		
		instance.log(new LogMessageTrace(LogMessageTrace.EXIT, "LogBootup", "message", null));		
	}
	
	public void clearQueues() {
		for (LogSink sink : logsinks.values()) {
			sink.clearQueue();
		}		
	}
}
