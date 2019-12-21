package com.sunys.facade.run;

import java.util.concurrent.Callable;

public interface Run extends Callable<RunStatus> {

	long getId();
	
	String getName();
	
	Run getProxy();
	
	void setProxy(Run proxy);
	
	RunStatus getStatus();
	
	<T> T parents(Class<? extends Run> clazz);
	
	GroupRun<? extends Run> getParent();
	
	RootGroupRun<? extends Run> getRoot();
	
	void init() throws Exception;
	
	void run() throws Exception;
	
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
	
	void destroy();
}
