package com.sunys.core.run.impl;

import com.sunys.core.run.shell.ShellStateType;
import com.sunys.facade.run.State;
import com.sunys.facade.run.StateEvent;
import com.sunys.facade.run.Subject;

public class ShellState implements State {

	private Subject subject;
	
	private ShellStateType type;
	
	public ShellState(ShellStateType type) {
		this.type = type;
		this.subject = new SubjectImpl();
		init();
	}
	
	public ShellState(Subject subject, ShellStateType type) {
		this.subject = subject;
		this.type = type;
		init();
	}
	
	private void init() {
		
	}

	@Override
	public ShellStateType type() {
		return type;
	}

	@Override
	public <P> void handle(StateEvent<P, ? extends State> event) {
		subject.event(event);
	}

}
