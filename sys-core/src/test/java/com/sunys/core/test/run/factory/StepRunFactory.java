package com.sunys.core.test.run.factory;

import com.sunys.core.run.impl.factory.AbstractRunFactory;
import com.sunys.core.test.bean.Step;
import com.sunys.core.test.run.StepRunImpl;
import com.sunys.core.test.run.TimeoutCheckImpl;

public class StepRunFactory extends AbstractRunFactory<StepRunImpl> {

	@Override
	public StepRunImpl createRun(Object obj) {
		Step step = (Step) obj;
		StepRunImpl stepRun = new StepRunImpl();
		stepRun.setStep(step);
		stepRun.setTimeoutCheck(new TimeoutCheckImpl(8, null));
		return stepRun;
	}

}
