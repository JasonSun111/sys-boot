package com.sunys.facade.run;

import java.util.List;

public interface GroupRun<T extends Run> extends Run {

	RunType getRunType();
	
	List<T> getRuns();
}
