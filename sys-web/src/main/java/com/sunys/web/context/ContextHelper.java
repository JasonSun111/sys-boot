package com.sunys.web.context;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import com.sunys.core.context.ContextProvider;
import com.sunys.core.context.SpringContextHelper;
import com.sunys.facade.bean.DapContext;

/**
 * 获取当前用户和ApplicationContext
 * ContextHelper
 * @author sunys
 * @date 2019年1月7日
 */
public class ContextHelper {

	private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<>();

	private ContextHelper() {
	}

	public static void setRequest(HttpServletRequest request){
		requestLocal.set(request);
	}

	public static HttpServletRequest getRequest(){
		return requestLocal.get();
	}

	public static void removeRequest(){
		requestLocal.remove();
	}

	/**
	 * 根据当前请求获取Context
	 * @return
	 */
	public static DapContext getContext() {
		HttpServletRequest request = requestLocal.get();
		if (request==null) {
			return null;
		}
		DapContext context = (DapContext) request.getAttribute("context");
		if (context==null) {
			ContextProvider contextProvider = SpringContextHelper.getApplicationContext().getBean("contextProvider", ContextProvider.class);
			context = contextProvider.getContext();
			request.setAttribute("context", context);
		}
		return context;
	}

	/**
	 * 获取ApplicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return SpringContextHelper.getApplicationContext();
	}

}
