package com.sunys.core.run.impl.factory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.sunys.core.run.impl.factory.handle.AnnotationHandler;
import com.sunys.core.run.impl.factory.handle.HttpAnnotationHandler;
import com.sunys.core.run.impl.factory.handle.InterceptorAnnotationHandler;
import com.sunys.facade.run.RunChain;
import com.sunys.facade.run.MethodInterceptor;
import com.sunys.facade.run.http.HttpBuildParam;
import com.sunys.facade.run.http.HttpRunChain;
import com.sunys.facade.run.http.HttpRunInvocationHandler;

/**
 * HttpRunInvocationHandlerImpl
 * @author sunys
 * @date Dec 21, 2019
 */
public class HttpRunInvocationHandlerImpl implements HttpRunInvocationHandler {

	private HttpBuildParam param;
	
	private Map<Method, List<MethodInterceptor>> interceptorsMap;
//	private AnnotationHandler interceptorAnnotationHandler = new InterceptorAnnotationHandler(this);
//	private AnnotationHandler httpAnnotationHandler = new HttpAnnotationHandler(this);
	
	public HttpRunInvocationHandlerImpl(HttpBuildParam param) throws Exception {
		this.param = param;
		init();
	}

	private void init() throws Exception {
//		interceptorAnnotationHandler.handle(param.getClazz());
//		httpAnnotationHandler.handle(param.getClazz());
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		List<MethodInterceptor> interceptors = interceptorsMap.get(method);
		//创建方法执行链
		RunChain runChain = new HttpRunChain(interceptors, param.getHttpRun(), proxy, method, args);
		//执行拦截器和目标方法
		Object obj = runChain.invoke();
		return obj;
	}
/*
	@Override
	public void setInterceptorsMap(Map<Method, List<MethodInterceptor>> interceptorsMap) {
		this.interceptorsMap = interceptorsMap;
	}
*/
}
