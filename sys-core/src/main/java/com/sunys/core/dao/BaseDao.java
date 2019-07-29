package com.sunys.core.dao;

import java.util.List;
import java.util.Map;

import com.sunys.facade.bean.AbstractBean;

/**
 * BaseDao
 * @author sunys
 * @date 2019年1月4日
 */
public interface BaseDao<T extends AbstractBean> {

	/**
	 * 根据id查询数据
	 * @param pkid
	 * @return
	 * @throws Exception
	 */
	T getById(Long pkid) throws Exception;

	/**
	 * 根据一定的条件查询出列表
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<T> listBy(Map<String, Object> params) throws Exception;
	
	/**
	 * 根据条件查询出符合条件的个数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	int countBy(Map<String, Object> params) throws Exception;

	/**
	 * 根据条件查询出分页的列表
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<T> pageList(Map<String, Object> params) throws Exception;

	/**
	 * 根据条件查询出分页的总条数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	int pageCount(Map<String, Object> params) throws Exception;

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
	 * 根据id更新所有字段
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	int update(T entity) throws Exception;

	/**
	 * 根据id更新参数中不为空的字段
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	int updateIf(T entity) throws Exception;

	/**
	 * 根据id删除一条记录
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
}
