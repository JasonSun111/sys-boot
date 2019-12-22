package com.sunys.core.run.impl.timeout;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.TimeoutCheck;

/**
 * AbstractTimeoutCheck
 * @author sunys
 * @date Dec 21, 2019
 */
public abstract class AbstractTimeoutCheck implements TimeoutCheck {

	private static final Logger logger = LoggerFactory.getLogger(AbstractTimeoutCheck.class);
	
	private Lock lock = new ReentrantLock();
	
	private Condition condition = lock.newCondition();

	private long timeout;

	private boolean isTimeout;

	private ScheduledFuture<?> timeoutScheduledFuture;

	private Runnable timeoutHandler;

	public AbstractTimeoutCheck(Lock lock, long timeout, Runnable timeoutHandler) {
		this.lock = lock;
		this.condition = lock.newCondition();
		this.timeout = timeout;
		this.timeoutHandler = timeoutHandler;
	}
	
	public AbstractTimeoutCheck(long timeout, Runnable timeoutHandler) {
		this.timeout = timeout;
		this.timeoutHandler = timeoutHandler;
	}

	/**
	 * 获取定时任务线程池
	 * @return
	 */
	protected abstract ScheduledExecutorService getScheduled();

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

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	@Override
	public void startCheckTimeout() {
		startCheckTimeout(null);
	}
	
	@Override
	public void startCheckTimeout(Long timeout) {
		startCheckTimeout(timeout, TimeUnit.SECONDS);
	}
	
	@Override
	public void startCheckTimeout(Long timeout, TimeUnit unit) {
		if (timeout == null) {
			timeout = getTimeout();
		}
		if (unit == null) {
			unit = TimeUnit.SECONDS;
		}
		if (timeout != null && timeout > 0) {
			ScheduledExecutorService scheduledExecutorService = getScheduled();
			timeoutScheduledFuture = scheduledExecutorService.schedule(this::timeout, timeout, unit);
		} else {
			logger.error("start check timeout error, timeout:{}", timeout);
		}
	}

	private void timeout() {
		lock.lock();
		try {
			isTimeout = true;
			if (timeoutHandler != null) {
				timeoutHandler.run();
			}
			condition.signalAll();
			logger.info("timeout signalAll...");
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void cancelCheckTimeout(boolean flag) {
		if (timeoutScheduledFuture != null && !timeoutScheduledFuture.isCancelled()) {
			timeoutScheduledFuture.cancel(flag);
			logger.info("cancelCheckTimeout, flag:{}", flag);
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
			logger.info("delay time {}, value:{}", unit, delay);
			return delay;
		}
		return null;
	}

	@Override
	public void await() throws InterruptedException {
		lock.lock();
		try {
			logger.info("wait");
			condition.await();
			logger.info("notify");
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean await(int sec) throws InterruptedException {
		lock.lock();
		try {
			logger.info("wait sec:{}", sec);
			boolean flag = condition.await(sec, TimeUnit.SECONDS);
			logger.info("notify, flag:{}", flag);
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
			logger.info("signal...");
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void signalAll() {
		lock.lock();
		try {
			condition.signalAll();
			logger.info("signalAll...");
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void reset() {
		lock.lock();
		try {
			isTimeout = false;
			cancelCheckTimeout(true);
			condition.signalAll();
			logger.info("reset signalAll...");
		} finally {
			lock.unlock();
		}
	}

}
