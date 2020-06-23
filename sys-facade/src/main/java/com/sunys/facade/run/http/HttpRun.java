package com.sunys.facade.run.http;

import java.lang.reflect.Type;

public interface HttpRun {

	<T> ResponseEntity<T> request(HttpMethod httpMethod, String url, HttpEntity<?> httpEntity, Type type);

	default <T> T requestObject(HttpMethod httpMethod, String url, HttpEntity<?> httpEntity, Type type) {
		ResponseEntity<T> responseEntity = request(httpMethod, url, httpEntity, type);
		return responseEntity.getBody();
	}

	default <T> ResponseEntity<T> get(String url, HttpEntity<?> httpEntity, Type type) {
		ResponseEntity<T> entity = request(HttpMethod.GET, url, httpEntity, type);
		return entity;
	}

	default <T> T getObject(String url, HttpEntity<?> httpEntity, Type type) {
		ResponseEntity<T> responseEntity = get(url, httpEntity, type);
		return responseEntity.getBody();
	}

	default <T> ResponseEntity<T> post(String url, HttpEntity<?> httpEntity, Type type) {
		ResponseEntity<T> entity = request(HttpMethod.POST, url, httpEntity, type);
		return entity;
	}

	default <T> T postObject(String url, HttpEntity<?> httpEntity, Type type) {
		ResponseEntity<T> responseEntity = post(url, httpEntity, type);
		return responseEntity.getBody();
	}

	default <T> ResponseEntity<T> put(String url, HttpEntity<?> httpEntity, Type type) {
		ResponseEntity<T> entity = request(HttpMethod.PUT, url, httpEntity, type);
		return entity;
	}

	default <T> T putObject(String url, HttpEntity<?> httpEntity, Type type) {
		ResponseEntity<T> responseEntity = put(url, httpEntity, type);
		return responseEntity.getBody();
	}

	default <T> ResponseEntity<T> delete(String url, HttpEntity<?> httpEntity, Type type) {
		ResponseEntity<T> entity = request(HttpMethod.DELETE, url, httpEntity, type);
		return entity;
	}

	default <T> T deleteObject(String url, HttpEntity<?> httpEntity, Type type) {
		ResponseEntity<T> responseEntity = delete(url, httpEntity, type);
		return responseEntity.getBody();
	}

}
