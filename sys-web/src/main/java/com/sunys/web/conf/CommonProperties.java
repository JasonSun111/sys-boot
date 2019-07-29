package com.sunys.web.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="spring.mvc.cmn")
public class CommonProperties {

	private boolean checkLogin = false;

	public boolean getCheckLogin() {
		return checkLogin;
	}

	public void setCheckLogin(boolean checkLogin) {
		this.checkLogin = checkLogin;
	}

}
