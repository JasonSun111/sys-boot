package com.sunys.core.run.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.sunys.core.run.Run;
import com.sunys.core.run.RunStatus;

public abstract class AbstractRun implements Run {

	protected Long id;
	
	protected final Lock lock = new ReentrantLock();
	
	protected volatile RunStatus status;
	
	protected Run parent;
	
	protected Long runDuration;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Run getParent() {
		return parent;
	}

	public void setParent(Run parent) {
		this.parent = parent;
	}

	@Override
	public RunStatus getStatus() {
		return status;
	}

	public void setStatus(RunStatus status) {
		this.status = status;
	}

}
