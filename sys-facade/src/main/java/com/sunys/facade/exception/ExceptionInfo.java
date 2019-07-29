/*
 * @author sunys
 * @date 2019年4月3日
 */
package com.sunys.facade.exception;

/**
 * ExceptionInfo
 * @author sunys
 * @date 2019年4月3日
 */
public interface ExceptionInfo {

	String INFO = "info";

	String ERROR = "error";

	String WARNING = "warning";

	/**
	 * 异常信息等级
	 * @return
	 */
	String getLevel();

	/**
	 * 异常的编码
	 * @return
	 */
	String getCode();

	/**
	 * 异常信息
	 * @return
	 */
	String getMsg();

	/**
	 * 错误后是否用toast提示
	 * @return
	 */
	default Boolean getIsToast(){
		return true;
	}

}
