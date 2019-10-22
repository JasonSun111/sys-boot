package com.sunys.core.run;

public interface RunFactory<T extends Run> {

	T getInstance() throws Exception;
}
