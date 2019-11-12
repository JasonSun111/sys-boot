package com.sunys.core.run;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.Run;

public class RunContext {

	private static final Logger logger = LoggerFactory.getLogger(RunContext.class);
	
	private static final ThreadLocal<LinkedList<Run>> runThreadLocal = new ThreadLocal<>();
	
	private static final Map<Long, Run> runMap = new ConcurrentHashMap<>();
	
	public static void pushRun(Run run) {
		LinkedList<Run> linkedList = getStack();
		linkedList.push(run);
		logger.info("RunContext push Run, Class:{}, name:{}, id:{}", run.getClass().getSimpleName(), run.getName(), run.getId());
	}
	
	public static void popRun() {
		LinkedList<Run> linkedList = getStack();
		Run run = linkedList.pop();
		if (linkedList.size() == 0) {
			runThreadLocal.remove();
		}
		logger.info("RunContext pop Run, Class:{}, name:{}, id:{}", run.getClass().getSimpleName(), run.getName(), run.getId());
	}
	
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
	
	public static <T extends Run> T getRun(Long id) {
		Run run = runMap.get(id);
		return (T) run;
	}
	
	public static void putRun(Run run) {
		runMap.put(run.getId(), run);
	}
}
