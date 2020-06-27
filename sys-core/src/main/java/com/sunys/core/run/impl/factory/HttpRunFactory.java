package com.sunys.core.run.impl.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.sunys.facade.run.ObjectFactory;
import com.sunys.facade.run.http.HttpBuildParam;
import com.sunys.facade.run.http.HttpRun;

public class HttpRunFactory<T extends HttpRun> implements ObjectFactory<T, HttpBuildParam> {

	@Override
	public T getInstance(HttpBuildParam param) throws Exception {
		Class<? extends HttpRun> clazz = param.getClazz();
		Class<?> proxyClass = Proxy.getProxyClass(clazz.getClass().getClassLoader(), clazz);
		Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
		InvocationHandler invocationHandler = new HttpRunInvocationHandlerImpl(param);
		Object obj = constructor.newInstance(invocationHandler);
		return (T) obj;
	}

}
