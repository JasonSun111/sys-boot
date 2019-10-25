package com.sunys.core.run;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sunys.facade.run.RootGroupRun;
import com.sunys.facade.run.Run;

public class RunContext {

	private static final ThreadLocal<Run> runThreadLocal = new ThreadLocal<>();
	
	private static final Map<Long, RootGroupRun<Run>> rootGroupRunMap = new ConcurrentHashMap<>();
	
	public void setRun(Run run) {
		runThreadLocal.set(run);
	}
	
	public void removeRun() {
		runThreadLocal.remove();
	}
	
	public static Run getRun(Long rootId, Long id) {
		RootGroupRun<Run> rootGroupRun = rootGroupRunMap.get(rootId);
		Run run = rootGroupRun.runMap().get(id);
		return run;
	}
	
	public static RootGroupRun<Run> getRoot(Long rootId) {
		RootGroupRun<Run> rootGroupRun = rootGroupRunMap.get(rootId);
		return rootGroupRun;
	}
}
