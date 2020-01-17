package com.sunys.facade.run;

import java.util.concurrent.Callable;

/**
 * Run
 * @author sunys
 * @date Dec 21, 2019
 */
public interface Run extends Callable<Byte> {

	byte STATUS_NONE = 0;
	byte STATUS_IDLE = 1;
	byte STATUS_STARTING = 2;
	byte STATUS_RUNNING = 3;
	byte STATUS_SUCCESS = 4;
	byte STATUS_FAIL = 5;
	byte STATUS_STOPING = 6;
	byte STATUS_DESTROY = 7;
	
	/**
	 * 获取id
	 * @return
	 */
	long getId();
	
	/**
	 * 获取name
	 * @return
	 */
	String getName();
	
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
	 * 获取运行状态
	 * @return
	 */
	byte getStatus();
	
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
	
	/**
	 * 获取超时检测接口
	 * @return
	 */
	TimeoutCheck getTimeoutCheck();
	
	/**
	 * 创建对象后初始化
	 * @throws Exception
	 */
	void init() throws Exception;
	
	/**
	 * 运行
	 * @throws Exception
	 */
	void run() throws Exception;
	
	@Override
	default Byte call() throws Exception {
		//获取当前对象的代理
		Run proxy = getProxy();
		if (proxy != null) {
			//运行代理对象
			proxy.run();
		} else {
			run();
		}
		return getStatus();
	}
	
	/**
	 * 清空运行数据
	 */
	void clean();
	
	/**
	 * 重置对象状态
	 */
	void reset();
	
	/**
	 * 清空运行数据并重置对象状态
	 */
	default void cleanAndReset() {
		clean();
		reset();
	}
	
	/**
	 * 销毁对象调用
	 */
	void destroy();
}
