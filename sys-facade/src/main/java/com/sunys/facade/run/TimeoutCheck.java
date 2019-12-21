package com.sunys.facade.run;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public interface TimeoutCheck {

	Lock getLock();
	
	Condition getDefaultCondition();
	
	Condition newCondition();
	
	long getTimeout();
	
	boolean isTimeout();
	
	Long getDelay(TimeUnit unit);
	
	void startCheckTimeout();
	
	void startCheckTimeout(Long timeout);
	
	void startCheckTimeout(Long timeout, TimeUnit unit);
	
	void cancelCheckTimeout(boolean flag);
	
	void await() throws InterruptedException;
	
	boolean await(int sec) throws InterruptedException;
	
	void signal();
	
	void signalAll();
	
	void reset();
}
