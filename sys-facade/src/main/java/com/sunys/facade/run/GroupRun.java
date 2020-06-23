package com.sunys.facade.run;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * GroupRun
 * @author sunys
 * @date Dec 21, 2019
 */
public interface GroupRun<T> {

	/**
	 * 组里面包含的run对象
	 * @return
	 */
	Iterable<T> getRuns();
	
	/**
	 * 递归整个组，是clazz的子类，添加到list中
	 * @param clazz
	 * @param list
	 */
	default void recursion(Class<? extends T> clazz, List<T> list) {
		recursion(run -> clazz.isInstance(run), list);
	}
	
	/**
	 * 递归整个组，将符合条件的添加到list中
	 * @param predicate
	 * @param list
	 */
	default void recursion(Predicate<T> predicate, List<T> list) {
		recursion(run -> {
			if (predicate.test(run)) {
				list.add(run);
			}
		});
	}
	
	/**
	 * 递归整个组，调用consumer接口
	 * @param consumer
	 */
	default void recursion(Consumer<T> consumer) {
		Iterable<T> runs = getRuns();
		for (T run : runs) {
			consumer.accept(run);
			if (run instanceof GroupRun) {
				GroupRun<T> groupRun = (GroupRun<T>) run;
				groupRun.recursion(consumer);
			}
		}
	}

}
