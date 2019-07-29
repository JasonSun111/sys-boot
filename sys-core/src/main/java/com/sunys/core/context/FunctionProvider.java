package com.sunys.core.context;

import java.util.List;

import com.sunys.facade.annotation.ServiceParam;
import com.sunys.facade.bean.DapFunction;

/**
 * 获取系统功能菜单
 * FunctionProvider
 * @author sunys
 * @date 2019年1月18日
 */
public interface FunctionProvider {

	/**
	 * 所有功能列表树
	 * @return
	 */
	List<? extends DapFunction> getFunctions();

	/**
	 * 根据codeId查询功能
	 * @param codeId
	 * @return
	 */
	DapFunction getFunction(@ServiceParam("codeId") String codeId);
}
