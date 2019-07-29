package com.sunys.core.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunys.facade.bean.CommonConst;
import com.sunys.facade.bean.ErrorData;
import com.sunys.facade.bean.ResultBean;
import com.sunys.facade.exception.BusiException;

/**
 * RemoteServiceInvoke
 * @author sunys
 * @date 2019年3月18日
 */
public class RemoteServiceInvoke {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RestTemplate restTemplate;

	public <T> ResultBean<T> callService(String path,String serviceName,String methodName,Object obj,ParameterizedTypeReference<ResultBean<T>> typeRef) throws IOException{
		MultiValueMap<String,Object> paramMap = new LinkedMultiValueMap<>();
		String jsonStr = objectMapper.writeValueAsString(obj);
		paramMap.add("json", jsonStr);
		ResultBean<T> resultBean = callService(path, serviceName, methodName, paramMap, null, typeRef);
		return resultBean;
	}

	public <T> ResultBean<T> callService(String path,String serviceName,String methodName, MultiValueMap<String,Object> paramMap, MultiValueMap<String,String> headlers, ParameterizedTypeReference<ResultBean<T>> typeRef) throws IOException{
		StringBuilder sb = new StringBuilder();
		String url = sb.append(path).append(CommonConst.COMMON_SERVICE_PATH).append("/json/").append(serviceName).append('/').append(methodName).append(".json").toString();
		ResultBean<T> resultBean = callService(url, paramMap, headlers, typeRef);
		return resultBean;
	}

	/**
	 * 请求其他服务器的url链接
	 * @param url
	 * @param paramMap
	 * @param headlers
	 * @param typeRef
	 * @return
	 * @throws IOException
	 */
	public <T> ResultBean<T> callService(String url, MultiValueMap<String,Object> paramMap, MultiValueMap<String,String> headlers, ParameterizedTypeReference<ResultBean<T>> typeRef) throws IOException{
		HttpEntity<MultiValueMap<String,Object>> httpEntity = new HttpEntity<>(paramMap, headlers);
		ResponseEntity<ResultBean<T>> entity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, typeRef);
		ResultBean<T> resultBean = entity.getBody();
		if(resultBean.getStatus()==ResultBean.ERROR_STATUS){
			ErrorData errorData = resultBean.getErrorData();
			throw new BusiException(errorData.getErrorInfo(),errorData.getData());
		}
		return resultBean;
	}

}
