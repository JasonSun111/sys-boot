package com.sunys.facade.run;

/**
 * 方法拦截器
 * RunMethodInterceptor
 * @author sunys
 * @date Dec 21, 2019
 */
public interface RunMethodInterceptor {

	/**
	 * 控制目标方法是否执行
	 * @param runChain
	 * @return
	 * @throws Exception
	 */
	Object intercept(RunChain runChain) throws Exception;
}
