package com.sunys.core.run.impl;

import com.sunys.core.run.GroupRun;
import com.sunys.core.run.Run;
import com.sunys.core.run.RunStatus;

public abstract class AbstractGroupRun<T extends Run> extends AbstractRun implements GroupRun<T> {

	@Override
	public void init() {
		getRuns().forEach(Run::init);
		status = RunStatus.idle;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clean() {
		getRuns().forEach(Run::clean);
	}

	@Override
	public void reset() {
		clean();
		getRuns().forEach(Run::reset);
		status = RunStatus.idle;
	}

	@Override
	public void destory() {
		getRuns().forEach(Run::destory);
		status = RunStatus.destory;
	}

	@Override
	public long calculateRunDuration() {
		if (runDuration == null) {
			runDuration = getRuns().stream().map(Run::calculateRunDuration).reduce(0L, (v1, v2) -> v1 + v2);
		}
		return runDuration;
	}

}
