package com.sunys.web.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sunys.core.conf.CustomeJson;
import com.sunys.core.util.RemoteServiceInvoke;
import com.sunys.core.util.ServiceInvoke;

/**
 * CommonAutoConfiguration
 * @author sunys
 * @date 2019年1月14日
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(CommonProperties.class)
public class CommonAutoConfiguration implements WebMvcConfigurer {

	@Autowired
	private CommonProperties commonProperties;

	@Bean
	public ServiceInvoke serviceInvoke() {
		ServiceInvoke serviceInvoke = new ServiceInvoke();
		return serviceInvoke;
	}

	@Bean
	@ConditionalOnBean(value=RestTemplate.class)
	public RemoteServiceInvoke remoteServiceInvoke() {
		RemoteServiceInvoke remoteServiceInvoke = new RemoteServiceInvoke();
		return remoteServiceInvoke;
	}

/*
	@Bean
	@ConditionalOnMissingBean
	public ServiceController serviceController() {
		ServiceController serviceController = new ServiceController();
		return serviceController;
	}

	@Bean
	@ConditionalOnMissingBean
	public PageController pageController() {
		PageController pageController = new PageController();
		return pageController;
	}

	@Bean
	public HandlerExceptionResolver handlerResolver() {
		HandlerExceptionResolver exceptionResolver = new HandlerException();
		return exceptionResolver;
	}
*/

	@Bean
	public CustomeJson customeJson(){
		return new CustomeJson();
	}

}
