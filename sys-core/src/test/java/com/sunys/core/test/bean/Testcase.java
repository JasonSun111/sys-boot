package com.sunys.core.test.bean;

import java.util.ArrayList;
import java.util.List;

import com.sunys.facade.annotation.Factory;

@Factory("com.sunys.core.test.run.factory.TestcaseRunFactory")
public class Testcase {

	private List<Dut> duts = new ArrayList<>();

	public List<Dut> getDuts() {
		return duts;
	}

	public void setDuts(List<Dut> duts) {
		this.duts = duts;
	}
}
