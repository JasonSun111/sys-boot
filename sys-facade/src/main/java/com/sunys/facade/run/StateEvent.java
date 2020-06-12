package com.sunys.facade.run;

/**
 * StateEvent
 * @author sunys
 * @date Jun 11, 2020
 */
public interface StateEvent<P, T extends State<? extends StateType>> extends Event<P> {

	/**
	 * 获取state
	 * @return
	 */
	T getState();

}
