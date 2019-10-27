package com.sunys.core.run.impl.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunChain;
import com.sunys.facade.run.RunMethodInterceptor;

public class LogRunMethodInterceptor implements RunMethodInterceptor {

	private Run run;
	private Logger logger;

	@Override
	public Object intercept(RunChain runChain) throws Exception {
		String methodName = runChain.getMethod().getName();
		logger.info("{} {} run start", run.getClass().getSimpleName(), methodName);
		Object obj = runChain.invoke();
		logger.info("{} {} run end", run.getClass().getSimpleName(), methodName);
		return obj;
	}

	@Override
	public void setRun(Run run) {
		this.run = run;
		this.logger = LoggerFactory.getLogger(run.getClass());
	}

}
