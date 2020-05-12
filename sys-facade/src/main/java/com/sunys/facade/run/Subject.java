package com.sunys.facade.run;

/**
 * 被观察者
 * Subject
 * @author sunys
 * @date Apr 12, 2020
 */
public interface Subject {

	/**
	 * 注册观察者
	 * @param type
	 * @param eventHandler
	 */
	void registEventHandler(EventType type, EventHandler<?> eventHandler);

	/**
	 * 移除观察者
	 * @param type
	 */
	void removeEventHandler(EventType type);
	
	/**
	 * 移除观察者
	 * @param eventHandler
	 */
	void removeEventHandler(EventHandler<?> eventHandler);
	
	/**
	 * 触发事件
	 * @param event
	 */
	<P> void event(Event<P> event);

}
