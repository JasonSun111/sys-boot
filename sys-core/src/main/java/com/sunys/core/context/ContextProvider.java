package com.sunys.core.context;

import com.sunys.facade.annotation.ServiceParam;
import com.sunys.facade.bean.DapContext;

/**
 * ContextProvider
 * @author sunys
 * @date 2019年1月11日
 */
public interface ContextProvider {

	/**
	 * 获取当前用户的登陆信息
	 * @return
	 */
	DapContext getContext();

	/**
	 * 根据ck获取当前用户的登陆信息
	 * @param ck
	 * @return
	 */
	DapContext getContextByCk(@ServiceParam("ck") String ck);
}
