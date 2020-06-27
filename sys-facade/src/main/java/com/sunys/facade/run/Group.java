package com.sunys.facade.run;

import java.util.List;
import java.util.function.Consumer;

/**
 * Group
 * @author sunys
 * @date Dec 21, 2019
 */
public interface Group<T> {

	/**
	 * 组里面包含的run对象
	 * @return
	 */
	Iterable<T> iterable();
	
	/**
	 * 递归整个组，是clazz的子类，添加到list中
	 * @param clazz
	 * @param list
	 */
	default <E> void recursion(Class<? extends E> clazz, List<E> list) {
		recursion(obj -> {
			if (clazz.isInstance(obj)) {
				list.add((E) obj);
			}
		});
	}
	
	/**
	 * 递归整个组，调用consumer接口
	 * @param consumer
	 */
	default void recursion(Consumer<Object> consumer) {
		Iterable<T> runs = iterable();
		for (T run : runs) {
			consumer.accept(run);
			if (run instanceof Group) {
				Group<T> groupRun = (Group<T>) run;
				groupRun.recursion(consumer);
			}
		}
	}

}
