package com.sunys.facade.run;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Group
 * @author sunys
 * @date Dec 21, 2019
 */
public interface Group<T> {

	/**
	 * 组里面包含的对象
	 * @return
	 */
	Iterable<T> iterable();
	
	/**
	 * 递归整个组，是clazz的子类，添加到list中
	 * @param clazz
	 * @param list
	 */
	default <E> void recursion(Class<? extends E> clazz, List<E> list) {
		dfs(obj -> {
			if (clazz.isInstance(obj)) {
				list.add((E) obj);
			}
		});
	}

	/**
	 * 深度优先，递归整个组
	 * @param consumer
	 */
	default void dfs(Consumer<Object> consumer) {
		consumer.accept(this);
		dfsSub(consumer);
	}
	
	/**
	 * 深度优先，递归组下面的元素
	 * @param consumer
	 */
	default void dfsSub(Consumer<Object> consumer) {
		Iterable<? extends T> domains = iterable();
		if (domains == null) {
			return;
		}
		for (T obj : domains) {
			consumer.accept(obj);
			if (obj instanceof Group) {
				Group<?> group = (Group<?>) obj;
				group.dfsSub(consumer);
			}
		}
	}
	
	/**
	 * 广度优先，递归整个组
	 * @param consumer
	 */
	default void bfs(Consumer<Object> consumer) {
		consumer.accept(this);
		List<Group<?>> groups = new ArrayList<>();
		groups.add(this);
		bfsSub(consumer, groups);
	}
	
	/**
	 * 广度优先，递归组下面的元素
	 * @param consumer
	 * @param list
	 */
	default void bfsSub(Consumer<Object> consumer, List<Group<?>> list) {
		List<Group<?>> groups = new ArrayList<>();
		for (Group<?> group : list) {
			Iterable<?> domains = group.iterable();
			if (domains == null) {
				continue;
			}
			for (Object obj : domains) {
				consumer.accept(obj);
				if (obj instanceof Group) {
					groups.add((Group<?>) obj);
				}
			}
		}
		bfsSub(consumer, groups);
	}

}
