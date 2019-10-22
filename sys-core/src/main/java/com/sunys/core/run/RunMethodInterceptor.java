package com.sunys.core.run;

public interface RunMethodInterceptor {

	void setRun(Run run);
	
	Object intercept(RunChain runChain) throws Exception;
}
