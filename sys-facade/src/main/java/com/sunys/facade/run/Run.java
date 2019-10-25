package com.sunys.facade.run;

import java.util.concurrent.Callable;

public interface Run extends Callable<RunStatus> {

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
		Run run = getProxy();
		if (run != null) {
			run.run();
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
