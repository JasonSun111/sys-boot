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
import com.sunys.facade.bean.DapContext;
import com.sunys.facade.bean.PageBean;
import com.sunys.facade.bean.PageParam;
import com.sunys.facade.bean.base.BaseDo;

/**
 * BaseServiceImpl
 * @author sunys
 * @date 2019年1月4日
 */
public abstract class BaseServiceImpl<Do extends BaseDo> implements BaseService<Do> {

	@Autowired
	private ContextProvider contextProvider;
	
	/**
	 * 获取要操作的Dao
	 * @return
	 */
	protected abstract BaseDao<Do> getDao();

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
	public Do getById(Long pkid) throws Exception {
		Do t = getDao().getById(pkid);
		return t;
	}

	@Override
	public PageBean<Do> listPage(Map<String, Object> params,PageParam pageParam) throws Exception {
		if(params==null){
			params = new HashMap<>();
		}
		params.put("pageParam", pageParam);
		List<Do> list = getDao().pageList(params);
		int totalCount = getDao().pageCount(params);
		PageBean<Do> pageBean = PageUtils.getPageBean(list, totalCount, pageParam);
		return pageBean;
	}

	@Override
	public Do getOne(Map<String, Object> params) throws Exception {
		List<Do> list = getDao().listBy(params);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Do> listBy(Map<String, Object> params) throws Exception {
		List<Do> list = getDao().listBy(params);
		return list;
	}

	@Override
	public int insert(Do entity) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		entity.setCreateBy(username);
		entity.setCreateOn(date);
		int count = getDao().insert(entity);
		return count;
	}

	@Override
	public int insertAll(List<Do> list) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		for (Do entity : list) {
			entity.setCreateBy(username);
			entity.setCreateOn(date);
		}
		int count = getDao().insertAll(list);
		return count;
	}

	@Override
	public int[] insertList(List<Do> list) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		int[] arr = new int[list.size()];
		for(int i=0; i<list.size(); i++){
			Do entity = list.get(i);
			entity.setCreateBy(username);
			entity.setCreateOn(date);
			arr[i] = getDao().insert(entity);
		}
		return arr;
	}

	@Override
	public int update(Do entity) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		entity.setUpdateBy(username);
		entity.setUpdateOn(date);
		int count = getDao().update(entity);
		return count;
	}

	@Override
	public int[] updateList(List<Do> list) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		int[] arr = new int[list.size()];
		for(int i=0; i<list.size(); i++){
			Do entity = list.get(i);
			entity.setUpdateBy(username);
			entity.setUpdateOn(date);
			arr[i] = getDao().update(entity);
		}
		return arr;
	}

	@Override
	public int updateIf(Do entity) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		entity.setUpdateBy(username);
		entity.setUpdateOn(date);
		int count = getDao().updateIf(entity);
		return count;
	}

	@Override
	public int[] updateIfList(List<Do> list) throws Exception {
		String username = currentUsername();
		Date date = new Date();
		int[] arr = new int[list.size()];
		for(int i=0; i<list.size(); i++){
			Do entity = list.get(i);
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
