package com.sunys.core.run;

import java.util.HashMap;
import java.util.Map;

public class RunContext {

	private static final Map<Long, RootGroupRun<Run>> rootGroupRunMap = new HashMap<>();
	
	public static <T> T getRun() {
		
		return null;
	}
	
	public static <T extends Run> T getParents(Run run, Class<T> clazz) {
		
		return null;
	}
	
	public static RootGroupRun<Run> getRoot(Long id) {
		RootGroupRun<Run> rootGroupRun = rootGroupRunMap.get(id);
		return rootGroupRun;
	}
}
