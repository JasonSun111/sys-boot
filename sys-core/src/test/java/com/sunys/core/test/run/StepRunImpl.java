package com.sunys.core.test.run;

import com.sunys.core.run.impl.AbstractRun;
import com.sunys.core.test.bean.Step;

public class StepRunImpl extends AbstractRun implements StepRun {

	private Step step;
	
	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() throws Exception {
		timeoutCheck.await(step.getTime());
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		timeoutCheck.reset();
	}

	@Override
	public long calculateRunDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}

}
