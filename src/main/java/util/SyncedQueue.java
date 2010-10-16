package util;


public class SyncedQueue<O> extends ThreadLocal<SimpleQueue<O>> implements SimpleQueue<O> {
	
	private int queuesize;

	public SyncedQueue(String queuesize) {
		this(Integer.parseInt(queuesize));
	}

	public SyncedQueue(int queuesize) {
		this.queuesize = queuesize;
	}
	
	private synchronized SimpleQueue<O> getQueue() {
		SimpleQueue<O> result = get();
		if (result == null) {
			result = new FixedArrayQueue<O>(queuesize);
			set(result);
		}
		return result;
	}

	public synchronized boolean add(O o) {
		return getQueue().add(o);
	}
	
	public synchronized void clear() {
		getQueue().clear();
	}
	
	public synchronized O dequeue() {
		return getQueue().dequeue();
	}

	public synchronized boolean isEmpty() {
		return getQueue().isEmpty();
	}

	public Object getLock() {
		return getQueue();
	}
	
}
