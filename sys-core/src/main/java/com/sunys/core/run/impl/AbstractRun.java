package com.sunys.core.run.impl;

import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;

import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.RootGroupRun;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunStatus;
import com.sunys.facade.run.TimeoutCheck;

/**
 * AbstractRun
 * @author sunys
 * @date Dec 21, 2019
 */
public abstract class AbstractRun implements Run {

	private static final LongAdder longAdder = new LongAdder();
	
	private Run proxy;
	
	protected long id;
	
	protected String name;
	
	protected volatile RunStatus status;
	
	protected GroupRun<? extends Run> parent;
	
	protected RootGroupRun<? extends Run> root;
	
	protected Long runDuration;

	protected TimeoutCheck timeoutCheck;

	public AbstractRun() {
		synchronized (AbstractRun.class) {
			longAdder.increment();
			this.id = longAdder.sum();
		}
	}

	@Override
	public Run getProxy() {
		return proxy;
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

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public TimeoutCheck getTimeoutCheck() {
		return timeoutCheck;
	}

	public void setTimeoutCheck(TimeoutCheck timeoutCheck) {
		this.timeoutCheck = timeoutCheck;
	}

	@Override
	public RunStatus getStatus() {
		return status;
	}

	public void setStatus(RunStatus status) {
		Lock lock = timeoutCheck.getLock();
		lock.lock();
		try {
			this.status = status;
		} finally {
			lock.unlock();
		}
	}

}
