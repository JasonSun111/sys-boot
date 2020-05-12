package com.sunys.facade.run;

import java.io.File;
import java.io.Writer;
import java.util.Map;

/**
 * 使用模板转换为String
 * StringConverter
 * @author sunys
 * @date Apr 12, 2020
 */
public interface StringConverter {

	String convert(Object... args);
	
	String convert(Object data);
	
	String convert(Map<String, Object> data);
	
	void convert(String path, Object... args);
	
	void convert(String path, Object data);
	
	void convert(String path, Map<String, Object> data);
	
	void convert(File file, Object... args);
	
	void convert(File file, Object data);
	
	void convert(File file, Map<String, Object> data);
	
	void convert(Writer writer, Object... args);
	
	void convert(Writer writer, Object data);
	
	void convert(Writer writer, Map<String, Object> data);
	
}
