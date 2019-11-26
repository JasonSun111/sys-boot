package com.sunys.facade.http;

import java.util.List;
import java.util.Map;

public interface Http {

	<T> T request(HttpMethod method, String url, Map<String, List<String>> headers, Object body);
	
	default <T> T get(String url, Map<String, List<String>> headers, Object body) {
		T t = request(HttpMethod.GET, url, headers, body);
		return t;
	}
	
	default <T> T post(String url, Map<String, List<String>> headers, Object body) {
		T t = request(HttpMethod.POST, url, headers, body);
		return t;
	}
	
	default <T> T put(String url, Map<String, List<String>> headers, Object body) {
		T t = request(HttpMethod.PUT, url, headers, body);
		return t;
	}
	
	default <T> T delete(String url, Map<String, List<String>> headers, Object body) {
		T t = request(HttpMethod.DELETE, url, headers, body);
		return t;
	}
}
