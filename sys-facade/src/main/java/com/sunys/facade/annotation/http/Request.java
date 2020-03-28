package com.sunys.facade.annotation.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sunys.facade.run.http.HttpMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Request {

	String value();
	
	Headers headers() default @Headers;
	
	HttpMethod method() default HttpMethod.GET;
}
