package com.sunys.core.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.sunys.core.test.aop.LogAspectTest;
import com.sunys.core.test.conf.TestsConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={TestsConfiguration.class})
//@SpringBootTest
public class SpringTests {

	private static final Logger logger = LoggerFactory.getLogger(SpringTests.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private LogAspectTest logAspectTest;
	
	@Test
	public void contextLoads() {
		String[] names = applicationContext.getBeanDefinitionNames();
		for (String name : names) {
			Object bean = applicationContext.getBean(name);
			logger.info("beanName:{},beanClass:{}",name,bean.getClass().getName());
		}
		String str = logAspectTest.test("ddd");
		logger.info(str);
	}
}
