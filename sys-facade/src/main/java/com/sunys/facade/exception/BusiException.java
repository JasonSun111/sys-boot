package com.sunys.facade.exception;

import java.util.Arrays;

import com.sunys.facade.bean.ErrorData;

/**
 * BusiException
 * @author sunys
 * @date 2019年1月5日
 */
public class BusiException extends RuntimeException {

	private static final long serialVersionUID = -5541819847886218352L;

	private ExceptionInfo info;
	private Object[] args;
	private ErrorData errorData;

	public BusiException(ExceptionInfo info,Object... args) {
		this.info = info;
		this.args = args;
		errorData = new ErrorData();
		errorData.setData(args);
		errorData.setErrorInfo(info);
	}

	public ExceptionInfo getInfo() {
		return info;
	}

	public void setInfo(ExceptionInfo info) {
		this.info = info;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	@Override
	public String getMessage() {
		return String.format(info.getMsg(), args);
	}

	@Override
	public String toString() {
		return "BusiException [info=" + info + ", args=" + Arrays.toString(args) + "]";
	}

}
