package com.sunys.core.test.run;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.sunys.core.run.impl.timeout.AbstractTimeoutCheck;

public class TimeoutCheckImpl extends AbstractTimeoutCheck {

	private ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
	
	public TimeoutCheckImpl(long timeout, Runnable timeoutHandler) {
		super(timeout, timeoutHandler);
	}

	@Override
	protected ScheduledExecutorService getScheduled() {
		return scheduled;
	}

	public void setScheduled(ScheduledExecutorService scheduled) {
		this.scheduled = scheduled;
	}
}
