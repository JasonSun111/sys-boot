package com.sunys.web.conf;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunys.core.util.Log;
import com.sunys.facade.bean.ResultBean;
import com.sunys.facade.exception.BusiException;

/**
 * HandlerException
 * @author sunys
 * @date 2019年1月22日
 */
public class HandlerException implements HandlerExceptionResolver {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		String acceptHeader = request.getHeader("Accept");
		ResultBean<?> resultBean = handle(ex);
		ModelAndView mv = new ModelAndView();
		if(acceptHeader!=null && acceptHeader.contains(MediaType.TEXT_HTML_VALUE)){
			mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			mv.addObject("exception", ex);
			mv.setViewName("page/500");
			return mv;
		}else{
			mv.setView(new MappingJackson2JsonView(objectMapper));
			mv.addObject("time", resultBean.getTime());
			mv.addObject("status",resultBean.getStatus());
			mv.addObject("errorData",resultBean.getErrorData());
			mv.addObject("result",resultBean.getResult());
			return mv;
		}
	}

	public static ResultBean<?> handle(Exception e) {
		ResultBean<?> resultBean = null;
		if (e instanceof BusiException) {
			resultBean = ResultBean.failResult(e);
		} else if (e instanceof InvocationTargetException) {
			InvocationTargetException ie = (InvocationTargetException) e;
			Throwable targetException = getTargetException(ie);
			resultBean = ResultBean.failResult(targetException);
			if(!(targetException instanceof BusiException)){
				Log.error(e);
			}
		} else {
			resultBean = ResultBean.failResult(e);
			Log.error(e);
		}
		return resultBean;
	}

	private static Throwable getTargetException(InvocationTargetException e){
		Throwable targetException = e.getTargetException();
		if(targetException instanceof InvocationTargetException){
			InvocationTargetException ie = (InvocationTargetException) targetException;
			return getTargetException(ie);
		}
		return targetException;
	}

}
