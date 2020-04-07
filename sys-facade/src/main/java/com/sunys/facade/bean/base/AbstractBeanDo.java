package com.sunys.facade.bean.base;

import java.util.Date;

/**
 * AbstractBeanDo
 * @author sunys
 * @date 2019年1月4日
 */
public abstract class AbstractBeanDo implements BaseDo {

	private static final long serialVersionUID = -7361003408776086219L;

	private Long pkid;
	private Date createOn;
	private String createBy;
	private Date updateOn;
	private String updateBy;

	@Override
	public Long getPkid() {
		return pkid;
	}

	@Override
	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	@Override
	public Date getCreateOn() {
		return createOn;
	}

	@Override
	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	@Override
	public String getCreateBy() {
		return createBy;
	}

	@Override
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Override
	public Date getUpdateOn() {
		return updateOn;
	}

	@Override
	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	@Override
	public String getUpdateBy() {
		return updateBy;
	}

	@Override
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pkid == null) ? 0 : pkid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractBeanDo other = (AbstractBeanDo) obj;
		if (pkid == null) {
			if (other.pkid != null)
				return false;
		} else if (!pkid.equals(other.pkid))
			return false;
		return true;
	}

}
