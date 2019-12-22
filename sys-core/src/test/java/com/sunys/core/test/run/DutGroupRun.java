package com.sunys.core.test.run;

import com.sunys.core.run.impl.interceptor.ContextRunMethodInterceptor;
import com.sunys.core.run.impl.interceptor.LogRunMethodInterceptor;
import com.sunys.facade.annotation.Interceptor;
import com.sunys.facade.run.GroupRun;

public interface DutGroupRun extends GroupRun<StepRun> {

	@Interceptor({ContextRunMethodInterceptor.class, LogRunMethodInterceptor.class})
	@Override
	void run() throws Exception;
}
