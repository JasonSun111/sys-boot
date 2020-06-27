package com.sunys.facade.run;

/**
 * LinkedRun
 * @author sunys
 * @date May 1, 2020
 */
public interface LinkedRun {

	/**
	 * 获取上级节点
	 * @return
	 */
	Group<?> parent();
	
	/**
	 * 获取指定类型的上级节点
	 * @param clazz
	 * @return
	 */
	default Group<?> parents(Class<? extends Group<?>> clazz) {
		Group<?> parent = parent();
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
