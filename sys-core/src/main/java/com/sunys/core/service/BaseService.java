package com.sunys.core.service;

import java.util.List;
import java.util.Map;

import com.sunys.facade.bean.AbstractBean;
import com.sunys.facade.bean.PageBean;
import com.sunys.facade.bean.PageParam;

/**
 * BaseService
 * @author sunys
 * @date 2019年1月4日
 */
public interface BaseService<T extends AbstractBean> {

	/**
	 * 根据id查询数据
	 * @param pkid
	 * @return
	 * @throws Exception
	 */
	T getById(Long pkid) throws Exception;

	/**
	 * 分页查询
	 * @param params
	 * @param pageParam
	 * @return
	 * @throws Exception
	 */
	PageBean<T> listPage(Map<String, Object> params, PageParam pageParam) throws Exception;

	/**
	 * 根据条件查询出符合条件的一条记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	T getOne(Map<String, Object> params) throws Exception;
	
	/**
	 * 根据条件查询出符合条件的所有记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<T> listBy(Map<String, Object> params) throws Exception;

	/**
	 * 插入一条记录
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	int insert(T entity) throws Exception;

	/**
	 * 插入列表的所有记录
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int insertAll(List<T> list) throws Exception;

	/**
	 * 插入列表的所有记录
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int[] insertList(List<T> list) throws Exception;

	/**
	 * 根据id更新所有字段
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	int update(T entity) throws Exception;

	/**
	 * 根据id更新列表中的所有记录的所有字段
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int[] updateList(List<T> list) throws Exception;

	/**
	 * 根据id更新参数中不为空的字段
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	int updateIf(T entity) throws Exception;

	/**
	 * 根据id更新列表中的所有记录的不为空的字段
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int[] updateIfList(List<T> list) throws Exception;

	/**
	 * 根据id删除
	 * @param pkid
	 * @return
	 * @throws Exception
	 */
	int deleteById(Long pkid) throws Exception;

	/**
	 * 根据条件删除符合条件的所有记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	int deleteBy(Map<String, Object> params) throws Exception;

	/**
	 * 根据条件查询符合条件的个数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	int countBy(Map<String, Object> params) throws Exception;
}
