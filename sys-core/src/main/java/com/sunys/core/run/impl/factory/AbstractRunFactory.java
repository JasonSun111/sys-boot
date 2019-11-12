package com.sunys.core.run.impl.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunFactory;

public abstract class AbstractRunFactory<T extends Run> implements RunFactory<T> {

	public abstract T createRun(Object obj);
	
	@Override
	public T getInstance(Object obj) throws Exception {
		T run = createRun(obj);
		T runProxy = getProxy(run);
		run.setProxy(runProxy);
		return runProxy;
	}

	private T getProxy(T run) throws Exception {
		Class<?> proxyClass = Proxy.getProxyClass(run.getClass().getClassLoader(), run.getClass().getInterfaces());
		InvocationHandler invocationHandler = new RunInvocationHandler(run);
		Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
		Object obj = constructor.newInstance(invocationHandler);
		return (T) obj;
	}

}
