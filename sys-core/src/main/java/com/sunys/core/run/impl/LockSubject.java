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

	private Lock rlock;
	private Lock wlock;
	
	private Subject subject;
	
	public LockSubject(ReadWriteLock rwlock, Subject subject) {
		if (rwlock == null) {
			rwlock = new ReentrantReadWriteLock();
		}
		if (subject == null) {
			subject = new SubjectImpl();
		}
		this.rlock = rwlock.readLock();
		this.wlock = rwlock.writeLock();
		this.subject = subject;
	}
	
	public LockSubject(Subject subject) {
		this(null, subject);
	}
	
	public LockSubject(ReadWriteLock rwlock) {
		this(rwlock, null);
	}
	
	public LockSubject() {
		this(null, null);
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
