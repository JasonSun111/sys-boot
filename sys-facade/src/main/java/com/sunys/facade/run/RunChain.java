package com.sunys.facade.run;

import java.lang.reflect.Method;

/**
 * 封装了调用一次接口方法需要执行的拦截器和目标方法
 * RunChain
 * @author sunys
 * @date Dec 21, 2019
 */
public interface RunChain {

	/**
	 * 执行拦截器栈和目标方法
	 * @return
	 * @throws Exception
	 */
	Object invoke() throws Exception;

	/**
	 * 获取目标对象
	 * @return
	 */
	Run getTarget();

	/**
	 * 获取代理对象
	 * @return
	 */
	Run getProxy();

	/**
	 * 获取目标的方法
	 * @return
	 */
	Method getMethod();

	/**
	 * 方法传入的参数
	 * @return
	 */
	Object[] getArgs();

}
