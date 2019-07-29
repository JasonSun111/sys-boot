package com.sunys.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunys.core.util.ServiceInvoke;
import com.sunys.facade.bean.CommonConst;
import com.sunys.facade.bean.ResultBean;

/**
 * ServiceController
 * @author sunys
 * @date 2019年1月4日
 */
@Controller
@RequestMapping(CommonConst.COMMON_SERVICE_PATH)
public class ServiceController {

	@Autowired
	private ServiceInvoke serviceInvoke;

	@ResponseBody
	@RequestMapping(value="/param/{serviceName}/{methodName}",method={RequestMethod.GET,RequestMethod.POST})
	public ResultBean<Object> paramService(@PathVariable("serviceName") String serviceName,@PathVariable("methodName") String methodName,@RequestParam Map<String,String> paramMap) throws Exception {
		Object result = serviceInvoke.doService(serviceName, methodName,paramMap,true);
		ResultBean<Object> resultBean = ResultBean.successResult(result);
		return resultBean;
	}

	@ResponseBody
	@RequestMapping(value="/json/{serviceName}/{methodName}",method={RequestMethod.GET,RequestMethod.POST})
	public ResultBean<Object> jsonService(@PathVariable("serviceName") String serviceName,@PathVariable("methodName") String methodName,String json) throws Exception {
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("json", json);
		Object result = serviceInvoke.doService(serviceName, methodName,paramMap,false);
		ResultBean<Object> resultBean = ResultBean.successResult(result);
		return resultBean;
	}

}
