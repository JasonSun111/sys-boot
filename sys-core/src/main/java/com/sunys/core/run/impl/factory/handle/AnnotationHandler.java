package com.sunys.core.run.impl.factory.handle;

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
	 * @throws Exception
	 */
	void handle(Class<?> clazz) throws Exception;
}
