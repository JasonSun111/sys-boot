package com.sunys.core.runtime.impl;

import java.util.concurrent.locks.Lock;

import java.util.concurrent.locks.ReentrantLock;

import com.sunys.core.runtime.Runtime;
import com.sunys.core.runtime.RuntimeStatus;

public class AbstractRuntime implements Runtime {

	protected Long runtimeId;
	
	protected final Lock lock = new ReentrantLock();
	
	protected volatile RuntimeStatus status;
	
	protected Runtime parent;
	
	@Override
	public RuntimeStatus getStatus() {
		return status;
	}

	@Override
	public Runtime getParent() {
		return parent;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destory() {
		// TODO Auto-generated method stub
		
	}

}
