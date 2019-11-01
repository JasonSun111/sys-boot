package com.sunys.facade.run;

import java.util.concurrent.Callable;

public interface Run extends Callable<RunStatus> {

	Long getId();
	
	String getName();
	
	long getTimeout();
	
	boolean isTimeout();
	
	void startCheckTimeout();
	
	void startCheckTimeout(Long timeout);
	
	void cancelCheckTimeout();
	
	void await() throws InterruptedException;
	
	boolean await(int sec) throws InterruptedException;
	
	Run getProxy();
	
	void setProxy(Run proxy);
	
	RunStatus getStatus();
	
	<T> T parents(Class<? extends Run> clazz);
	
	GroupRun<? extends Run> getParent();
	
	RootGroupRun<? extends Run> getRoot();
	
	void init();
	
	void run();
	
	@Override
	default RunStatus call() throws Exception {
		Run proxy = getProxy();
		if (proxy != null) {
			proxy.run();
		} else {
			run();
		}
		return getStatus();
	}
	
	void clean();
	
	void reset();
	
	long calculateRunDuration();
	
	void destory();
}
