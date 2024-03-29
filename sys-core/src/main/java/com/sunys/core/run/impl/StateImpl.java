package com.sunys.core.run.impl;

import com.sunys.facade.run.EventHandler;
import com.sunys.facade.run.EventType;
import com.sunys.facade.run.State;
import com.sunys.facade.run.StateEvent;
import com.sunys.facade.run.StateType;
import com.sunys.facade.run.Subject;

/**
 * StateImpl
 * @author sunys
 * @date Jun 11, 2020
 */
public class StateImpl<T extends StateType> implements State<T> {

	private Subject subject;
	
	private T type;
	
	public StateImpl(Subject subject, T type) {
		if (subject == null) {
			subject = new SubjectImpl();
		}
		this.subject = subject;
		this.type = type;
	}
	
	public StateImpl(T type) {
		this(null, type);
	}

	@Override
	public T type() {
		return type;
	}

	@Override
	public void registEventHandler(EventType type, EventHandler<?> eventHandler) {
		subject.registEventHandler(type, eventHandler);
	}

	@Override
	public void removeEventHandler(EventType type) {
		subject.removeEventHandler(type);
	}

	@Override
	public void removeEventHandler(EventHandler<?> eventHandler) {
		subject.removeEventHandler(eventHandler);
	}

	@Override
	public <P> void handle(StateEvent<P, ? extends State<? extends StateType>> event) {
		subject.event(event);
	}

}
