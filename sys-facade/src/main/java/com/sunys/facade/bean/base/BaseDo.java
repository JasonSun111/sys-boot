package com.sunys.facade.bean.base;

import java.io.Serializable;
import java.util.Date;

public interface BaseDo extends Serializable {

	Long getPkid();

	void setPkid(Long pkid);

	Date getCreateOn();

	void setCreateOn(Date createOn);

	String getCreateBy();

	void setCreateBy(String createBy);

	Date getUpdateOn();

	void setUpdateOn(Date updateOn);

	String getUpdateBy();

	void setUpdateBy(String updateBy);

}
