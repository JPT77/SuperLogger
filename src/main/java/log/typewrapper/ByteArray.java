package log.typewrapper;

import util.StringUtils;

public class ByteArray extends ScalarArray {

	private byte[] data;

	public ByteArray(byte [] data) {
		this.data = data;
	}

	public String toString() {
		return toString(data);
	}

	/**
	 * Converts an array of bytes to a string of two-character hex values.
	 *
	 * @param bytes
	 *            the byte array
	 *
	 * @return the string
	 */
	protected static String toString(byte[] bytes) {
		if (bytes == null) {
			return "null";
		}

		StringBuffer buf = new StringBuffer(bytes.length * 4);
		buf.append("(byte[]) 0x[");
		for (int i = 0; i < bytes.length; i++) {
			buf.append(StringUtils.byteString(bytes[i]));
			if (i < bytes.length - 1){
				buf.append(", ");
			}
		}
		buf.append(']');
		return buf.toString();
	}

}
