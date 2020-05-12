package com.sunys.facade.run;

/**
 * LinkedRun
 * @author sunys
 * @date May 1, 2020
 */
public interface LinkedRun extends Run {

	/**
	 * 获取上级节点
	 * @return
	 */
	GroupRun<Run> parent();
	
	/**
	 * 获取指定类型的上级节点
	 * @param clazz
	 * @return
	 */
	default GroupRun<Run> parents(Class<? extends GroupRun<? extends Run>> clazz) {
		GroupRun<Run> parent = parent();
		if (clazz.isInstance(parent)) {
			return parent;
		} else {
			if (parent instanceof LinkedRun) {
				LinkedRun linkedRun = (LinkedRun) parent;
				return linkedRun.parents(clazz);
			}
		}
		return null;
	}
	
}
