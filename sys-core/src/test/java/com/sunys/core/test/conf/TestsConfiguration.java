package com.sunys.core.test.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.sunys.core.conf.CustomeJson;
import com.sunys.core.context.SpringContextHelper;
import com.sunys.core.test.aop.LogAspect;
import com.sunys.core.test.aop.LogAspectTest;
import com.sunys.core.test.aop.TestService;
import com.sunys.core.test.process.TestBeanPostProcessor;

@Configuration
@EnableAspectJAutoProxy
public class TestsConfiguration {

	@Bean
	public CustomeJson customeJson() {
		return new CustomeJson();
	}

	@Bean
	public SpringContextHelper springContextHelper() {
		return new SpringContextHelper();
	}

	@Bean
	public TestBeanPostProcessor referenceAnnotationBeanPostProcessor() {
		TestBeanPostProcessor referenceAnnotationBeanPostProcessor = new TestBeanPostProcessor();
		return referenceAnnotationBeanPostProcessor;
	}

	@Bean
	public LogAspect logAspect(){
		return new LogAspect();
	}

	@Bean
	public LogAspectTest logAspectTest(){
		return new LogAspectTest();
	}

	@Bean
	public TestService testService() {
		return new TestService();
	}

}
