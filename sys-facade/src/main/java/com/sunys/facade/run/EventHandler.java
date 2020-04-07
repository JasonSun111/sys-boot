package com.sunys.facade.run;

public interface EventHandler<P> {

	Object handle(Event<P> event);
}
