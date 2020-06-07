package com.sunys.core.util;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class LimitQueue<T> implements Deque<T> {

	private LinkedList<T> linkedList = new LinkedList<>();
	
	private int maxCount;
	
	public LimitQueue(int maxCount) {
		this.maxCount = maxCount;
	}

	private void removeEnd() {
		while (size() >= maxCount) {
			pollLast();
		}
	}
	
	private void removeStart() {
		while (size() >= maxCount) {
			pollFirst();
		}
	}

	@Override
	public int size() {
		return linkedList.size();
	}

	@Override
	public boolean isEmpty() {
		return linkedList.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return linkedList.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return linkedList.iterator();
	}

	@Override
	public Object[] toArray() {
		return linkedList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return linkedList.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return linkedList.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return linkedList.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return linkedList.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return linkedList.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return linkedList.retainAll(c);
	}

	@Override
	public void clear() {
		linkedList.clear();
	}

	@Override
	public boolean add(T e) {
		removeStart();
		return linkedList.add(e);
	}

	@Override
	public boolean offer(T e) {
		removeStart();
		return linkedList.offer(e);
	}

	@Override
	public T remove() {
		return linkedList.remove();
	}

	@Override
	public T poll() {
		return linkedList.poll();
	}

	@Override
	public T element() {
		return linkedList.element();
	}

	@Override
	public T peek() {
		return linkedList.peek();
	}

	@Override
	public void addFirst(T e) {
		removeEnd();
		linkedList.addFirst(e);
	}

	@Override
	public void addLast(T e) {
		removeStart();
		linkedList.addLast(e);
	}

	@Override
	public boolean offerFirst(T e) {
		removeEnd();
		return linkedList.offerFirst(e);
	}

	@Override
	public boolean offerLast(T e) {
		removeStart();
		return linkedList.offerLast(e);
	}

	@Override
	public T removeFirst() {
		return linkedList.removeFirst();
	}

	@Override
	public T removeLast() {
		return linkedList.removeLast();
	}

	@Override
	public T pollFirst() {
		return linkedList.pollFirst();
	}

	@Override
	public T pollLast() {
		return linkedList.pollLast();
	}

	@Override
	public T getFirst() {
		return linkedList.getFirst();
	}

	@Override
	public T getLast() {
		return linkedList.getLast();
	}

	@Override
	public T peekFirst() {
		return linkedList.peekFirst();
	}

	@Override
	public T peekLast() {
		return linkedList.peekLast();
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		return linkedList.removeFirstOccurrence(o);
	}

	@Override
	public boolean removeLastOccurrence(Object o) {
		return linkedList.removeLastOccurrence(o);
	}

	@Override
	public void push(T e) {
		addFirst(e);
	}

	@Override
	public T pop() {
		return linkedList.pop();
	}

	@Override
	public Iterator<T> descendingIterator() {
		return linkedList.descendingIterator();
	}

}
