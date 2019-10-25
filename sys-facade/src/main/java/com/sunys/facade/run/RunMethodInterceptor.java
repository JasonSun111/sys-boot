package com.sunys.facade.run;

public interface RunMethodInterceptor {

	void setRun(Run run);
	
	Object intercept(RunChain runChain) throws Exception;
}
