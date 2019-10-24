package com.sunys.core.run;

import java.util.Map;

public interface RootGroupRun<T extends Run> extends GroupRun<T> {

	Map<Long, Run> runMap();
}
