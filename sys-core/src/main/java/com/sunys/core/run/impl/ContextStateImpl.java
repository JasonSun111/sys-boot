package com.sunys.core.run.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.ContextState;
import com.sunys.facade.run.State;
import com.sunys.facade.run.StateEvent;

public class ContextStateImpl<T extends State> implements ContextState<T> {

	private static final Logger log = LoggerFactory.getLogger(ContextStateImpl.class);
	
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
		T state = event.getState();
		log.info("State change, {} -> {}", this.state.type().getName(), state.type().getName());
		state.handle(event);
		this.state = state;
	}

}
