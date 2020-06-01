package com.sunys.facade.run;

/**
 * 状态机
 * ContextState
 * @author sunys
 * @date May 17, 2020
 */
public interface ContextState<T extends State> {

	/**
	 * 当前状态
	 * @return
	 */
	T currentState();
	
	/**
	 * 改变状态
	 * @param state
	 */
	<P> void change(StateEvent<P, T> event);
}
