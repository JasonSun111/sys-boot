package com.sunys.core.run.impl.factory.handle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sunys.facade.annotation.Interceptor;
import com.sunys.facade.run.MethodInterceptor;
import com.sunys.facade.run.ObjectFactory;

/**
 * 处理Interceptor注解
 * InterceptorAnnotationHandler
 * @author sunys
 * @date Feb 7, 2020
 */
public class InterceptorAnnotationHandler implements AnnotationHandler {

	private ObjectFactory<MethodInterceptor, Class<? extends MethodInterceptor>> interceptorFactory;
	
	public InterceptorAnnotationHandler(ObjectFactory<MethodInterceptor, Class<? extends MethodInterceptor>> interceptorFactory) {
		this.interceptorFactory = interceptorFactory;
	}

	@Override
	public Map<Method, List<MethodInterceptor>> handle(Class<?> clazz) throws Exception {
		Map<Method, List<MethodInterceptor>> interceptorsMap = new HashMap<>();
		//获取对象所有方法
		Iterator<Method> it = getAllInterfaceSet(clazz).stream().flatMap(inter -> Arrays.stream(inter.getMethods())).iterator();
		while (it.hasNext()) {
			Method method = it.next();
			method.setAccessible(true);
			//获取方法上的Interceptor注解
			Interceptor anno = method.getAnnotation(Interceptor.class);
			List<MethodInterceptor> interceptors = new ArrayList<>();
			if (anno != null) {
				//获取注解上的拦截器类型
				Class<? extends MethodInterceptor>[] value = anno.value();
				for (Class<? extends MethodInterceptor> interceptorClazz : value) {
					//创建方法拦截器对象
					MethodInterceptor interceptor = interceptorFactory.getInstance(interceptorClazz);
					interceptors.add(interceptor);
				}
				//获取拦截器类名
				String[] classNames = anno.classNames();
				for (String className : classNames) {
					//根据类名创建方法拦截器对象
					Class<? extends MethodInterceptor> interceptorClazz = (Class<? extends MethodInterceptor>) Class.forName(className);
					MethodInterceptor interceptor = interceptorFactory.getInstance(interceptorClazz);
					interceptors.add(interceptor);
				}
			}
			interceptorsMap.put(method, interceptors);
		}
		return interceptorsMap;
	}

	/**
	 * 获取实现的所有接口
	 * @param clazz
	 * @return
	 */
	public static Class<?>[] getAllInterfaceArray(Class<?> clazz) {
		Set<Class<?>> set = getAllInterfaceSet(clazz);
		return set.toArray(new Class[set.size()]);
	}

	/**
	 * 获取实现的所有接口
	 * @param clazz
	 * @return
	 */
	public static Set<Class<?>> getAllInterfaceSet(Class<?> clazz) {
		Set<Class<?>> set = new HashSet<Class<?>>();
		if (clazz.isInterface()) {
			set.add(clazz);
			return set;
		}
		addInterfaceClass(set, clazz);
		while (!Object.class.equals(clazz)) {
			clazz = clazz.getSuperclass();
			if (clazz == null) {
				break;
			}
			addInterfaceClass(set, clazz);
		}
		return set;
	}

	private static void addInterfaceClass(Set<Class<?>> set, Class<?> clazz) {
		Class<?>[] classes = clazz.getInterfaces();
		for (Class<?> c : classes) {
			set.add(c);
		}
	}

}
