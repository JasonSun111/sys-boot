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

/**
 * RunInvocationHandler
 * @author sunys
 * @date Dec 21, 2019
 */
public class RunInvocationHandler implements InvocationHandler {

	private Run run;
	
	private Map<Method, List<RunMethodInterceptor>> interceptorsMap = new HashMap<>();
	
	public RunInvocationHandler(Run run) throws InstantiationException, IllegalAccessException {
		this.run = run;
		init();
	}

	private void init() throws InstantiationException, IllegalAccessException {
		//获取对象所以方法
		Method[] methods = run.getClass().getMethods();
		for (Method method : methods) {
			method.setAccessible(true);
			//获取方法上的Interceptor注解
			Interceptor anno = method.getAnnotation(Interceptor.class);
			if (anno != null) {
				List<RunMethodInterceptor> interceptors = new ArrayList<>();
				//获取注解上的拦截器类型
				Class<? extends RunMethodInterceptor>[] value = anno.value();
				for (Class<? extends RunMethodInterceptor> clazz : value) {
					//创建方法拦截器对象
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
		//创建方法执行链
		RunChain runChain = new RunChain(interceptors, run, method, args);
		//执行拦截器和目标方法
		Object obj = runChain.invoke();
		return obj;
	}

}
