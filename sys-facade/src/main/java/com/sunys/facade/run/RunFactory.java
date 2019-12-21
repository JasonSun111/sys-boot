package com.sunys.facade.run;

import com.sunys.facade.annotation.Factory;

/**
 * RunFactory
 * @author sunys
 * @date Dec 21, 2019
 */
public interface RunFactory<T extends Run> {

	/**
	 * 创建run接口对象
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	T getInstance(Object obj) throws Exception;
	
	/**
	 * 根据参数对象的注解调用不同的工厂类创建run接口对象
	 * @param <R>
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	static <R extends Run> R getRun(Object obj) throws Exception {
		Factory anno = obj.getClass().getAnnotation(Factory.class);
		String className = anno.value();
		Class<?> clazz = Class.forName(className);
		RunFactory<? extends Run> runFactory = (RunFactory<? extends Run>) clazz.newInstance();
		R instance = (R) runFactory.getInstance(obj);
		return instance;
	}
}
