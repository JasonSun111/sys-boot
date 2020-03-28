package com.sunys.facade.bean.base;

/**
 * AbstractTreeBeanDo
 * @author sunys
 * @date 2019年1月4日
 */
public abstract class AbstractTreeBeanDo extends AbstractBeanDo implements TreeDo {

	private static final long serialVersionUID = 9036192868970712989L;

	private String codeId;
	private String codeName;
	private String parentCode;

	@Override
	public String getCodeId() {
		return codeId;
	}

	@Override
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	@Override
	public String getCodeName() {
		return codeName;
	}

	@Override
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	@Override
	public String getParentCode() {
		return parentCode;
	}

	@Override
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codeId == null) ? 0 : codeId.hashCode());
		result = prime * result + ((parentCode == null) ? 0 : parentCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTreeBeanDo other = (AbstractTreeBeanDo) obj;
		if (codeId == null) {
			if (other.codeId != null)
				return false;
		} else if (!codeId.equals(other.codeId))
			return false;
		if (parentCode == null) {
			if (other.parentCode != null)
				return false;
		} else if (!parentCode.equals(other.parentCode))
			return false;
		return true;
	}

}
