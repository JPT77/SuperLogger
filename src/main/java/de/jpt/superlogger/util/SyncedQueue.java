package de.jpt.superlogger.util;


/**
 * Attention: Does not sync the Collection-Interface. SimpleQueue only. 
 * @author jpt
 *
 * @param <O>
 */
public class SyncedQueue<O> extends FixedArrayQueue<O> implements SimpleQueue<O>{

	public SyncedQueue(String queuesize) {
		this(Integer.parseInt(queuesize));
	}

	public SyncedQueue(int queuesize) {
		super(queuesize);
	}

	public synchronized boolean add(O o) {
		return super.add(o);
	}

	public synchronized void clear() {
		super.clear();
	}

	public synchronized O dequeue() {
		return super.dequeue();
	}
	
}
