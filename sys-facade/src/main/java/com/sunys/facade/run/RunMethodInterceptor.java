package com.sunys.facade.run;

public interface RunMethodInterceptor {

	Object intercept(RunChain runChain) throws Exception;
	
	void setRun(Run run);
}
