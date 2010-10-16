package log;

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

import util.SimpleQueue;


class LogSingleton {

	private static LogSingleton instance = new LogSingleton();

	private Map<String, LogSink> logsinks = new HashMap<String, LogSink>();
	
	public LogSingleton() {
		try {
			init("logger.xml");
		} catch (Throwable e) {
			e.printStackTrace();
		}			
	}
	
	private void init(String filename) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fac.newDocumentBuilder();
		Document doc = builder.parse(new File("logger.xml"));		
		Element root = doc.getDocumentElement();		
		createSinks(root);		
	}
	
	/**
	 * Parse Configfile, Part Logsinks
	 */
	private void createSinks(Element root) {
		NodeList elems = root.getElementsByTagName("logsink");
		if (elems != null && elems.getLength() > 0) {
			for (int i = 0; i < elems.getLength(); i++) {
				register(createSink((Element) elems.item(i)));
			}
		} else {
			register(new LogSink("default", LogLevel.L3_WARN, new Properties()));
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
	private LogRotate createRotate(Element elem, LogSink logsink) {		
		Properties props = getProperties(elem);
		String classname = elem.getAttribute("class");
		String name = getAttribute(elem, "name", "default");
		return (LogRotate) createInstance(classname, null, new Object[] { name, logsink, props });
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
		instance.log(new LogMessage(LogLevel.L4_INFO, "LogBootup", "message"));		
		instance.log(new LogMessage(LogLevel.L4_INFO, "LogBootup", "message1"));		
		instance.log(new LogMessage(LogLevel.L4_INFO, "LogBootup", "message2"));		
		instance.log(new LogMessage(LogLevel.L5_DEBUG, "LogBootup", "message3"));		
		instance.log(new LogMessage(LogLevel.L5_DEBUG, "LogBootup", "message4"));		
		instance.log(new LogMessage(LogLevel.L5_DEBUG, "LogBootup", "message5"));		
		instance.log(new LogMessage(LogLevel.L5_DEBUG, "LogBootup", "message6"));		
		instance.log(new LogMessage(LogLevel.L3_WARN, "LogBootup", "message7"));		
	}

}
