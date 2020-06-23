package com.sunys.facade.run;

import com.sunys.facade.annotation.Factory;

/**
 * RunFactory
 * @author sunys
 * @date Dec 21, 2019
 */
public interface RunFactory<T, P> {

	/**
	 * 创建run接口对象
	 * @param param
	 * @return
	 * @throws Exception
	 */
	T getInstance(P param) throws Exception;
	
	/**
	 * 根据参数对象的注解调用不同的工厂类创建run接口对象
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	static <R> R getRun(Object obj) throws Exception {
		Factory anno = obj.getClass().getAnnotation(Factory.class);
		String className = anno.value();
		Class<?> clazz = Class.forName(className);
		RunFactory<R, Object> runFactory = (RunFactory<R, Object>) clazz.newInstance();
		R instance = runFactory.getInstance(obj);
		return instance;
	}
}
