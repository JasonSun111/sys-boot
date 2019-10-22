package com.sunys.facade.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * AbstractBean
 * @author sunys
 * @date 2019年1月4日
 */
public abstract class AbstractBean implements Serializable {

	private static final long serialVersionUID = -7361003408776086219L;

	private Long pkid;
	private Date createOn;
	private String createBy;
	private Date updateOn;
	private String updateBy;

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public Date getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateOn() {
		return updateOn;
	}

	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	public String getUpdateBy() {
		return updateBy;
	}

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
		AbstractBean other = (AbstractBean) obj;
		if (pkid == null) {
			if (other.pkid != null)
				return false;
		} else if (!pkid.equals(other.pkid))
			return false;
		return true;
	}

}
