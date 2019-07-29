package com.sunys.facade.bean;

/**
 * 分页参数
 * PageParam
 * @author sunys
 * @date 2019年1月8日
 */
public class PageParam {

	private int pageIndex;
	private int pageSize;

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
