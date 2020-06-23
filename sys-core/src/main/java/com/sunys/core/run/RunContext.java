package com.sunys.core.run;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RunContext
 * @author sunys
 * @date Dec 21, 2019
 */
public class RunContext {

	private static final Logger logger = LoggerFactory.getLogger(RunContext.class);
	
	private static final ThreadLocal<LinkedList<Object>> runThreadLocal = new ThreadLocal<>();
	
	/**
	 * 添加当前线程正在运行的接口
	 * @param run
	 */
	public static void pushRun(Object run) {
		LinkedList<Object> linkedList = getStack();
		linkedList.push(run);
		logger.info("RunContext push Run, Class:{}, name:{}", run.getClass().getSimpleName());
	}
	
	/**
	 * 移除当前线程正在运行的接口
	 */
	public static void popRun() {
		LinkedList<Object> linkedList = getStack();
		Object run = linkedList.pop();
		if (linkedList.size() == 0) {
			runThreadLocal.remove();
		}
		logger.info("RunContext pop Run, Class:{}, name:{}, id:{}", run.getClass().getSimpleName());
	}
	
	/**
	 * 获取当前线程运行的接口
	 * @param <T>
	 * @return
	 */
	public static <T> T currentRun() {
		LinkedList<Object> linkedList = getStack();
		Object run = linkedList.getFirst();
		return (T) run;
	}
	
	private static LinkedList<Object> getStack() {
		LinkedList<Object> linkedList = runThreadLocal.get();
		if (linkedList == null) {
			linkedList = new LinkedList<>();
			runThreadLocal.set(linkedList);
		}
		return linkedList;
	}

}
