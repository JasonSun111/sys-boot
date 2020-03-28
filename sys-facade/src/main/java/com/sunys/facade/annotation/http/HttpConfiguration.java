package com.sunys.facade.annotation.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HttpConfiguration {

	String rootUrl() default "";
	
	String contextPath() default "";
	
	Headers headers() default @Headers;
}
