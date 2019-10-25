package com.sunys.facade.run;

import com.sunys.facade.annotation.Factory;

public interface RunFactory<T extends Run> {

	T getInstance(Object obj) throws Exception;
	
	static <R extends Run> R getRun(Object obj) throws Exception {
		Factory anno = obj.getClass().getAnnotation(Factory.class);
		Class<? extends RunFactory<? extends Run>> clazz = anno.value();
		RunFactory<? extends Run> runFactory = clazz.newInstance();
		R instance = (R) runFactory.getInstance(obj);
		return instance;
	}
}
