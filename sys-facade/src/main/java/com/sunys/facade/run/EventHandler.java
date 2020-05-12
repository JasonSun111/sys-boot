package com.sunys.facade.run;

/**
 * 观察者
 * EventHandler
 * @author sunys
 * @date Apr 12, 2020
 */
public interface EventHandler<P> {

	/**
	 * 处理事件
	 * @param event
	 */
	void handle(Event<P> event);
}
