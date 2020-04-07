package com.sunys.core.run.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import com.sunys.facade.run.Event;
import com.sunys.facade.run.EventHandler;
import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.Run;

/**
 * AbstractRun
 * @author sunys
 * @date Dec 21, 2019
 */
public abstract class AbstractRun implements Run {

	private static final LongAdder longAdder = new LongAdder();
	
	private Map<Integer, EventHandler<?>> eventHandlerMap = new HashMap<>();
	
	private Run proxy;
	
	protected long id;
	
	protected GroupRun<? extends Run> parent;
	
	public AbstractRun() {
		synchronized (AbstractRun.class) {
			longAdder.increment();
			this.id = longAdder.sum();
		}
	}

	@Override
	public Run getProxy() {
		return proxy;
	}

	@Override
	public void setProxy(Run proxy) {
		this.proxy = proxy;
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setParent(GroupRun<? extends Run> parent) {
		this.parent = parent;
	}

	@Override
	public void registEventHandler(int type, EventHandler<?> eventHandler) {
		eventHandlerMap.put(type, eventHandler);
	}

	@Override
	public void unregistEventHandler(int type) {
		eventHandlerMap.remove(type);
	}

	@Override
	public <P, R> R event(Event<P> event) {
		int type = event.type();
		EventHandler<P> eventHandler = (EventHandler<P>) eventHandlerMap.get(type);
		Object result = eventHandler.handle(event);
		return (R) result;
	}

}
