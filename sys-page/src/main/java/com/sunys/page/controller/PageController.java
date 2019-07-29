package com.sunys.page.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sunys.core.context.FunctionProvider;
import com.sunys.facade.bean.CommonConst;
import com.sunys.facade.bean.DapFunction;

/**
 * PageController
 * @author sunys
 * @date 2019年1月18日
 */
@Controller
public class PageController {

	@Autowired
	private FunctionProvider functionProvider;
	
	private void setModel(Model model) {
		List<? extends DapFunction> functions = functionProvider.getFunctions();
		model.addAttribute("functions", functions);
	}
	
	@RequestMapping(value=CommonConst.COMMON_PAGE_PATH + "/{pageName}",method={RequestMethod.GET,RequestMethod.POST})
	public String pageLevel0(@PathVariable("pageName") String pageName, Model model) {
		String path = "page/" + pageName;
		setModel(model);
		return path;
	}
	
	@RequestMapping(value=CommonConst.COMMON_PAGE_PATH + "/{folder1}/{pageName}",method={RequestMethod.GET,RequestMethod.POST})
	public String pageLevel1(@PathVariable("folder1") String folder1, @PathVariable("pageName") String pageName, Model model) {
		String path = "page/" + folder1 + "/" + pageName;
		setModel(model);
		return path;
	}
	
	@RequestMapping(value=CommonConst.COMMON_PAGE_PATH + "/{folder1}/{folder2}/{pageName}",method={RequestMethod.GET,RequestMethod.POST})
	public String pageLevel2(@PathVariable("folder1") String folder1, @PathVariable("folder1") String folder2, @PathVariable("pageName") String pageName, Model model) {
		String path = "page/" + folder1 + "/" + folder2 + "/" + pageName;
		setModel(model);
		return path;
	}
	
	@RequestMapping(CommonConst.LOGIN_PATH)
	public String login() {
		return "login";
	}
}
