package log;

import java.io.BufferedWriter;
import java.io.PrintWriter;

/**
 * Needed for externally locking access to Writer while rotating logfiles.
 * @author jtisje
 *
 */
public class LockablePrintWriter extends PrintWriter {
	
	public LockablePrintWriter(BufferedWriter bufferedWriter) {
		super(bufferedWriter);
	}

	public Object getLock() {
		return lock;	 
	}

}
