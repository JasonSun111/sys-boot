package com.sunys.facade.run;

public interface StateEvent<P, T extends State<? extends StateType>> extends Event<P> {

	T getState();

}
