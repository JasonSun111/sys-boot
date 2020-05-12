package com.sunys.facade.run;

/**
 * ProxyRun
 * @author sunys
 * @date May 1, 2020
 */
public interface ProxyRun extends Run {

	/**
	 * 获取当前对象的代理对象
	 * @return
	 */
	ProxyRun getProxy();
	
	/**
	 * 设置当前对象的代理对象
	 * @param proxy
	 */
	void setProxy(ProxyRun proxy);

}
