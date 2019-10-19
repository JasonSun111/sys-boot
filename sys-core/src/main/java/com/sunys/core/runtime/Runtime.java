package com.sunys.core.runtime;

public interface Runtime {

	RuntimeStatus getStatus();
	
	Runtime getParent();
	
	void init();
	
	void run();
	
	void clean();
	
	void reset();
	
	void destory();
}
