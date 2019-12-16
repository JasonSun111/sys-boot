package com.sunys.core.run.impl.timeout;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.sunys.facade.run.TimeoutCheckHandler;

public abstract class AbstractTimeoutCheckHandler implements TimeoutCheckHandler {

	private Lock lock = new ReentrantLock();
	
	private Condition condition = lock.newCondition();

	private long timeout;

	private boolean isTimeout;

	private ScheduledFuture<?> timeoutScheduledFuture;

	private Runnable timeoutHandle;

	public AbstractTimeoutCheckHandler(Lock lock, long timeout, Runnable timeoutHandle) {
		this.lock = lock;
		this.condition = lock.newCondition();
		this.timeout = timeout;
	}

	@Override
	public Lock getLock() {
		return lock;
	}

	@Override
	public Condition getDefaultCondition() {
		return condition;
	}

	@Override
	public Condition newCondition() {
		return lock.newCondition();
	}

	@Override
	public long getTimeout() {
		return timeout;
	}

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

	private void timeout() {
		lock.lock();
		try {
			isTimeout = true;
			if (timeoutHandle != null) {
				timeoutHandle.run();
			}
			condition.signal();
		} finally {
			lock.unlock();
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
	public Long getDelay(TimeUnit unit) {
		if (timeoutScheduledFuture != null) {
			long delay = timeoutScheduledFuture.getDelay(unit);
			return delay;
		}
		return null;
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

	@Override
	public void signal() {
		lock.lock();
		try {
			condition.signal();
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void signalAll() {
		lock.lock();
		try {
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void reset() {
		lock.lock();
		try {
			isTimeout = false;
			cancelCheckTimeout();
		} finally {
			lock.unlock();
		}
	}

}
