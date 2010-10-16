package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * This queue is not thread save. Use ThreadLocalQueue or SyncedQueue instead, if your application logs from multiple threads. 
 * @author jtisje
 *
 * @param <O>
 */
public class FixedArrayQueue<O> implements Queue<O> , Collection<O>, SimpleQueue<O> {

	private int position = 0;
	private O[] array = null;
	private int size = 0;

	@SuppressWarnings("unchecked")
	public FixedArrayQueue(int size) {
		array = (O[])new Object[size];
	}

	private int getAbsoluteStart() {
		int result = (position - size + array.length) % array.length;
		assert result >= 0;
		assert result < array.length;
		return result;
	}
	
	public boolean add(O obj) {
		array[position]=obj;
		incPosition();
		incSize();
		return true;
	}

	private void incPosition() {
		position++;
		if (position >= array.length) {
			position = 0;
		}
		assert position < array.length;
	}

	private void incSize() {
		if (size < array.length) {
			size++;
		}
		assert size < array.length;
	}

	private void decSize() {
		if (size > 0) {
			size--;
		}
		assert size >= 0;
	}

	public O element() {
		if (size == 0) { throw new NoSuchElementException(); }
		return array[getAbsoluteStart()];
	}


	public boolean offer(O o) {
		// TODO Auto-generated method stub
		return false;
	}

	public O peek() {
		if (size > 0) {
			return array[getAbsoluteStart()];
		} else {
			return null;
		}
	}

	public O poll() {
		if (size > 0) {
			O result = array[getAbsoluteStart()];
			decSize();
			return result;
		} else {
			return null;
		}
	}

	public O remove() {
		return dequeue();
	}

	public O dequeue() {
		if (size == 0) { throw new NoSuchElementException(); }
		O result = array[getAbsoluteStart()];
		decSize();
		return result;
	}

	public boolean addAll(Collection<? extends O> collection) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		position = 0;
		size = 0;
	}

	public boolean contains(Object obj) {
		for (int i = 0; i < array.length; i++) {
//			if(obj.equals(obj))
		}
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean containsAll(Collection<?> collection) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Iterator<O> iterator() {
		ArrayList<O> result = new ArrayList<O>(size);

//		result.add
		return result.iterator();
	}

	public boolean remove(Object obj) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return array.length;
	}

	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("hiding")
	public <O> O[] toArray(O[] a) {
		throw new UnsupportedOperationException();
	}

	public static void main(String[] args) {
		System.out.println(1%3);
		System.out.println(2%3);
		System.out.println(3%3);
		System.out.println(4%3);
		System.out.println(5%3);
		System.out.println(0%3);
		System.out.println(-1%3);
		System.out.println(-2%3);
		System.out.println(-3%3);
		System.out.println(-4%3);
	}

	public Object getLock() {
		return this;
	}

}
