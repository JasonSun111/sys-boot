/*
 * @author sunys
 * @date 2019年3月11日
 */
package com.sunys.web.websocket;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * 请求参数中的值赋值到WebSocketSession的attributes中
 * HttpServletRequestHandshakeInterceptor
 * @author sunys
 * @date 2019年3月11日
 */
public class HttpServletRequestHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
		HttpServletRequest req = serverRequest.getServletRequest();
		Map<String, String[]> paramMap = req.getParameterMap();
		for(Iterator<Entry<String, String[]>> it = paramMap.entrySet().iterator();it.hasNext();){
			Entry<String, String[]> entry = it.next();
			if(entry.getKey()!=null && entry.getValue()!=null){
				String[] value = entry.getValue();
				if(value.length == 1){
					attributes.put(entry.getKey(), entry.getValue()[0]);
				}else if(value.length > 1){
					attributes.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		
	}

}
