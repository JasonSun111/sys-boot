package com.sunys.core.run.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sunys.core.run.Run;
import com.sunys.core.run.RunChain;
import com.sunys.core.run.RunMethodInterceptor;
import com.sunys.core.run.annotation.Interceptor;

public class RunInvocationHandler implements InvocationHandler {

	private Run run;
	
	private Map<String, List<RunMethodInterceptor>> interceptorsMap = new HashMap<>();
	
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
					interceptor.setRun(run);
					interceptors.add(interceptor);
				}
				String key = method.toString();
				interceptorsMap.put(key, interceptors);
			}
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String key = method.toString();
		List<RunMethodInterceptor> interceptors = interceptorsMap.get(key);
		RunChain runChain = new RunChain(interceptors, run, method, args);
		Object obj = runChain.invoke();
		return obj;
	}

}
