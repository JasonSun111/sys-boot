package com.sunys.core.run.impl;

import java.util.concurrent.atomic.LongAdder;

import com.sunys.facade.run.Run;

/**
 * AbstractRun
 * @author sunys
 * @date Dec 21, 2019
 */
public abstract class AbstractRun implements Run {

	private static final LongAdder longAdder = new LongAdder();
	
	protected long id;
	
	public AbstractRun() {
		synchronized (AbstractRun.class) {
			longAdder.increment();
			this.id = longAdder.sum();
		}
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
