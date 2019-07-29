package com.sunys.web.context;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.sunys.facade.bean.CommonConst;
import com.sunys.facade.bean.DapContext;
import com.sunys.facade.exception.CommonEIEnum;
import com.sunys.facade.exception.BusiException;

/**
 * 判断当前用户是否登陆
 * LoginInterceptor
 * @author sunys
 * @date 2019年1月7日
 */
public class LoginInterceptor implements HandlerInterceptor {

	private boolean checkLogin;

	public LoginInterceptor(boolean checkLogin) {
		this.checkLogin = checkLogin;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try{
			ContextHelper.setRequest(request);
			Optional<DapContext> contextOpt = Optional.ofNullable(ContextHelper.getContext());
			if (!contextOpt.map(DapContext::getPerson).isPresent()) {
				if (checkLogin) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}catch(BusiException e){
			if(CommonEIEnum.LOGIN_EXCEPTION.equals(e.getInfo())){
				response.sendRedirect(request.getContextPath() + CommonConst.LOGIN_PATH);
				return false;
			}
		}
		return false;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		ContextHelper.removeRequest();
	}
}
