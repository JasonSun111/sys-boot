package com.sunys.facade.run;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ListIterator;

/**
 * RunChainImpl
 * @author sunys
 * @date Dec 21, 2019
 */
public class RunChainImpl implements RunChain {

	private ListIterator<RunMethodInterceptor> listIterator;

	private Run run;
	private Run proxy;
	private Method method;
	private Object[] args;

	public RunChainImpl(List<RunMethodInterceptor> interceptors, Run run, Run proxy, Method method, Object... args) {
		if (interceptors != null) {
			this.listIterator = interceptors.listIterator();
		}
		this.run = run;
		this.proxy = proxy;
		this.method = method;
		this.args = args;
	}

	@Override
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

	@Override
	public Run getTarget() {
		return run;
	}

	@Override
	public Run getProxy() {
		return proxy;
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public Object[] getArgs() {
		return args;
	}

}
