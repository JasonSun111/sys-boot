package com.sunys.facade.run;

/**
 * RootGroupRun
 * @author sunys
 * @date Dec 21, 2019
 */
public interface RootGroupRun<T extends Run> extends GroupRun<T> {

	/**
	 * 根据id获取
	 * @param id
	 * @return
	 */
	Run getRun(long id);

}
