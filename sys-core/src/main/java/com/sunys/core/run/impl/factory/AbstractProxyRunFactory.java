package com.sunys.core.run.impl.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.sunys.core.run.impl.factory.handle.InterceptorAnnotationHandler;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunFactory;

/**
 * AbstractProxyRunFactory
 * @author sunys
 * @date Dec 21, 2019
 */
public abstract class AbstractProxyRunFactory<T extends Run, P> implements RunFactory<T, P> {

	/**
	 * 创建原始的run实现类对象
	 * @param param
	 * @return
	 */
	public abstract T createRun(P param) throws Exception;
	
	@Override
	public T getInstance(P param) throws Exception {
		T run = createRun(param);
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
		Class<?> proxyClass = Proxy.getProxyClass(run.getClass().getClassLoader(), InterceptorAnnotationHandler.getAllInterfaceArray(run.getClass()));
		InvocationHandler invocationHandler = new RunInvocationHandlerImpl(run);
		Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
		Object obj = constructor.newInstance(invocationHandler);
		return (T) obj;
	}

}
