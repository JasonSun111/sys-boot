package com.sunys.facade.run.http;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ListIterator;

import com.sunys.facade.run.RunChain;
import com.sunys.facade.run.RunMethodInterceptor;

/**
 * HttpRunChain
 * @author sunys
 * @date Dec 21, 2019
 */
public class HttpRunChain implements RunChain {

	private ListIterator<RunMethodInterceptor> listIterator;

	private HttpRun httpRun;
	private Object proxy;
	private Method method;
	private Object[] args;

	public HttpRunChain(List<RunMethodInterceptor> interceptors, HttpRun httpRun, Object proxy, Method method, Object... args) {
		if (interceptors != null) {
			this.listIterator = interceptors.listIterator();
		}
		this.httpRun = httpRun;
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
			
		}
		return obj;
	}

	public HttpRun getHttpRun() {
		return httpRun;
	}

	@Override
	public Object getTarget() {
		return null;
	}

	@Override
	public Object getProxy() {
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
