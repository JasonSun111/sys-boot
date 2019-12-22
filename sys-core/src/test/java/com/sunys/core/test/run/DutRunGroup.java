package com.sunys.core.test.run;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sunys.core.run.impl.AbstractGroupRun;
import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.Run;

public class DutRunGroup extends AbstractGroupRun<Run> implements GroupRun<Run> {

	private ExecutorService executorService = Executors.newFixedThreadPool(8);
	
	private List<Run> steps = new ArrayList<>();
	
	@Override
	public List<Run> getRuns() {
		return steps;
	}

	@Override
	protected ExecutorService getExecutorService() {
		return executorService;
	}

	public List<Run> getSteps() {
		return steps;
	}

	public void setSteps(List<Run> steps) {
		this.steps = steps;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
}
