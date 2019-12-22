package com.sunys.core.test.run.factory;

import java.util.List;

import com.sunys.core.run.impl.factory.AbstractRunFactory;
import com.sunys.core.test.bean.Dut;
import com.sunys.core.test.bean.Step;
import com.sunys.core.test.run.DutRunGroup;
import com.sunys.core.test.run.TimeoutCheckImpl;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunFactory;

public class DutRunFactory extends AbstractRunFactory<DutRunGroup> {

	@Override
	public DutRunGroup createRun(Object obj) throws Exception {
		Dut dut = (Dut) obj;
		DutRunGroup dutRun = new DutRunGroup();
		List<Step> steps = dut.getSteps();
		for (Step step : steps) {
			Run stepRun = RunFactory.getRun(step);
			dutRun.getRuns().add(stepRun);
		}
		dutRun.setTimeoutCheck(new TimeoutCheckImpl(100, null));
		return dutRun;
	}

}
