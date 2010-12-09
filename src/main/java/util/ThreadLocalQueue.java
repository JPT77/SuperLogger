package util;


class ThreadLocalQueue<O> extends ThreadLocal<SimpleQueue<O>> implements SimpleQueue<O> {

	private int queuesize;

	public ThreadLocalQueue(String queuesize) {
		this(Integer.parseInt(queuesize));
	}

	public ThreadLocalQueue(int queuesize) {
		this.queuesize = queuesize;
	}

	private SimpleQueue<O> getQueue() {
		SimpleQueue<O> result = get();
		if (result == null) {
			result = new FixedArrayQueue<O>(queuesize);
			set(result);
		}
		return result;
	}

	public boolean add(O o) {
		return getQueue().add(o);
	}

	public void clear() {
		getQueue().clear();
	}

	public O dequeue() {
		return getQueue().dequeue();
	}

	public boolean isEmpty() {
		return getQueue().isEmpty();
	}

	public Object getLock() {
		return this;
	}

}
