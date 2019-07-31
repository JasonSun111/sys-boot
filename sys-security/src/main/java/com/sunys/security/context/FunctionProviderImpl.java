package com.sunys.security.context;

import java.util.List;

import com.sunys.facade.bean.DapFunction;
import com.sunys.core.context.FunctionProvider;

/**
 * 获取系统功能菜单
 * FunctionProviderImpl
 * @author sunys
 * @date 2019年7月22日
 */
public class FunctionProviderImpl implements FunctionProvider {

	@Override
	public List<? extends DapFunction> getFunctions() {
		return null;
	}
	
	@Override
	public DapFunction getFunction(String codeId) {
		return null;
	}
}
