package com.sunys.core.run.impl;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.sunys.core.run.RunContext;
import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.RootGroupRun;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunStatus;

public abstract class AbstractRun implements Run {

	private static final LongAdder longAdder = new LongAdder();
	
	private Run proxy;
	
	protected Long id;
	
	protected final Lock lock = new ReentrantLock();
	
	protected Condition condition = lock.newCondition();
	
	protected volatile RunStatus status;
	
	protected GroupRun<? extends Run> parent;
	
	protected RootGroupRun<? extends Run> root;
	
	protected Long runDuration;
	
	protected long timeout;
	
	protected boolean isTimeout;
	
	private ScheduledFuture<?> timeoutScheduledFuture;

	public AbstractRun() {
		longAdder.increment();
		this.id = longAdder.sum();
	}

	@Override
	public void startCheckTimeout() {
		ScheduledExecutorService scheduledExecutorService = RunContext.getScheduledExecutorService();
		timeoutScheduledFuture = scheduledExecutorService.schedule(this::timeout, getTimeout(), TimeUnit.SECONDS);
	}

	@Override
	public void cancelCheckTimeout() {
		if (timeoutScheduledFuture != null && !timeoutScheduledFuture.isCancelled()) {
			timeoutScheduledFuture.cancel(false);
			isTimeout = false;
		}
	}

	@Override
	public void await() throws InterruptedException {
		lock.lock();
		try {
			condition.await();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean await(int sec) throws InterruptedException {
		lock.lock();
		try {
			boolean flag = condition.await(sec, TimeUnit.SECONDS);
			return flag;
		} finally {
			lock.unlock();
		}
	}

	private void timeout() {
		lock.lock();
		try {
			timeoutHandle();
			isTimeout = true;
			condition.signal();
		} finally {
			lock.unlock();
		}
	}

	public abstract void timeoutHandle();

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

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
