package com.sunys.facade.run;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ListIterator;

/**
 * 封装了调用一次接口方法需要执行的拦截器和目标方法
 * RunChain
 * @author sunys
 * @date Dec 21, 2019
 */
public class RunChain {

	private ListIterator<RunMethodInterceptor> listIterator;

	private Run run;
	private Method method;
	private Object[] args;

	public RunChain(List<RunMethodInterceptor> interceptors, Run run, Method method, Object... args) {
		if (interceptors != null) {
			this.listIterator = interceptors.listIterator();
		}
		this.run = run;
		this.method = method;
		this.args = args;
	}

	/**
	 * 执行拦截器栈和目标方法
	 * @return
	 * @throws Exception
	 */
	public Object invoke() throws Exception {
		Object obj = null;
		if (listIterator != null && listIterator.hasNext()) {
			//如果有拦截器，先执行拦截器
			RunMethodInterceptor interceptor = listIterator.next();
			obj = interceptor.intercept(this);
		} else {
			//如果没有拦截器，执行目标方法
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
