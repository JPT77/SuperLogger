package log;

public class LogMessageTrace extends LogMessage {

	public static final int ENTRY  = 1;
	public static final int EXIT   = 2;
	public static final int START  = 3;
	public static final int FINISH = 4;
	
	private Object data = null;
	private int type;
	
	public LogMessageTrace(int type, String classname, String message, Object data) {
		super(LogLevel.FAKE_TRACE, classname, message);
		this.type = type;
		this.data = data;
	}

	protected char getType() {
		switch (type) {
			case ENTRY : return '>';
			case EXIT  : return '<';
			case START : return '>';
			case FINISH: return '<';
			default: return '?';
		}
	}
	
	protected void appendParams(StringBuffer buffer) {
		if (data != null) {
			switch (type) {
				case ENTRY :
				case START : 
					buffer.append('(');
					if (data instanceof Object[]) {
						appendArrayContent(buffer, (Object[]) data);
					} else {
						buffer.append(data);
					}
					buffer.append(')');
					break;
				case EXIT  : 
				case FINISH: 
					buffer.append("(...) = ");
					if (data instanceof Object[]) {
						buffer.append('[');
						appendArrayContent(buffer, (Object[]) data);
						buffer.append(']');
					} else {
						buffer.append(data);
					}
					break;
				default: 
					if (data instanceof Object[]) {
						appendArrayContent(buffer, (Object[]) data);
					} else {
						buffer.append(data);
					}
					break;
			}
		} else {
			switch (type) {
				case ENTRY :
				case START :
					buffer.append("()");
					break;
				case EXIT  :
				case FINISH: 
					break;
				default: 
					break;
			}
		}
	}
	
}
