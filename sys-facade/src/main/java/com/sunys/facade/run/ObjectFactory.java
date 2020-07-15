package com.sunys.facade.run;

import com.sunys.facade.annotation.Factory;

/**
 * ObjectFactory
 * @author sunys
 * @date Dec 21, 2019
 */
public interface ObjectFactory<T, P> {

	/**
	 * 创建run接口对象
	 * @param param
	 * @return
	 * @throws Exception
	 */
	T getInstance(P param);
	
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
		ObjectFactory<R, Object> objectFactory = (ObjectFactory<R, Object>) clazz.newInstance();
		R instance = objectFactory.getInstance(obj);
		return instance;
	}
}
