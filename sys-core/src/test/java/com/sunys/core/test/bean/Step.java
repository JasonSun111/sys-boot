package com.sunys.core.test.bean;

import com.sunys.facade.annotation.Factory;

@Factory("com.sunys.core.test.run.factory.StepRunFactory")
public class Step {

	private int time = 4;

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
