package com.sunys.core.test.run;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sunys.core.run.impl.AbstractGroupRun;

public class DutGroupRunImpl extends AbstractGroupRun<StepRun> implements DutGroupRun {

	private ExecutorService executorService = Executors.newFixedThreadPool(8);
	
	private List<StepRun> steps = new ArrayList<>();
	
	@Override
	public List<StepRun> getRuns() {
		return steps;
	}

	@Override
	protected ExecutorService getExecutorService() {
		return executorService;
	}

	public List<StepRun> getSteps() {
		return steps;
	}

	public void setSteps(List<StepRun> steps) {
		this.steps = steps;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
}
