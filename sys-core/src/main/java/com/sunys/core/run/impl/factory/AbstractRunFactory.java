package com.sunys.core.run.impl.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunFactory;

/**
 * AbstractRunFactory
 * @author sunys
 * @date Dec 21, 2019
 */
public abstract class AbstractRunFactory<T extends Run> implements RunFactory<T> {

	/**
	 * 创建原始的run实现类对象
	 * @param obj
	 * @return
	 */
	public abstract T createRun(Object obj) throws Exception;
	
	@Override
	public T getInstance(Object obj) throws Exception {
		T run = createRun(obj);
		//创建当前对象的代理对象
		T runProxy = getProxy(run);
		//设置到当前对象中
		run.setProxy(runProxy);
		return runProxy;
	}

	/**
	 * 创建run接口对象的代理对象
	 * @param run
	 * @return
	 * @throws Exception
	 */
	private T getProxy(T run) throws Exception {
		Class<?> proxyClass = Proxy.getProxyClass(run.getClass().getClassLoader(), run.getClass().getInterfaces());
		InvocationHandler invocationHandler = new RunInvocationHandler(run);
		Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
		Object obj = constructor.newInstance(invocationHandler);
		return (T) obj;
	}

}
