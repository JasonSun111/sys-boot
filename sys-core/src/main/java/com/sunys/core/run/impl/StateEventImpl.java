package com.sunys.core.run.impl;

import com.sunys.facade.run.State;
import com.sunys.facade.run.StateEvent;

public class StateEventImpl<P, T extends State> implements StateEvent<P, T> {

	private P param;
	
	private T state;
	
	private StateEventType type;
	
	public StateEventImpl(P param, T state, StateEventType type) {
		this.param = param;
		this.state = state;
		this.type = type;
	}

	@Override
	public StateEventType type() {
		return type;
	}

	@Override
	public P getParam() {
		return param;
	}

	@Override
	public T getState() {
		return state;
	}

}
