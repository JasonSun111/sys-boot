package com.sunys.facade.bean;

import java.util.Date;

import com.sunys.facade.exception.CommonEIEnum;
import com.sunys.facade.exception.BusiException;
import com.sunys.facade.exception.ExceptionInfo;

/**
 * Controller返回的结果
 * ResultBean
 * @author sunys
 * @date 2019年1月5日
 */
public class ResultBean<T> {

	public static final byte SUCCESS_STATUS = 1;
	public static final byte ERROR_STATUS = 0;

	private T result;
	private Date time;
	private Byte status;
	private ErrorData errorData;

	/**
	 * 返回成功
	 * @param result
	 * @return
	 */
	public static <T> ResultBean<T> successResult(T result){
		ResultBean<T> resultBean = new ResultBean<>();
		resultBean.setResult(result);
		resultBean.setTime(new Date());
		resultBean.setStatus(SUCCESS_STATUS);
		return resultBean;
	}

	/**
	 * 返回失败
	 * @param th
	 * @return
	 */
	public static ResultBean<?> failResult(Throwable th){
		ResultBean<?> resultBean = new ResultBean<>();
		if(th instanceof BusiException){
			BusiException be = (BusiException) th;
			resultBean.setErrorData(be.getErrorData());
		}else{
			ErrorData errorData = new ErrorData();
			errorData.setErrorInfo(CommonEIEnum.UNKNOWN_EXCEPTION);
			resultBean.setErrorData(errorData);
		}
		resultBean.setTime(new Date());
		resultBean.setStatus(ERROR_STATUS);
		return resultBean;
	}
	
	/**
	 * 返回失败
	 * @param errorInfo
	 * @return
	 */
	public static ResultBean<?> failResult(ExceptionInfo errorInfo,Object... args){
		ResultBean<?> resultBean = new ResultBean<>();
		ErrorData errorData = new ErrorData();
		errorData.setErrorInfo(errorInfo);
		errorData.setData(args);
		resultBean.setErrorData(errorData);
		resultBean.setTime(new Date());
		resultBean.setStatus(ERROR_STATUS);
		return resultBean;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

}
