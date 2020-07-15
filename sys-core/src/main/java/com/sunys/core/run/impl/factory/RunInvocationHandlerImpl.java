package com.sunys.core.run.impl.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.sunys.core.run.impl.factory.handle.AnnotationHandler;
import com.sunys.facade.run.MethodInterceptor;
import com.sunys.facade.run.RunChain;
import com.sunys.facade.run.RunChainImpl;

/**
 * RunInvocationHandlerImpl
 * @author sunys
 * @date Dec 21, 2019
 */
public class RunInvocationHandlerImpl implements InvocationHandler {

	private Object target;
	private Map<Method, List<MethodInterceptor>> interceptorsMap;

	public RunInvocationHandlerImpl(Object target, AnnotationHandler handler) throws Exception {
		this.target = target;
		this.interceptorsMap = handler.handle(target.getClass());
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		List<MethodInterceptor> interceptors = interceptorsMap.get(method);
		//创建方法执行链
		RunChain runChain = new RunChainImpl(interceptors, target, proxy, method, args);
		//执行拦截器和目标方法
		Object obj = runChain.invoke();
		return obj;
	}

}
