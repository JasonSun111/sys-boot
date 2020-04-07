package com.sunys.facade.run;

public interface Event<P> {

	int type();
	
	P getParam();
}
