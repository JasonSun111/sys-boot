package com.sunys.core.run.impl.interceptor;

import com.sunys.core.run.RunContext;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunChain;
import com.sunys.facade.run.RunMethodInterceptor;

public class ContextRunMethodInterceptor implements RunMethodInterceptor {

	@Override
	public Object intercept(RunChain runChain) throws Exception {
		Run run = runChain.getRun();
		RunContext.pushRun(run);
		Object obj = runChain.invoke();
		RunContext.popRun();
		return obj;
	}

}
