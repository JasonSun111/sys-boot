package com.sunys.core.run.impl;

import com.sunys.facade.run.State;
import com.sunys.facade.run.StateEvent;
import com.sunys.facade.run.StateType;
import com.sunys.facade.run.Subject;

public class StateImpl implements State {

	private Subject subject;
	
	private StateType type;
	
	public StateImpl(Subject subject, StateType type) {
		this.subject = subject;
		this.type = type;
	}

	@Override
	public StateType type() {
		return type;
	}

	@Override
	public <P> void handle(StateEvent<P, ? extends State> event) {
		subject.event(event);
	}

}
