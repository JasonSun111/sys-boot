package com.sunys.facade.run;

import com.sunys.facade.annotation.Factory;

public interface RunFactory<T extends Run> {

	T getInstance(Object obj) throws Exception;
	
	static <R extends Run> R getRun(Object obj) throws Exception {
		Factory anno = obj.getClass().getAnnotation(Factory.class);
		String className = anno.value();
		Class<?> clazz = Class.forName(className);
		RunFactory<? extends Run> runFactory = (RunFactory<? extends Run>) clazz.newInstance();
		R instance = (R) runFactory.getInstance(obj);
		return instance;
	}
}
