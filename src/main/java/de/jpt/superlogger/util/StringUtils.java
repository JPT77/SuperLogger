package de.jpt.superlogger.util;

public class StringUtils {

	/**
	 * The characters to use to represent the values of a byte.
	 */
	private static final char[] BYTE_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Converts a <code>byte</code> to a hex string.
	 * 
	 * @param b
	 *            the byte
	 * 
	 * @return the string
	 */
	public static String toString(byte b) {
		return "0x" + byteString(b);
	}
	
	/**
	 * Returns the two hex characters that represent the given byte.
	 * 
	 * @param b
	 *            the byte
	 * 
	 * @return the two hex characters that represent the byte.
	 */
	public static String byteString(byte b) {
		return new String(new char[] { BYTE_CHARS[(b >> 4) & 0x0F], BYTE_CHARS[b & 0x0F] });
	}
	
}
