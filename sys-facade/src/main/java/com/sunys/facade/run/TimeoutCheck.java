package com.sunys.facade.run;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * TimeoutCheck
 * @author sunys
 * @date Dec 21, 2019
 */
public interface TimeoutCheck {

	/**
	 * 获取锁
	 * @return
	 */
	Lock getLock();
	
	/**
	 * 获取默认的Condition
	 * @return
	 */
	Condition getDefaultCondition();
	
	/**
	 * 创建Condition
	 * @return
	 */
	Condition newCondition();
	
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
	Long getDelay(TimeUnit unit);
	
	/**
	 * 开始检测超时
	 */
	void startCheckTimeout();
	
	/**
	 * 开始检测超时
	 * @param timeout
	 */
	void startCheckTimeout(Long timeout);
	
	/**
	 * 开始检测超时
	 * @param timeout
	 * @param unit
	 */
	void startCheckTimeout(Long timeout, TimeUnit unit);
	
	/**
	 * 取消超时检测
	 * @param flag
	 */
	void cancelCheckTimeout(boolean flag);
	
	/**
	 * 当前线程等待
	 * @throws InterruptedException
	 */
	void await() throws InterruptedException;
	
	/**
	 * 当前线程等待
	 * @param sec
	 * @return
	 * @throws InterruptedException
	 */
	boolean await(int sec) throws InterruptedException;
	
	/**
	 * 唤醒等待线程
	 */
	void signal();
	
	/**
	 * 获取所有等待线程
	 */
	void signalAll();
	
	/**
	 * 重置
	 */
	void reset();
}
