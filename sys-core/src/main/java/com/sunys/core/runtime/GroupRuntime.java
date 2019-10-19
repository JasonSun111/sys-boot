package com.sunys.core.runtime;

import java.util.List;

public interface GroupRuntime<T extends Runtime> extends Runtime {

	ExecType getExecType();
	
	List<T> getRuntimes();
}
