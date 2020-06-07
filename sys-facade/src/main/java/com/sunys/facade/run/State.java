package com.sunys.facade.run;

/**
 * 状态
 * State
 * @author sunys
 * @date May 17, 2020
 */
public interface State<T extends StateType> {

	/**
	 * 状态类型
	 * @return
	 */
	T type();
	
	/**
	 * 改变状态时的处理
	 * @param state
	 */
	<P> void handle(StateEvent<P, ? extends State<? extends StateType>> event);
}
