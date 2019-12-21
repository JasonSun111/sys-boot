package com.sunys.facade.run;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface GroupRun<T extends Run> extends Run {

	RunType getRunType();
	
	List<T> getRuns();
	
	default void recursion(Class<? extends Run> clazz, List<Run> list) {
		recursion(clazz, null, list);
	}
	
	default void recursion(Class<? extends Run> clazz, Predicate<Run> predicate, List<Run> list) {
		List<T> runs = getRuns();
		for (T run : runs) {
			if (clazz.isInstance(runs)) {
				if (predicate != null && predicate.test(run)) {
					list.add(run);
				} else if (predicate == null) {
					list.add(run);
				}
			}
			if (run instanceof GroupRun) {
				recursion(clazz, list);
			}
		}
	}
	
	default void recursion(Class<? extends Run> clazz, Consumer<Run> consumer) {
		List<T> runs = getRuns();
		for (T run : runs) {
			if (clazz.isInstance(runs)) {
				consumer.accept(run);
			}
			if (run instanceof GroupRun) {
				recursion(clazz, consumer);
			}
		}
	}
	
	void eventRun(int eventIndex);
	
	double getProgress();
	
	void setEventRunStatus();
	
	void setEventRunStatus(RunStatus status);
}
