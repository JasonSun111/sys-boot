package com.sunys.facade.run.http;

public class ResponseEntity<T> extends HttpEntity<T> {

	private int statusCode;

	public ResponseEntity(HttpHeaders httpHeaders) {
		super(httpHeaders);
	}

	public ResponseEntity(HttpHeaders httpHeaders, T body) {
		super(httpHeaders, body);
	}
	
	public ResponseEntity(HttpHeaders httpHeaders, T body, int statusCode) {
		super(httpHeaders, body);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
