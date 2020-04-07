package com.sunys.facade.bean.vo;

import com.sunys.facade.bean.base.BaseDo;
import com.sunys.facade.bean.base.TreeDo;

/**
 * AbstractTreeBeanVo
 * @author sunys
 * @date 2019年1月4日
 */
public abstract class AbstractTreeBeanVo extends AbstractBeanVo implements TreeVo {

	private static final long serialVersionUID = 9036192868970712989L;

	protected abstract TreeDo getTreeDo();

	@Override
	protected BaseDo getBaseDo() {
		return getTreeDo();
	}

	@Override
	public String getCodeId() {
		return getTreeDo().getCodeId();
	}

	@Override
	public void setCodeId(String codeId) {
		getTreeDo().setCodeId(codeId);
	}

	@Override
	public String getCodeName() {
		return getTreeDo().getCodeName();
	}

	@Override
	public void setCodeName(String codeName) {
		getTreeDo().setCodeName(codeName);
	}

	@Override
	public String getParentCode() {
		return getTreeDo().getParentCode();
	}

	@Override
	public void setParentCode(String parentCode) {
		getTreeDo().setParentCode(parentCode);
	}

}
