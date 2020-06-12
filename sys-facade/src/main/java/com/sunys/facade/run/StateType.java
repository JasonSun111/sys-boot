package com.sunys.facade.run;

import java.util.Set;

/**
 * 状态类型
 * StateType
 * @author sunys
 * @date May 17, 2020
 */
public interface StateType {

	/**
	 * 获取名称
	 * @return
	 */
	String getName();
	
	/**
	 * 获取可以改变成什么状态类型
	 * @return
	 */
	Set<? extends StateType> nexts();
	
	/**
	 * 获取下一个status
	 * @param type
	 * @return
	 */
	State<? extends StateType> getState(StateType type);
}
