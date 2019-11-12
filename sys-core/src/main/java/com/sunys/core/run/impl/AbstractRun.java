package com.sunys.core.run.impl;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.RootGroupRun;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunStatus;

public abstract class AbstractRun implements Run {

	private static final LongAdder longAdder = new LongAdder();
	
	private Run proxy;
	
	protected Long id;
	
	protected String name;
	
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

	public abstract ScheduledExecutorService getScheduled();
	
	@Override
	public void startCheckTimeout() {
		startCheckTimeout(null);
	}
	
	@Override
	public void startCheckTimeout(Long timeout) {
		if (timeout == null) {
			timeout = getTimeout();
		}
		if (timeout != null && timeout > 0) {
			ScheduledExecutorService scheduledExecutorService = getScheduled();
			timeoutScheduledFuture = scheduledExecutorService.schedule(this::timeout, timeout, TimeUnit.SECONDS);
		}
	}

	@Override
	public void cancelCheckTimeout() {
		if (timeoutScheduledFuture != null && !timeoutScheduledFuture.isCancelled()) {
			timeoutScheduledFuture.cancel(false);
		}
	}

	@Override
	public boolean isTimeout() {
		return isTimeout;
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
			isTimeout = true;
			timeoutHandle();
			condition.signal();
		} finally {
			lock.unlock();
		}
	}

	public abstract void timeoutHandle();

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
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	@Override
	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

}
