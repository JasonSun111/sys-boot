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
	
	protected Long id;
	
	protected final Lock lock = new ReentrantLock();
	
	protected volatile RunStatus status;
	
	protected GroupRun<Run> parent;
	
	protected RootGroupRun<Run> root;
	
	protected Long runDuration;

	public AbstractRun() {
		longAdder.increment();
		this.id = longAdder.sum();
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
	public GroupRun<Run> getParent() {
		return parent;
	}

	public void setParent(GroupRun<Run> parent) {
		this.parent = parent;
	}
	
	@Override
	public RootGroupRun<Run> getRoot() {
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
