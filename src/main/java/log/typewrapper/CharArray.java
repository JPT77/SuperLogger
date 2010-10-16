package log.typewrapper;

public class CharArray extends ScalarArray {
	
	private char[] data;

	public CharArray(char [] data) {
		this.data = data;
	}

	public String toString() {
		return "(char[]) " + new String(data).replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t");
	}
	
}