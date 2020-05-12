package com.sunys.core.run.impl;

import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.Run;

/**
 * AbstractGroupRun
 * @author sunys
 * @date Dec 21, 2019
 */
public abstract class AbstractGroupRun<T extends Run> extends AbstractRun implements GroupRun<T> {

	@Override
	public Iterable<T> getRuns() {
		// TODO Auto-generated method stub
		return null;
	}

}
