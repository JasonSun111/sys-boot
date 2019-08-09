package com.sunys.core.test.aop;

import org.springframework.beans.factory.annotation.Autowired;

import com.sunys.facade.annotation.Log;

public class LogAspectTest {

	@Autowired
	private TestService testService;
	
	@Log
	public String test(String str) {
		testService.service();
		return "test:" + str;
	}
}
