package com.sunys.facade.run;

/**
 * Run
 * @author sunys
 * @date Dec 21, 2019
 */
public interface Run {

	/**
	 * 获取id
	 * @return
	 */
	long getId();
	
	/**
	 * 获取当前对象的代理对象
	 * @return
	 */
	Run getProxy();
	
	/**
	 * 设置当前对象的代理对象
	 * @param proxy
	 */
	void setProxy(Run proxy);
	
	void registEventHandler(int type, EventHandler<?> eventHandler);
	
	void unregistEventHandler(int type);
	
	<P, R> R event(Event<P> event);

}
