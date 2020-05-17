package com.sunys.core.run.impl;

import com.sunys.facade.run.ContextState;
import com.sunys.facade.run.State;

public class ContextStateImpl implements ContextState {

	private State state;
	
	@Override
	public State currentState() {
		return state;
	}

	@Override
	public void change(State state) {
		// TODO Auto-generated method stub
		
	}

}
