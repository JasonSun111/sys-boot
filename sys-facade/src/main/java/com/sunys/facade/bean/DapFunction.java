package com.sunys.facade.bean;

import java.util.List;

/**
 * 系统功能
 * Function
 * @author sunys
 * @date 2019年1月18日
 */
public interface DapFunction {

	String getCodeId();
	
	String getName();
	
	Byte getType();
	
	String getIcon();
	
	String getFuncUrl();
	
	Integer getSeq();
	
	DapFunction getParent();
	
	List<? extends DapFunction> getSubFunctions();
}
