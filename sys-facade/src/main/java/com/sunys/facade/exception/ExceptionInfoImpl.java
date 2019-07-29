package com.sunys.facade.exception;

/**
 * ExceptionInfoImpl
 * @author sunys
 * @date 2019年4月3日
 */
public class ExceptionInfoImpl implements ExceptionInfo {

	private String msg;
	private String code;
	private String level;
	private Boolean isToast;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getLevel() {
		return level;
	}

	@Override
	public Boolean getIsToast() {
		return isToast;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setIsToast(Boolean isToast) {
		this.isToast = isToast;
	}

	@Override
	public String toString() {
		return "ExceptionInfoImpl [msgCode=" + msg + ", code=" + code + ", level=" + level
				+ ", isToast=" + isToast + "]";
	}

}
