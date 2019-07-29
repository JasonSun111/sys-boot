package com.sunys.facade.bean;

import java.util.Locale;

/**
 * DapContext
 * @author sunys
 * @date 2019年1月7日
 */
public interface DapContext {

	String getCk();

	Object getPerson();

	String getUsername();

	Locale getLocale();

}
