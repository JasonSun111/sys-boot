package com.sunys.core.run.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.sunys.facade.run.Event;
import com.sunys.facade.run.EventHandler;
import com.sunys.facade.run.EventType;
import com.sunys.facade.run.Subject;

/**
 * SubjectImpl
 * @author sunys
 * @date Apr 19, 2020
 */
public class SubjectImpl implements Subject {

	private Map<EventType, Set<EventHandler<?>>> eventHandlerMap = new HashMap<>();
	
	@Override
	public void registEventHandler(EventType type, EventHandler<?> eventHandler) {
		Set<EventHandler<?>> set = eventHandlerMap.get(type);
		if (set == null) {
			set = new HashSet<>();
			eventHandlerMap.put(type, set);
		}
		set.add(eventHandler);
	}

	@Override
	public void removeEventHandler(EventType type) {
		eventHandlerMap.remove(type);
	}

	@Override
	public void removeEventHandler(EventHandler<?> eventHandler) {
		for (Map.Entry<EventType, Set<EventHandler<?>>> entry : eventHandlerMap.entrySet()) {
			Set<EventHandler<?>> set = entry.getValue();
			if (set.remove(eventHandler)) {
				return;
			}
		}
	}

	@Override
	public <P> void event(Event<P> event) {
		EventType type = event.type();
		Set<EventHandler<?>> set = eventHandlerMap.get(type);
		for (Iterator<EventHandler<?>> it = set.iterator(); it.hasNext();) {
			EventHandler<P> eventHandler = (EventHandler<P>) it.next();
			eventHandler.handle(event);
		}
	}
}
