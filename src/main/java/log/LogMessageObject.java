package log;

public class LogMessageObject extends LogMessage {

	private Object data;

	public LogMessageObject(LogLevel level, String classname, String message, Object data) {
		super(level, classname, message);
		this.data = data;
	}

	protected char getType() {
		return 'o';
	}

	protected void appendParams(StringBuffer buffer) {
		// TODO Object ausgeben
		buffer.append(MESSAGE_SEPARATOR);
		if (data instanceof Object[]) {
			buffer.append('[');
			appendArrayContent(buffer, (Object[]) data);
			buffer.append(']');
		} else {
			buffer.append(data);
		}
//		buffer.append(data.toString());
	}
	
}
