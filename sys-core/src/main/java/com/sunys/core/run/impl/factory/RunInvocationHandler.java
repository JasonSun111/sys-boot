package com.sunys.core.run.impl.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sunys.facade.annotation.Interceptor;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunChain;
import com.sunys.facade.run.RunMethodInterceptor;

public class RunInvocationHandler implements InvocationHandler {

	private Run run;
	
	private Map<Method, List<RunMethodInterceptor>> interceptorsMap = new HashMap<>();
	
	public RunInvocationHandler(Run run) throws InstantiationException, IllegalAccessException {
		this.run = run;
		init();
	}

	private void init() throws InstantiationException, IllegalAccessException {
		Method[] methods = run.getClass().getMethods();
		for (Method method : methods) {
			method.setAccessible(true);
			List<RunMethodInterceptor> interceptors = new ArrayList<>();
			Interceptor anno = method.getAnnotation(Interceptor.class);
			if (anno != null) {
				Class<? extends RunMethodInterceptor>[] value = anno.value();
				for (Class<? extends RunMethodInterceptor> clazz : value) {
					RunMethodInterceptor interceptor = clazz.newInstance();
					interceptors.add(interceptor);
				}
				interceptorsMap.put(method, interceptors);
			}
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		List<RunMethodInterceptor> interceptors = interceptorsMap.get(method);
		RunChain runChain = new RunChain(interceptors, run, method, args);
		Object obj = runChain.invoke();
		return obj;
	}

}
