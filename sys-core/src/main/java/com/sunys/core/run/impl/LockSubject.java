package com.sunys.core.run.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sunys.facade.run.Event;
import com.sunys.facade.run.EventHandler;
import com.sunys.facade.run.EventType;
import com.sunys.facade.run.Subject;

/**
 * LockSubject
 * @author sunys
 * @date May 12, 2020
 */
public class LockSubject implements Subject {

	private ReadWriteLock rwlock = new ReentrantReadWriteLock();
	private Lock rlock = rwlock.readLock();
	private Lock wlock = rwlock.writeLock();
	
	private Subject subject;
	
	public LockSubject(Subject subject) {
		this.subject = subject;
	}

	public LockSubject() {
		subject = new SubjectImpl();
	}

	@Override
	public void registEventHandler(EventType type, EventHandler<?> eventHandler) {
		wlock.lock();
		try {
			subject.registEventHandler(type, eventHandler);
		} finally {
			wlock.unlock();
		}
	}

	@Override
	public void removeEventHandler(EventType type) {
		wlock.lock();
		try {
			subject.removeEventHandler(type);
		} finally {
			wlock.unlock();
		}
	}

	@Override
	public void removeEventHandler(EventHandler<?> eventHandler) {
		wlock.lock();
		try {
			subject.removeEventHandler(eventHandler);
		} finally {
			wlock.unlock();
		}
	}

	@Override
	public <P> void event(Event<P> event) {
		rlock.lock();
		try {
			subject.event(event);
		} finally {
			rlock.unlock();
		}
	}

}
