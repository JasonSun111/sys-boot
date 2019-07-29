package com.sunys.facade.bean;

/**
 * 前端处理的消息
 * SocketMessage
 * @author sunys
 * @date 2019年3月5日
 */
public class SocketMessage {

	//前端回调函数的名称
	private String funcName;
	//服务端返回的数据
	private Object data;

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
