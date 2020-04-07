package com.sunys.facade.run;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * GroupRun
 * @author sunys
 * @date Dec 21, 2019
 */
public interface GroupRun<T extends Run> extends Run {

	/**
	 * 组里面包含的run对象
	 * @return
	 */
	List<T> getRuns();
	
	/**
	 * 递归整个组，是clazz的子类，添加到list中
	 * @param clazz
	 * @param list
	 */
	default void recursion(Class<? extends Run> clazz, List<Run> list) {
		recursion(clazz, null, list);
	}
	
	/**
	 * 递归整个组，是clazz的子类且符合条件的，添加到list中
	 * @param clazz
	 * @param predicate
	 * @param list
	 */
	default void recursion(Class<? extends Run> clazz, Predicate<Run> predicate, List<Run> list) {
		recursion(clazz, run -> {
			if (predicate.test(run)) {
				list.add(run);
			}
		});
	}
	
	/**
	 * 递归整个组，是clazz子类的，调用consumer接口
	 * @param clazz
	 * @param consumer
	 */
	default void recursion(Class<? extends Run> clazz, Consumer<Run> consumer) {
		List<T> runs = getRuns();
		for (T run : runs) {
			if (clazz.isInstance(run)) {
				consumer.accept(run);
			}
			if (run instanceof GroupRun) {
				GroupRun<? extends Run> groupRun = (GroupRun) run;
				groupRun.recursion(clazz, consumer);
			}
		}
	}

}
