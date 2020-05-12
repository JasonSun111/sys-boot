package com.sunys.facade.run;

/**
 * Event
 * @author sunys
 * @date Apr 12, 2020
 */
public interface Event<P> {

	/**
	 * 事件类型
	 * @return
	 */
	EventType type();
	
	/**
	 * 参数
	 * @return
	 */
	P getParam();
}
