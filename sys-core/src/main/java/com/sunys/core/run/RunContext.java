package com.sunys.core.run;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

import com.sunys.facade.run.RootGroupRun;
import com.sunys.facade.run.Run;

public class RunContext {

	private static final ThreadLocal<LinkedList<Run>> runThreadLocal = new ThreadLocal<>();
	
	private static final Map<Long, RootGroupRun<Run>> rootGroupRunMap = new ConcurrentHashMap<>();
	
	public static void pushRun(Run run) {
		LinkedList<Run> linkedList = getStack();
		linkedList.push(run);
	}
	
	public static void popRun() {
		LinkedList<Run> linkedList = getStack();
		linkedList.pop();
		if (linkedList.size() == 0) {
			runThreadLocal.remove();
		}
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
	
	public static <T extends Run> T getRun(Long rootId, Long id) {
		RootGroupRun<Run> rootGroupRun = rootGroupRunMap.get(rootId);
		Run run = rootGroupRun.runMap().get(id);
		return (T) run;
	}
	
	public static RootGroupRun<Run> getRoot(Long rootId) {
		RootGroupRun<Run> rootGroupRun = rootGroupRunMap.get(rootId);
		return rootGroupRun;
	}
	
	public static ScheduledExecutorService getScheduledExecutorService() {
		return null;
	}
}
