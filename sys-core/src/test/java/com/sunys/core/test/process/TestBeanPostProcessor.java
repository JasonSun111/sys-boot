package com.sunys.core.test.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class TestBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements MergedBeanDefinitionPostProcessor {

	private static final Logger logger = LoggerFactory.getLogger(TestBeanPostProcessor.class);
	
	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		if("testService".equals(beanName)){
			logger.info("postProcessBeforeInstantiation:" + beanName);
		}
		return null;
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		if("testService".equals(beanName)){
			logger.info("postProcessAfterInstantiation:" + beanName);
		}
		return true;
	}

	@Override
	public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
			throws BeansException {
		if("testService".equals(beanName)){
			logger.info("postProcessProperties:" + beanName);
		}
		return null;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if("testService".equals(beanName)){
			logger.info("postProcessBeforeInitialization:" + beanName);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if("testService".equals(beanName)){
			logger.info("postProcessAfterInitialization:" + beanName);
		}
		return bean;
	}

	@Override
	public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
		if("testService".equals(beanName)){
			logger.info("postProcessMergedBeanDefinition:" + beanName);
		}
	}

	@Override
	public void resetBeanDefinition(String beanName) {
		if("testService".equals(beanName)){
			logger.info("resetBeanDefinition:" + beanName);
		}
	}

}
