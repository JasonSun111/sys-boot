package com.sunys.core.test.run.factory;

import java.util.List;
import java.util.Map;

import com.sunys.core.run.impl.factory.AbstractRunFactory;
import com.sunys.core.test.bean.Dut;
import com.sunys.core.test.bean.Testcase;
import com.sunys.core.test.run.TestcaseRootGroupRun;
import com.sunys.core.test.run.TimeoutCheckImpl;
import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.RunFactory;
import com.sunys.facade.run.RunType;

public class TestcaseRunFactory extends AbstractRunFactory<TestcaseRootGroupRun> {

	@Override
	public TestcaseRootGroupRun createRun(Object obj) throws Exception {
		Testcase testcase = (Testcase) obj;
		TestcaseRootGroupRun testcaseRun = new TestcaseRootGroupRun();
		List<Dut> duts = testcase.getDuts();
		for (Dut dut : duts) {
			GroupRun<Run> dutRun = RunFactory.getRun(dut);
			testcaseRun.getDuts().add(dutRun);
		}
		testcaseRun.setTimeoutCheck(new TimeoutCheckImpl(1000, null));
		testcaseRun.setRunType(RunType.parallel);
		Map<Long, Run> runMap = testcaseRun.runMap();
		testcaseRun.recursion(Run.class, run -> runMap.put(run.getId(), run));
		return testcaseRun;
	}

}
