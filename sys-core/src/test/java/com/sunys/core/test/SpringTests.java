package com.sunys.core.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.sunys.core.test.aop.LogAspectTest;
import com.sunys.core.test.bean.Dut;
import com.sunys.core.test.bean.Step;
import com.sunys.core.test.bean.Testcase;
import com.sunys.core.test.conf.TestsConfiguration;
import com.sunys.core.test.run.DutGroupRun;
import com.sunys.facade.run.RootGroupRun;
import com.sunys.facade.run.RunFactory;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={TestsConfiguration.class})
//@SpringBootTest
public class SpringTests {

	private static final Logger logger = LoggerFactory.getLogger(SpringTests.class);
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;
	
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
	
	@Test
	public void runTest() throws Exception {
		Testcase testcase = new Testcase();
		for (int i = 0; i < 4; i++) {
			Dut dut = new Dut();
			for (int j = 0; j < 4; j++) {
				Step step = new Step();
				dut.getSteps().add(step);
			}
			testcase.getDuts().add(dut);
		}
		RootGroupRun<DutGroupRun> testcaseRun = RunFactory.getRun(testcase);
		testcaseRun.run();
	}
}
