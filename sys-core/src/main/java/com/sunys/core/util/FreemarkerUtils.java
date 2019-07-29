/*
 * @author sunys
 * @date 2019年1月31日
 */
package com.sunys.core.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.sunys.core.context.SpringContextHelper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * FreemarkerUtils
 * @author sunys
 * @date 2019年1月31日
 */
public class FreemarkerUtils {

	private static Configuration configuration = SpringContextHelper.getApplicationContext().getBean(Configuration.class);

	private FreemarkerUtils() {
	}

	/**
	 * 根据名称获取模板
	 * @param templateName
	 * @return
	 * @throws IOException
	 */
	public static Template getTemplate(String templateName) throws IOException  {
		Template template = configuration.getTemplate(templateName);
		return template;
	}
	
	/**
	 * 使用模板渲染数据，返回渲染后的字符串
	 * @param templateName
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static String convertToString(String templateName,Object model) throws IOException, TemplateException {
		Template template = getTemplate(templateName);
		String result = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		return result;
	}
	
	/**
	 * 使用模板渲染数据到指定的文件中
	 * @param templateName
	 * @param model
	 * @param path
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void convertToFile(String templateName,Object model,String path) throws IOException, TemplateException {
		File file = new File(path);
		convertToFile(templateName, model, file);
	}
	
	/**
	 * 使用模板渲染数据到指定的文件中
	 * @param templateName
	 * @param model
	 * @param file
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void convertToFile(String templateName,Object model,File file) throws IOException, TemplateException {
		File dir = file.getParentFile();
		if(!(dir.exists() && dir.isDirectory())){
			dir.mkdirs();
		}
		String result = convertToString(templateName, model);
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file));){
			bw.write(result);
			bw.flush();
		}
	}
	
	/**
	 * 使用模板渲染数据到指定的文件中
	 * @param templateName
	 * @param model
	 * @param out
	 * @throws IOException
	 * @throws TemplateException
	 */
	public static void convertToFile(String templateName,Object model,Writer out) throws IOException, TemplateException {
		if(out!=null){
			try{
				Template template = getTemplate(templateName);
				template.process(model, out);
			}finally{
				out.close();
			}
		}
	}
	
	/**
	 * 根据一段字符串生成一个模板
	 * @param name
	 * @param sourceCode
	 * @return
	 * @throws IOException
	 */
	public static Template compileTemplate(String name, String sourceCode) throws IOException {
		Template template = new Template(name, sourceCode, configuration);
		return template;
	}
}
