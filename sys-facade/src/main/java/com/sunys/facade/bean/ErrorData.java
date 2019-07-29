package com.sunys.facade.bean;

import com.sunys.facade.exception.ExceptionInfo;

/**
 * 错误提示信息
 * ErrorData
 * @author sunys
 * @date 2019年7月7日
 */
public class ErrorData {

	private Object[] data;
	private ExceptionInfo errorInfo;

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}

	public ExceptionInfo getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(ExceptionInfo errorInfo) {
		this.errorInfo = errorInfo;
	}
}
