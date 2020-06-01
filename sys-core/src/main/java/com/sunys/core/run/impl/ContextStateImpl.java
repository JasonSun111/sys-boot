package com.sunys.core.run.impl;

import com.sunys.facade.run.ContextState;
import com.sunys.facade.run.State;
import com.sunys.facade.run.StateEvent;

public class ContextStateImpl<T extends State> implements ContextState<T> {

	private T state;
	
	public ContextStateImpl(T state) {
		this.state = state;
	}

	@Override
	public T currentState() {
		return state;
	}

	@Override
	public <P> void change(StateEvent<P, T> event) {
		state.handle(event);
		this.state = event.getState();
	}

}
