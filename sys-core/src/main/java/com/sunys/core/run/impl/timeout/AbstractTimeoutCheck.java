package com.sunys.core.run.impl.timeout;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
	
	private long timeout;
	
	private TimeUnit unit;

	private volatile boolean isTimeout;

	private Runnable timeoutHandler;
	
	private ScheduledFuture<?> timeoutScheduledFuture;

	public AbstractTimeoutCheck(long timeout) {
		this.timeout = timeout;
		this.unit = TimeUnit.SECONDS;
	}

	public AbstractTimeoutCheck(long timeout, TimeUnit unit) {
		this.timeout = timeout;
		this.unit = unit;
	}

	public AbstractTimeoutCheck(long timeout, TimeUnit unit, Runnable timeoutHandler) {
		this.timeout = timeout;
		this.unit = unit;
		this.timeoutHandler = timeoutHandler;
	}

	/**
	 * 获取定时任务线程池
	 * @return
	 */
	protected abstract ScheduledExecutorService getScheduled();

	@Override
	public long getTimeout() {
		return timeout;
	}

	@Override
	public void startCheck() {
		ScheduledExecutorService scheduledExecutorService = getScheduled();
		isTimeout = false;
		timeoutScheduledFuture = scheduledExecutorService.schedule(this::timeout, timeout, unit);
	}

	private void timeout() {
		isTimeout = true;
		if (timeoutHandler != null) {
			timeoutHandler.run();
		}
	}

	@Override
	public void cancelCheck(boolean flag) {
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
	public long getDelay() {
		if (timeoutScheduledFuture != null) {
			long delay = timeoutScheduledFuture.getDelay(unit);
			logger.info("delay time {}, value:{}", unit, delay);
			return delay;
		}
		return 0L;
	}
	
	@Override
	public long getDelay(TimeUnit unit) {
		if (timeoutScheduledFuture != null) {
			long delay = timeoutScheduledFuture.getDelay(unit);
			logger.info("delay time {}, value:{}", unit, delay);
			return delay;
		}
		return 0L;
	}

}
