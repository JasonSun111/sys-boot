package com.sunys.facade.run.http;

/**
 * HttpBuildParam
 * @author sunys
 * @date Feb 10, 2020
 */
public class HttpBuildParam {

	private HttpRun httpRun;
	private Class<? extends HttpRun> clazz;

	private HttpBaseParam httpBaseParam;

	public HttpRun getHttpRun() {
		return httpRun;
	}

	public void setHttpRun(HttpRun httpRun) {
		this.httpRun = httpRun;
	}

	public Class<? extends HttpRun> getClazz() {
		return clazz;
	}

	public void setClazz(Class<? extends HttpRun> clazz) {
		this.clazz = clazz;
	}

	public HttpBaseParam getHttpBaseParam() {
		return httpBaseParam;
	}

	public void setHttpBaseParam(HttpBaseParam httpBaseParam) {
		this.httpBaseParam = httpBaseParam;
	}

}
