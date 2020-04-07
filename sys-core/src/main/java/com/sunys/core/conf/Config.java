package com.sunys.core.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Bean
	public CustomeJson customeJson() {
		return new CustomeJson();
	}
}
