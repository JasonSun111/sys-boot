package com.sunys.facade.run;

import java.util.concurrent.TimeUnit;

/**
 * TimeoutCheck
 * @author sunys
 * @date Dec 21, 2019
 */
public interface TimeoutCheck {

	/**
	 * 获取配置的超时时间
	 * @return
	 */
	long getTimeout();
	
	/**
	 * 获取是否超时
	 * @return
	 */
	boolean isTimeout();
	
	/**
	 * 获取还省多长时间超时
	 * @param unit
	 * @return
	 */
	long getDelay(TimeUnit unit);
	
	/**
	 * 获取还省多长时间超时
	 * @return
	 */
	long getDelay();
	
	/**
	 * 开始检测超时
	 */
	void startCheck();
	
	/**
	 * 取消超时检测
	 */
	default void cancelCheck() {
		cancelCheck(false);
	}
	
	/**
	 * 取消超时检测
	 * @param flag
	 */
	void cancelCheck(boolean flag);
	
}
