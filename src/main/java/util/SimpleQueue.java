package util;

public interface SimpleQueue<O> {

	public void clear();

	public boolean add(O obj);

	public O dequeue();

	public boolean isEmpty();

	/**
	 * Gets the lock for manually locking access to queue
	 * @return
	 */
	public Object getLock();

}
