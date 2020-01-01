package com.sunys.core.test.run;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sunys.core.run.impl.AbstractGroupRun;
import com.sunys.core.test.bean.Testcase;
import com.sunys.facade.run.RootGroupRun;
import com.sunys.facade.run.Run;

public class TestcaseRootGroupRun extends AbstractGroupRun<DutGroupRun> implements RootGroupRun<DutGroupRun> {

	private ExecutorService executorService = Executors.newFixedThreadPool(8);
	
	private Testcase testcase;
	
	private List<DutGroupRun> duts = new ArrayList<>();

	private Map<Long, Run> runMap = new HashMap<>();
	
	@Override
	public List<DutGroupRun> getRuns() {
		return duts;
	}

	@Override
	public Map<Long, Run> runMap() {
		return runMap;
	}

	@Override
	protected ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public Testcase getTestcase() {
		return testcase;
	}

	public void setTestcase(Testcase testcase) {
		this.testcase = testcase;
	}

	public List<DutGroupRun> getDuts() {
		return duts;
	}

	public void setDuts(List<DutGroupRun> duts) {
		this.duts = duts;
	}

}
