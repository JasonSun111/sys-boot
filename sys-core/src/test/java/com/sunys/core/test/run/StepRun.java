package com.sunys.core.test.run;

import com.sunys.core.run.impl.interceptor.ContextRunMethodInterceptor;
import com.sunys.core.run.impl.interceptor.LogRunMethodInterceptor;
import com.sunys.facade.annotation.Interceptor;
import com.sunys.facade.run.Run;

public interface StepRun extends Run {

	@Interceptor({ContextRunMethodInterceptor.class, LogRunMethodInterceptor.class})
	@Override
	void run() throws Exception;
}
