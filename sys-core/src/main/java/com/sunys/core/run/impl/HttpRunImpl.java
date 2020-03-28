package com.sunys.core.run.impl;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sunys.facade.run.GroupRun;
import com.sunys.facade.run.RootGroupRun;
import com.sunys.facade.run.Run;
import com.sunys.facade.run.TimeoutCheck;
import com.sunys.facade.run.http.HttpEntity;
import com.sunys.facade.run.http.HttpHeaders;
import com.sunys.facade.run.http.HttpMethod;
import com.sunys.facade.run.http.HttpRun;
import com.sunys.facade.run.http.ResponseEntity;

public class HttpRunImpl implements HttpRun {

	@Override
	public <T> ResponseEntity<T> request(HttpMethod httpMethod, String url, HttpEntity<?> httpEntity, Type type) {
		RestTemplate restTemplate = new RestTemplate();
		org.springframework.http.HttpMethod springHttpMethod = org.springframework.http.HttpMethod.valueOf(httpMethod.name());
		MultiValueMap<String, String> mulitValueMap = new LinkedMultiValueMap<>();
		HttpHeaders requestHttpHeaders = httpEntity.getHttpHeaders();
		for (Iterator<Map.Entry<String, List<String>>> it = requestHttpHeaders.iterator(); it.hasNext();) {
			Map.Entry<String, List<String>> entry = it.next();
			String key = entry.getKey();
			List<String> value = entry.getValue();
			mulitValueMap.put(key, value);
		}
		org.springframework.http.HttpEntity<?> springHttpEntity = new org.springframework.http.HttpEntity<>(httpEntity.getBody(), mulitValueMap);
		ParameterizedTypeReference<T> typeRef = ParameterizedTypeReference.forType(type);
		org.springframework.http.ResponseEntity<T> springResponseEntity = restTemplate.exchange(url, springHttpMethod, springHttpEntity, typeRef);
		HttpHeaders responseHeaders = new HttpHeaders();
		org.springframework.http.HttpHeaders springResponseHeaders = springResponseEntity.getHeaders();
		for (Map.Entry<String, List<String>> entry : springResponseHeaders.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			responseHeaders.setList(key, value);
		}
		ResponseEntity<T> responseEntity = new ResponseEntity<T>(null, springResponseEntity.getBody(), springResponseEntity.getStatusCodeValue());
		return responseEntity;
	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Run getProxy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProxy(Run proxy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T parents(Class<? extends Run> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GroupRun<? extends Run> getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RootGroupRun<? extends Run> getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeoutCheck getTimeoutCheck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
