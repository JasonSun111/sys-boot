package com.sunys.facade.run;

import java.util.Map;

/**
 * RootGroupRun
 * @author sunys
 * @date Dec 21, 2019
 */
public interface RootGroupRun<T extends Run> extends GroupRun<T> {

	/**
	 * 获取所有的子节点
	 * @return
	 */
	Map<Long, Run> runMap();
}
