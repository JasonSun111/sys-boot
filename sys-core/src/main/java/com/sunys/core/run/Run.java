package com.sunys.core.run;

import java.util.concurrent.Callable;

public interface Run extends Callable<RunStatus> {

	RunStatus getStatus();
	
	GroupRun<Run> getParent();
	
	<T> T parents(Class<? extends Run> clazz);
	
	RootGroupRun<Run> getRoot();
	
	void init();
	
	void run();
	
	@Override
	default RunStatus call() throws Exception {
		run();
		return getStatus();
	}
	
	void clean();
	
	void reset();
	
	long calculateRunDuration();
	
	void destory();
}
