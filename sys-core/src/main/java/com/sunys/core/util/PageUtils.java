package com.sunys.core.util;

import java.util.List;

import com.sunys.facade.bean.AbstractBean;
import com.sunys.facade.bean.PageBean;
import com.sunys.facade.bean.PageParam;

/**
 * 分页工具类
 * PageUtils
 * @author sunys
 * @date 2019年1月8日
 */
public class PageUtils {

	/**
	 * 根据字符串生产分页参数
	 * <p>1.20=>第1页，每页20条数据</p>
	 * <p>1=>第1页，默认每页10条数据</p>
	 * @param paramStr
	 * @return
	 */
	public static PageParam getPageParam(String paramStr){
		PageParam pageParam = new PageParam();
		pageParam.setPageIndex(1);
		pageParam.setPageSize(10);
		try{
			String[] arr = paramStr.split("\\.");
			if(arr.length==2){
				int pageIndex = Integer.parseInt(arr[0]);
				int pageSize = Integer.parseInt(arr[1]);
				pageParam.setPageIndex(pageIndex);
				pageParam.setPageSize(pageSize);
			}else if(arr.length==1){
				int pageIndex = Integer.parseInt(arr[0]);
				pageParam.setPageIndex(pageIndex);
			}
		}catch(Exception e){
			Log.error(e);
		}
		return pageParam;
	}
	
	/**
	 * 获取分页参数
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static PageParam getPageParam(int pageIndex,int pageSize){
		PageParam pageParam = new PageParam();
		pageParam.setPageIndex(pageIndex);
		pageParam.setPageSize(pageSize);
		return pageParam;
	}
	
	/**
	 * 生成PageBean
	 * @param list
	 * @param totalCount
	 * @param pageParam
	 * @return
	 */
	public static <T extends AbstractBean> PageBean<T> getPageBean(List<T> list,int totalCount,PageParam pageParam){
		PageBean<T> pageBean = new PageBean<>();
		pageBean.setRecordList(list);
		pageBean.setTotalCount(totalCount);
		int num = totalCount % pageParam.getPageSize()>0 ? 1 : 0;
		int pageCount = totalCount / pageParam.getPageSize() + num;
		pageBean.setPageCount(pageCount);
		pageBean.setPageIndex(pageParam.getPageIndex());
		pageBean.setPageSize(pageParam.getPageSize());
		return pageBean;
	}
}
