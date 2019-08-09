package com.sunys.core.test.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestService {

	private static final Logger logger = LoggerFactory.getLogger(TestService.class);
	
	public void service() {
		logger.info("com.sunys.core.test.aop.TestService.service() ...");
	}
}
