package com.sunys.core.run.impl.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunChain;
import com.sunys.facade.run.RunMethodInterceptor;

/**
 * LogRunMethodInterceptor
 * @author sunys
 * @date Dec 21, 2019
 */
public class LogRunMethodInterceptor implements RunMethodInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LogRunMethodInterceptor.class);
	
	@Override
	public Object intercept(RunChain runChain) throws Exception {
		Run run = runChain.getTarget();
		String methodName = runChain.getMethod().getName();
		logger.info("Class:{}, method:{}, name:{}, id:{}, run start", run.getClass().getSimpleName(), methodName, run.getName(), run.getId());
		Object obj = runChain.invoke();
		logger.info("Class:{}, method:{}, name:{}, id:{}, run end", run.getClass().getSimpleName(), methodName, run.getName(), run.getId());
		return obj;
	}

}
