package com.sunys.facade.run;

/**
 * 状态机
 * ContextState
 * @author sunys
 * @date May 17, 2020
 */
public interface ContextState {

	/**
	 * 当前状态
	 * @return
	 */
	State currentState();
	
	/**
	 * 改变状态
	 * @param state
	 */
	void change(State state);
}
