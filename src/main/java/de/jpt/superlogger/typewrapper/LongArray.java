package de.jpt.superlogger.typewrapper;

public class LongArray extends ScalarArray {
	
	private long[] data;

	public LongArray(long [] data) {
		this.data = data;
	}

	public String toString() {
		if (data == null) {
			return "null";
		}

		StringBuffer buf = new StringBuffer(data.length * 4);
		buf.append("(long[]) [");
		for (int i = 0; i < data.length; i++) {
			buf.append(data[i]);
			if (i < data.length - 1){
				buf.append(", ");
			}
		}
		buf.append(']');
		return buf.toString();
	}

}
