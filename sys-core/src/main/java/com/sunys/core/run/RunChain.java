package com.sunys.core.run;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class RunChain {

	private List<RunMethodInterceptor> interceptors;
	
	private Run run;
	private Method method;
	private Object[] args;
	
	public RunChain(List<RunMethodInterceptor> interceptors, Run run, Method method, Object... args) {
		this.interceptors = interceptors;
		this.run = run;
		this.method = method;
		this.args = args;
	}
	
	public Object invoke() throws Exception {
		Object obj = null;
		Iterator<RunMethodInterceptor> it = interceptors.iterator();
		if (it.hasNext()) {
			RunMethodInterceptor interceptor = it.next();
			obj = interceptor.intercept(this);
		} else {
			obj = method.invoke(run, args);
		}
		return obj;
	}
}
