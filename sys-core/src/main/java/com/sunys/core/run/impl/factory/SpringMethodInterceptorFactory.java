package com.sunys.core.run.impl.factory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.springframework.context.ApplicationContext;

import com.sunys.facade.run.MethodInterceptor;
import com.sunys.facade.run.ObjectFactory;

public class SpringMethodInterceptorFactory implements ObjectFactory<MethodInterceptor, Class<? extends MethodInterceptor>> {

	private ApplicationContext applicationContext;
	
	public SpringMethodInterceptorFactory(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public MethodInterceptor getInstance(Class<? extends MethodInterceptor> param) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(param);
			//获取javabean的所有属性
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			MethodInterceptor methodInterceptor = param.newInstance();
			for (PropertyDescriptor pd : pds) {
				String name = pd.getName();
				//获取set方法
				Method writeMethod = pd.getWriteMethod();
				if (writeMethod != null) {
					Object bean = applicationContext.getBean(name);
					//设置属性
					writeMethod.invoke(methodInterceptor, bean);
				}
			}
			return methodInterceptor;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
