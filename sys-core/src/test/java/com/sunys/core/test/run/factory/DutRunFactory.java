package com.sunys.core.test.run.factory;

import java.util.List;

import com.sunys.core.run.impl.factory.AbstractRunFactory;
import com.sunys.core.test.bean.Dut;
import com.sunys.core.test.bean.Step;
import com.sunys.core.test.run.DutGroupRunImpl;
import com.sunys.core.test.run.StepRun;
import com.sunys.core.test.run.TimeoutCheckImpl;
import com.sunys.facade.run.RunFactory;
import com.sunys.facade.run.RunType;

public class DutRunFactory extends AbstractRunFactory<DutGroupRunImpl> {

	@Override
	public DutGroupRunImpl createRun(Object obj) throws Exception {
		Dut dut = (Dut) obj;
		DutGroupRunImpl dutRun = new DutGroupRunImpl();
		List<Step> steps = dut.getSteps();
		for (Step step : steps) {
			StepRun stepRun = RunFactory.getRun(step);
			dutRun.getRuns().add(stepRun);
		}
		dutRun.setRunType(RunType.parallel);
		dutRun.setTimeoutCheck(new TimeoutCheckImpl(100, null));
		return dutRun;
	}

}
