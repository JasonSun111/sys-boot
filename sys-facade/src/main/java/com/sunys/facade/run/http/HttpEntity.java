package com.sunys.facade.run.http;

public class HttpEntity<T> {

	private HttpHeaders httpHeaders;
	private T body;

	public HttpEntity(HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public HttpEntity(HttpHeaders httpHeaders, T body) {
		this.httpHeaders = httpHeaders;
		this.body = body;
	}

	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}

	public T getBody() {
		return body;
	}

}
