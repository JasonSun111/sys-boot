package com.sunys.core.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class SpringTests {

	private static final Logger logger = LoggerFactory.getLogger(SpringTests.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Test
	public void contextLoads() {
		String[] names = applicationContext.getBeanDefinitionNames();
		for (String name : names) {
			Object bean = applicationContext.getBean(name);
			logger.info("beanName:{},beanClass:{}",name,bean.getClass().getName());
		}
	}

}
