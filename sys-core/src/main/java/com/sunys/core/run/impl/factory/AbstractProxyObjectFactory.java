package com.sunys.core.run.impl.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.sunys.core.run.impl.factory.handle.AnnotationHandler;
import com.sunys.core.run.impl.factory.handle.InterceptorAnnotationHandler;
import com.sunys.facade.run.MethodInterceptor;
import com.sunys.facade.run.ObjectFactory;
import com.sunys.facade.run.ProxyRun;

/**
 * AbstractProxyObjectFactory
 * @author sunys
 * @date Dec 21, 2019
 */
public abstract class AbstractProxyObjectFactory<T, P> implements ObjectFactory<T, P> {

	private ObjectFactory<MethodInterceptor, Class<? extends MethodInterceptor>> interceptorFactory = clazz -> {
		try {
			MethodInterceptor methodInterceptor = clazz.newInstance();
			return methodInterceptor;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	};

	/**
	 * 创建原始的run实现类对象
	 * @param param
	 * @return
	 */
	public abstract T createObject(P param);
	
	@Override
	public T getInstance(P param) {
		try {
			T target = createObject(param);
			//创建当前对象的代理对象
			T proxy = getProxy(target);
			if (target instanceof ProxyRun) {
				ProxyRun proxyRun = (ProxyRun) target;
				//设置到当前对象中
				proxyRun.setProxy((ProxyRun) proxy);
			}
			return proxy;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建run接口对象的代理对象
	 * @param target
	 * @return
	 * @throws Exception
	 */
	private T getProxy(T target) throws Exception {
		Class<?> proxyClass = Proxy.getProxyClass(target.getClass().getClassLoader(), InterceptorAnnotationHandler.getAllInterfaceArray(target.getClass()));
		AnnotationHandler annotationHandler = new InterceptorAnnotationHandler(interceptorFactory);
		InvocationHandler invocationHandler = new RunInvocationHandlerImpl(target, annotationHandler);
		Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
		Object obj = constructor.newInstance(invocationHandler);
		return (T) obj;
	}

	public void setInterceptorFactory(ObjectFactory<MethodInterceptor, Class<? extends MethodInterceptor>> interceptorFactory) {
		this.interceptorFactory = interceptorFactory;
	}

}
