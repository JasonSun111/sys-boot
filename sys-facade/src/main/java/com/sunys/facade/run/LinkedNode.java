package com.sunys.facade.run;

public interface LinkedNode extends Node {

	LinkedNode next();
	
	LinkedNode pre();
	
	Node parent();
	
	default Node parents(Class<? extends Node> clazz) {
		if (clazz.isInstance(this)) {
			return this;
		}
		Node parent = parent();
		if (parent instanceof LinkedNode) {
			LinkedNode linkedNode = (LinkedNode) parent;
			return linkedNode.parents(clazz);
		}
		return null;
	}
}
