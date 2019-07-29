package com.sunys.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SpringContextHelper
 * @author sunys
 * @date Jul 21, 2019
 */
public class SpringContextHelper implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	/**
	 * 获取spring ApplicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return context;
	}
}
