package com.sunys.facade.run;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ListIterator;

public class RunChain {

	private ListIterator<RunMethodInterceptor> listIterator;

	private Run run;
	private Method method;
	private Object[] args;

	public RunChain(List<RunMethodInterceptor> interceptors, Run run, Method method, Object... args) {
		this.listIterator = interceptors.listIterator();
		this.run = run;
		this.method = method;
		this.args = args;
	}

	public Object invoke() throws Exception {
		Object obj = null;
		if (listIterator.hasNext()) {
			RunMethodInterceptor interceptor = listIterator.next();
			obj = interceptor.intercept(this);
		} else {
			obj = method.invoke(run, args);
		}
		return obj;
	}

	public Run getRun() {
		return run;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getArgs() {
		return args;
	}

}
