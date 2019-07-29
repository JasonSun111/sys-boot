/*
 * @author sunys
 * @date 2019年4月3日
 */
package com.sunys.facade.exception;

/**
 * CommonEIEnum
 * @author sunys
 * @date 2019年4月3日
 */
public enum CommonEIEnum implements ExceptionInfo {

	UNKNOWN_EXCEPTION(ERROR, "000", false, "unknown exception"),
	LOGIN_EXCEPTION(ERROR, "001", false,"login exception"),
	JSON_PARSE_EXCEPTION(ERROR,"002", false,"json parse exception"),
	NOSUCH_METHOD_EXCEPTION(ERROR,"003", false,"no such method exception"),
	REQUIRE_PARAM_NULL_EXCEPTION(ERROR,"004", false,"require parameter is null"),
	PARAMETER_CONVERT_EXCEPTION(ERROR,"005", false,"parameter convert exception"),
	REMOTE_SERVICE_UNKNOWN_EXCEPTION(ERROR,"006", false,"remote service unknown exception"),
	;

	private String msg;
	private String code;
	private String level;
	private Boolean isToast;

	private CommonEIEnum(String level,String code,String msg) {
		this(level, code, true, msg);
	}

	private CommonEIEnum(String level,String code,Boolean isToast,String msg) {
		this.msg = msg;
		this.code = code;
		this.level = level;
		this.isToast = isToast;
	}

	@Override
	public String getLevel() {
		return level;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Boolean getIsToast() {
		return isToast;
	}

	@Override
	public String toString() {
		return "BaseEIEnum [msg=" + msg + ", code=" + code + ", level=" + level
				+ ", isToast=" + isToast + "]";
	}

}
