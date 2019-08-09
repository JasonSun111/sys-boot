package com.sunys.core.test.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogAspect {

	@Pointcut("@annotation(com.sunys.facade.annotation.Log)")
	public void pointcut(){
	}
	
	@Around(value="com.sunys.core.test.aop.LogAspect.pointcut()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		Class<? extends Object> clazz = joinPoint.getTarget().getClass();
		Logger logger = LoggerFactory.getLogger(clazz);
		logger.info("log before");
		try {
			Object obj = joinPoint.proceed();
			logger.info("log afert");
			return obj;
		} catch (Throwable e) {
			logger.info("log exception");
			throw e;
		}
	}
}
