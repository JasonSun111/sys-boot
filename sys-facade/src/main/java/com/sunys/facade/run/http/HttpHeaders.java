package com.sunys.facade.run.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpHeaders {

	private Map<String, List<String>> headers = new HashMap<>();
	
	public void add(String name, String value) {
		List<String> list = getList(name);
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(value);
	}
	
	public void addList(String name, List<String> value) {
		headers.put(name, value);
	}
	
	public void set(String name, String value) {
		List<String> list = new ArrayList<>();
		list.add(value);
		headers.put(name, list);
	}
	
	public void setList(String name, List<String> value) {
		headers.put(name, value);
	}
	
	public void remove(String name) {
		headers.remove(name);
	}
	
	public String getOne(String name) {
		List<String> list = getList(name);
		String value = null;
		if (list != null && list.size() > 0) {
			value = list.get(0);
		}
		return value;
	}
	
	public List<String> getList(String name) {
		return headers.get(name);
	}
	
	public Iterator<Map.Entry<String, List<String>>> iterator() {
		return headers.entrySet().iterator();
	}
	
	public Set<Map.Entry<String, List<String>>> entrySet() {
		return headers.entrySet();
	}
	
	public Set<String> keySet() {
		return headers.keySet();
	}
}
