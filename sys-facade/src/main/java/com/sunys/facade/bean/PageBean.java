package com.sunys.facade.bean;

import java.util.List;

import com.sunys.facade.bean.base.BaseDo;

/**
 * PageBean
 * @author sunys
 * @date 2019年1月4日
 */
public class PageBean<T extends BaseDo> {

	//总条数
	private int totalCount;
	//总页数
	private int pageCount;
	//第几页
	private int pageIndex;
	//一页几条数据
	private int pageSize;
	
	//第几页的数据
	private List<T> recordList;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

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

	public List<T> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<T> recordList) {
		this.recordList = recordList;
	}

}
