package com.sunys.core.run.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunys.facade.run.ContextState;
import com.sunys.facade.run.State;
import com.sunys.facade.run.StateEvent;
import com.sunys.facade.run.StateType;

public class ContextStateImpl<T extends State<? extends StateType>> implements ContextState<T> {

	private static final Logger log = LoggerFactory.getLogger(ContextStateImpl.class);
	
	private volatile T state;
	
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
