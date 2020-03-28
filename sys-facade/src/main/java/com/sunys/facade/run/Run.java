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
	
	/**
	 * 获取多层上级节点
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	<T> T parents(Class<? extends Run> clazz);
	
	/**
	 * 获取上级节点
	 * @return
	 */
	GroupRun<? extends Run> getParent();
	
	/**
	 * 获取根节点
	 * @return
	 */
	RootGroupRun<? extends Run> getRoot();

}
