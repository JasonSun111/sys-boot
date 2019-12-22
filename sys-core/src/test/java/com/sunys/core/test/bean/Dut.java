package com.sunys.core.test.bean;

import java.util.ArrayList;
import java.util.List;

import com.sunys.facade.annotation.Factory;

@Factory("com.sunys.core.test.run.factory.DutRunFactory")
public class Dut {

	private List<Step> steps = new ArrayList<>();

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
}
