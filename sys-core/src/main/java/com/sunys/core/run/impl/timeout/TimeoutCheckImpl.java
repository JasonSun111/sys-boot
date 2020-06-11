package com.sunys.core.run.impl.timeout;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.TimeoutCheck;

/**
 * TimeoutCheckImpl
 * @author sunys
 * @date Dec 21, 2019
 */
public class TimeoutCheckImpl implements TimeoutCheck {

	private static final Logger logger = LoggerFactory.getLogger(TimeoutCheckImpl.class);
	
	private long timeout;
	
	private TimeUnit unit;

	private volatile boolean isTimeout;

	private Runnable timeoutHandler;
	
	private ScheduledExecutorService scheduled;
	
	private ScheduledFuture<?> timeoutScheduledFuture;

	public TimeoutCheckImpl(ScheduledExecutorService scheduled, long timeout) {
		this(scheduled, timeout, null);
	}

	public TimeoutCheckImpl(ScheduledExecutorService scheduled, long timeout, TimeUnit unit) {
		this(scheduled, timeout, unit, null);
	}

	public TimeoutCheckImpl(ScheduledExecutorService scheduled, long timeout, TimeUnit unit, Runnable timeoutHandler) {
		this.scheduled = scheduled;
		this.timeout = timeout;
		if (unit == null) {
			unit = TimeUnit.SECONDS;
		}
		this.unit = unit;
		this.timeoutHandler = timeoutHandler;
	}

	@Override
	public long getTimeout() {
		return timeout;
	}

	@Override
	public void startCheck() {
		isTimeout = false;
		timeoutScheduledFuture = scheduled.schedule(this::timeout, timeout, unit);
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
