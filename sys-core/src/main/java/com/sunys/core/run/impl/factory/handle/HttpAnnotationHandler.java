package com.sunys.core.run.impl.factory.handle;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.sunys.facade.annotation.http.Header;
import com.sunys.facade.annotation.http.HttpConfiguration;
import com.sunys.facade.run.MethodInterceptor;
import com.sunys.facade.run.http.HttpRunInvocationHandler;

public class HttpAnnotationHandler implements AnnotationHandler {

	private HttpRunInvocationHandler invocationHandler;
	
	public HttpAnnotationHandler(HttpRunInvocationHandler invocationHandler) {
		this.invocationHandler = invocationHandler;
	}

	@Override
	public Map<Method, List<MethodInterceptor>> handle(Class<?> clazz) throws Exception {
		HttpConfiguration httpConfiguration = clazz.getAnnotation(HttpConfiguration.class);
		String rootUrl = httpConfiguration.rootUrl();
		String contextPath = httpConfiguration.contextPath();
		Header[] headers = httpConfiguration.headers().value();
		for (Header header : headers) {
			String headerName = header.name();
			String headerValue = header.value();
			boolean isSet = header.set();
			
		}
		return null;
	}

}
