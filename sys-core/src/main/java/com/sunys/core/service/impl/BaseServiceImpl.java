package com.sunys.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.sunys.core.context.ContextProvider;
import com.sunys.core.dao.BaseDao;
import com.sunys.core.service.BaseService;
import com.sunys.core.util.PageUtils;
import com.sunys.facade.bean.AbstractBean;
import com.sunys.facade.bean.DapContext;
import com.sunys.facade.bean.PageBean;
import com.sunys.facade.bean.PageParam;

/**
 * BaseServiceImpl
 * @author sunys
 * @date 2019年1月4日
 */
public abstract class BaseServiceImpl<T extends AbstractBean> implements BaseService<T> {

	@Autowired
	private ContextProvider contextProvider;
	
	/**
	 * 获取要操作的Dao
	 * @return
	 */
	public abstract BaseDao<T> getDao();

	/**
	 * 获取当前用户的用户名
	 * @return
	 */
	protected String currentUsername() {
		String username = Optional.ofNullable(contextProvider)
			.map(ContextProvider::getContext).map(DapContext::getUsername).orElse(null);
		return username;
	}

	@Override
	public T getById(Long pkid) throws Exception {
		T t = getDao().getById(pkid);
		return t;
	}

	@Override
	public PageBean<T> listPage(Map<String, Object> params,PageParam pageParam) throws Exception {
		if(params==null){
			params = new HashMap<>();
		}
		params.put("pageParam", pageParam);
		List<T> list = getDao().pageList(params);
		int totalCount = getDao().pageCount(params);
		PageBean<T> pageBean = PageUtils.getPageBean(list, totalCount, pageParam);
		return pageBean;
	}

	@Override
	public T getOne(Map<String, Object> params) throws Exception {
		List<T> list = getDao().listBy(params);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<T> listBy(Map<String, Object> params) throws Exception {
		List<T> list = getDao().listBy(params);
		return list;
	}

	@Override
	public int insert(T entity) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		entity.setCreateBy(username);
		entity.setCreateOn(date);
		int count = getDao().insert(entity);
		return count;
	}

	@Override
	public int insertAll(List<T> list) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		for (T entity : list) {
			entity.setCreateBy(username);
			entity.setCreateOn(date);
		}
		int count = getDao().insertAll(list);
		return count;
	}

	@Override
	public int[] insertList(List<T> list) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		int[] arr = new int[list.size()];
		for(int i=0; i<list.size(); i++){
			T entity = list.get(i);
			entity.setCreateBy(username);
			entity.setCreateOn(date);
			arr[i] = getDao().insert(entity);
		}
		return arr;
	}

	@Override
	public int update(T entity) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		entity.setUpdateBy(username);
		entity.setUpdateOn(date);
		int count = getDao().update(entity);
		return count;
	}

	@Override
	public int[] updateList(List<T> list) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		int[] arr = new int[list.size()];
		for(int i=0; i<list.size(); i++){
			T entity = list.get(i);
			entity.setUpdateBy(username);
			entity.setUpdateOn(date);
			arr[i] = getDao().update(entity);
		}
		return arr;
	}

	@Override
	public int updateIf(T entity) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		entity.setUpdateBy(username);
		entity.setUpdateOn(date);
		int count = getDao().updateIf(entity);
		return count;
	}

	@Override
	public int[] updateIfList(List<T> list) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		int[] arr = new int[list.size()];
		for(int i=0; i<list.size(); i++){
			T entity = list.get(i);
			entity.setUpdateBy(username);
			entity.setUpdateOn(date);
			arr[i] = getDao().updateIf(entity);
		}
		return arr;
	}

	@Override
	public int deleteById(Long pkid) throws Exception {
		int count = getDao().deleteById(pkid);
		return count;
	}

	@Override
	public int deleteBy(Map<String, Object> params) throws Exception {
		int count = getDao().deleteBy(params);
		return count;
	}

	@Override
	public int countBy(Map<String, Object> params) throws Exception {
		int count = getDao().countBy(params);
		return count;
	}

}
