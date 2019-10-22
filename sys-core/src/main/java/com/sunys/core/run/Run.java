package com.sunys.core.run;

public interface Run extends Runnable {

	RunStatus getStatus();
	
	Run getParent();
	
	void init();
	
	@Override
	void run();
	
	void clean();
	
	void reset();
	
	long calculateRunDuration();
	
	void destory();
}
