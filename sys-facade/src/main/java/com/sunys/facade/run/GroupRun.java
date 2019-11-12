package com.sunys.facade.run;

import java.util.List;

public interface GroupRun<T extends Run> extends Run {

	RunType getRunType();
	
	List<T> getRuns();
	
	void eventRun(int eventIndex);
	
	void setEventRunStatus();
	
	void setEventRunStatus(RunStatus status);
}
