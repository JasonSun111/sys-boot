package com.sunys.core.run.impl.factory.handle;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.sunys.facade.run.MethodInterceptor;

/**
 * 处理类上的注解
 * AnnotationHandler
 * @author sunys
 * @date Feb 7, 2020
 */
public interface AnnotationHandler {

	/**
	 * 处理类上的注解
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	Map<Method, List<MethodInterceptor>> handle(Class<?> clazz) throws Exception;

}
