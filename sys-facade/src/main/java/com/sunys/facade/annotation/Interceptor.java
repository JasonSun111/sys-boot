package com.sunys.facade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sunys.facade.run.RunMethodInterceptor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Interceptor {

	Class<? extends RunMethodInterceptor>[] value() default {};
	
	String[] classNames() default {};
}
