package com.sunys.core.run.impl.factory.handle;

import com.sunys.facade.annotation.http.Header;
import com.sunys.facade.annotation.http.HttpConfiguration;
import com.sunys.facade.run.http.HttpRunInvocationHandler;

public class HttpAnnotationHandler implements AnnotationHandler {

	private HttpRunInvocationHandler invocationHandler;
	
	public HttpAnnotationHandler(HttpRunInvocationHandler invocationHandler) {
		this.invocationHandler = invocationHandler;
	}

	@Override
	public void handle(Class<?> clazz) throws Exception {
		HttpConfiguration httpConfiguration = clazz.getAnnotation(HttpConfiguration.class);
		String rootUrl = httpConfiguration.rootUrl();
		String contextPath = httpConfiguration.contextPath();
		Header[] headers = httpConfiguration.headers().value();
		for (Header header : headers) {
			String headerName = header.name();
			String headerValue = header.value();
			boolean isSet = header.set();
			
		}
	}

}
