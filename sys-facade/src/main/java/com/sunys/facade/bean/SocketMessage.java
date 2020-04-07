package com.sunys.facade.bean;

/**
 * websocket消息
 * SocketMessage
 * @author sunys
 * @date 2019年3月5日
 */
public class SocketMessage<T> {

	//消息标题
	private String title;
	//服务端返回的数据
	private T data;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
