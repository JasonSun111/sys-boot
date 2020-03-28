package com.sunys.core.run;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.Run;

/**
 * RunContext
 * @author sunys
 * @date Dec 21, 2019
 */
public class RunContext {

	private static final Logger logger = LoggerFactory.getLogger(RunContext.class);
	
	private static final ThreadLocal<LinkedList<Run>> runThreadLocal = new ThreadLocal<>();
	
	private static final Map<Long, Run> runMap = new ConcurrentHashMap<>();
	
	/**
	 * 添加当前线程正在运行的接口
	 * @param run
	 */
	public static void pushRun(Run run) {
		LinkedList<Run> linkedList = getStack();
		linkedList.push(run);
		logger.info("RunContext push Run, Class:{}, name:{}, id:{}", run.getClass().getSimpleName(), run.getId());
	}
	
	/**
	 * 移除当前线程正在运行的接口
	 */
	public static void popRun() {
		LinkedList<Run> linkedList = getStack();
		Run run = linkedList.pop();
		if (linkedList.size() == 0) {
			runThreadLocal.remove();
		}
		logger.info("RunContext pop Run, Class:{}, name:{}, id:{}", run.getClass().getSimpleName(), run.getId());
	}
	
	/**
	 * 获取当前线程运行的接口
	 * @param <T>
	 * @return
	 */
	public static <T extends Run> T currentRun() {
		LinkedList<Run> linkedList = getStack();
		Run run = linkedList.getFirst();
		return (T) run;
	}
	
	public static <T extends Run> T currentRun(int index) {
		LinkedList<Run> linkedList = getStack();
		Run run = linkedList.get(index);
		return (T) run;
	}
	
	private static LinkedList<Run> getStack() {
		LinkedList<Run> linkedList = runThreadLocal.get();
		if (linkedList == null) {
			linkedList = new LinkedList<>();
			runThreadLocal.set(linkedList);
		}
		return linkedList;
	}
	
	/**
	 * 根据id获取run接口
	 * @param <T>
	 * @param id
	 * @return
	 */
	public static <T extends Run> T getRun(Long id) {
		Run run = runMap.get(id);
		return (T) run;
	}
	
	/**
	 * 添加run接口
	 * @param run
	 */
	public static void putRun(Run run) {
		runMap.put(run.getId(), run);
	}
}
