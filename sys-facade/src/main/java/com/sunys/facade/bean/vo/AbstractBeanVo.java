package com.sunys.facade.bean.vo;

import java.util.Date;

import com.sunys.facade.bean.base.BaseDo;

/**
 * AbstractBeanVo
 * @author sunys
 * @date 2019年1月4日
 */
public abstract class AbstractBeanVo implements BaseVo {

	private static final long serialVersionUID = -7361003408776086219L;

	protected abstract BaseDo getBaseDo();

	@Override
	public Long getPkid() {
		return getBaseDo().getPkid();
	}

	@Override
	public void setPkid(Long pkid) {
		getBaseDo().setPkid(pkid);
	}

	@Override
	public Date getCreateOn() {
		return getBaseDo().getCreateOn();
	}

	@Override
	public void setCreateOn(Date createOn) {
		getBaseDo().setCreateOn(createOn);
	}

	@Override
	public String getCreateBy() {
		return getBaseDo().getCreateBy();
	}

	@Override
	public void setCreateBy(String createBy) {
		getBaseDo().setCreateBy(createBy);
	}

	@Override
	public Date getUpdateOn() {
		return getBaseDo().getUpdateOn();
	}

	@Override
	public void setUpdateOn(Date updateOn) {
		getBaseDo().setUpdateOn(updateOn);
	}

	@Override
	public String getUpdateBy() {
		return getBaseDo().getUpdateBy();
	}

	@Override
	public void setUpdateBy(String updateBy) {
		getBaseDo().setUpdateBy(updateBy);
	}
	
}
