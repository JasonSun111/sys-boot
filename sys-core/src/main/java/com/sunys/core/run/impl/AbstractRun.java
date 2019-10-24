package com.sunys.core.run.impl;

import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.sunys.core.run.GroupRun;
import com.sunys.core.run.RootGroupRun;
import com.sunys.core.run.Run;
import com.sunys.core.run.RunStatus;

public abstract class AbstractRun implements Run {

	private static final LongAdder longAdder = new LongAdder();
	
	private Run proxy;
	
	protected Long id;
	
	protected final Lock lock = new ReentrantLock();
	
	protected volatile RunStatus status;
	
	protected GroupRun<? extends Run> parent;
	
	protected RootGroupRun<? extends Run> root;
	
	protected Long runDuration;

	public AbstractRun() {
		longAdder.increment();
		this.id = longAdder.sum();
	}

	@Override
	public Run getProxy() {
		if (proxy != null) {
			return proxy;
		}
		return this;
	}

	@Override
	public void setProxy(Run proxy) {
		this.proxy = proxy;
	}

	@Override
	public <T> T parents(Class<? extends Run> clazz) {
		if (parent != null) {
			if (clazz.isInstance(parent)) {
				return (T) parent;
			}
			T t = parent.parents(clazz);
			return t;
		}
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public GroupRun<? extends Run> getParent() {
		return parent;
	}

	public void setParent(GroupRun<? extends Run> parent) {
		this.parent = parent;
	}
	
	@Override
	public RootGroupRun<? extends Run> getRoot() {
		return root;
	}
	
	public void setRoot(RootGroupRun<Run> root) {
		this.root = root;
	}

	@Override
	public RunStatus getStatus() {
		return status;
	}

	public void setStatus(RunStatus status) {
		lock.lock();
		try {
			this.status = status;
		} finally {
			lock.unlock();
		}
	}

}
