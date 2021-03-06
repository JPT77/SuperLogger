package de.jpt.superlogger;

import java.io.BufferedWriter;
import java.io.PrintWriter;

/**
 * Needed for externally locking access to Writer while rotating logfiles.
 * @author jpt
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
