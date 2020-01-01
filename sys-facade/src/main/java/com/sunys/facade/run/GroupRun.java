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

	byte RUNTYPE_SERIAL = 1;
	byte RUNTYPE_PARALLEL = 2;
	byte RUNTYPE_EVENT = 3;
	
	/**
	 * 运行类型
	 * @return
	 */
	byte getRunType();
	
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
	
	/**
	 * RunType是event时，运行组中的一个run接口
	 * @param eventIndex
	 */
	void eventRun(int eventIndex);
	
	/**
	 * 获取当前运行的进度
	 * @return
	 */
	double getProgress();
	
	/**
	 * RunType是event时，设置状态，结束运行
	 */
	void setEventRunStatus();
	
	/**
	 * RunType是event时，设置状态，结束运行
	 * @param status
	 */
	void setEventRunStatus(Byte status);
}
