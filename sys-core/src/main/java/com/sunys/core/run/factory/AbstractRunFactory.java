package com.sunys.core.run.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.sunys.core.run.Run;
import com.sunys.core.run.RunFactory;

public abstract class AbstractRunFactory<T extends Run> implements RunFactory<T> {

	public abstract T createRun();
	
	@Override
	public T getInstance() throws Exception {
		T run = createRun();
		T runProxy = getProxy(run);
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
