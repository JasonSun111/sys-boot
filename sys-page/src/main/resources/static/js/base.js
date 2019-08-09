define("base",function(require, exports, module){
	require("template-web");
	require("cookie");
	require("i18n.properties");
	var toastr = require("toastr");
	require("vue");
	var alertInfo = require("alertInfo");
	
	var base = {};
	
	template.defaults.debug = true;
	
	base.contextPath = function(){
		var pathName = window.location.pathname;
		var index = pathName.substr(1).indexOf("/");
		var contextPath = pathName.substr(0,index+1);
		return contextPath;
	}
	
	/**
	 * toast提示，提示类型：success、info、warning、error
	 */
	base.toastr = function(info,title,type,timeout){
		toastr.options["positionClass"] = "toast-top-center";
		if(timeout){
			toastr.options["timeOut"] = timeout;
		}else{
			toastr.options["timeOut"] = 5000;
		}
		if (!type || !toastr[type]){
			type = "success";
		}
		toastr[type](info,title);
	}
	
	/**
	 * 清除toast提示
	 */
	base.clearToast = function(){
		toastr.clear();
	}
	
	base.params = function(){
		var params = {};
		if (window.location.search != ""){
			var p = window.location.search.substr(1);
			var ps = p.split("&");
			for(var i=0;i<ps.length;i++){
				var v=ps[i];
				var vs = v.split("=");
				if(vs.length==2){
					params[vs[0]] = decodeURI(vs[1]);
				}else{
					params[vs[0]] = "";
				}
			}
		}
		return params;
	}
	
	base.hashs = function(){
		if(window.location.hash==""){
			return {};
		}
		var p = window.location.hash.substr(1);
		var ps = p.split("&");
		var hashs = {};
		for(var i=0;i<ps.length;i++){
			var v = ps[i];
			var vs = v.split("=");
			if(vs.length==2){
				hashs[vs[0]] = decodeURI(vs[1]);
			}else{
				hashs[vs[0]] = "";
			}
		}
		return hashs;
	}
	
	//检查是否登陆，没有登陆跳转到登陆页面
	base.checkLogin = function(){
		var ck = Cookies.get("ck");
		if(ck == null){
			window.location.href = this.contextPath()+"/login";
			return;
		}
	}
	
	//保存ck
	base.saveCk = function(ck){
		Cookies.set("ck",ck);
	}
	
	//获取ck
	base.getCk = function(){
		var ck = Cookies.get("ck");
		return ck;
	}
	
	//删除ck
	base.removeCk = function(){
		Cookies.remove("ck");
	}
	
	//注销
	base.logout = function(){
		window.location.href = this.contextPath()+"/login";
	}
	
	/**
	 * 国际化模板过滤器
	 * 使用：
	 * 1、{{"i18nKey" | i18n}}
	 * 2、{{"i18nKey" | i18n arr}}
	 */
	template.defaults.imports.i18n = function(msgCode,arr){
		if(arr == null){
			arr = [msgCode];
		}else{
			arr.unshift(msgCode);
		}
		return $.i18n.prop.apply($.i18n,arr);
	}
	
	function prop(option){
		var def = $.Deferred();
		$.i18n.properties({
			name:option.name,
			path:base.contextPath() + "/i18n/" + (option.path ? option.path : ""),
			language:option.locale,
			mode:"map",//var,map,both三种方式
			callback:function(){
				def.resolve();
			}
		});
		return def.promise();
	}
	
	//根据系统语言加载国际化文件
	base.loadProp = function(option){
		if(option.locale){
			return prop(option);
		}else{
			if(this.context!=null && this.context.locale!=null){
				option.locale = this.context.locale;
				return prop(option);
			}else{
				return this.getContext().then(function(context){
					option.locale = context.locale;
					return prop(option);
				});
			}
		}
	}
	
	base.getContext = function(){
		var def = $.Deferred();
		if(this.context){
			def.resolve(this.context);
		}else{
			var ck = this.getCk();
			this.paramService("contextProvider","getContextByCk",{ck:ck}).then(function(data){
				base.context = data;
				def.resolve(data);
			});
		}
		return def.promise();
	}
	
	base.deepCopy = function(obj){
		var result = Array.isArray(obj) ? [] : {};
		for(key in obj){
			if(typeof obj[key] == "object"){
				result[key] = this.deepCopy(obj[key]);
			}else{
				result[key] = obj[key];
			}
		}
		return result;
	}
	
	base.iboxToolsEvent = function(option){
		var inspinia = require("inspinia");
		inspinia.iboxToolsEvent(option);
	}
	
	/**
	 * 初始化左侧菜单、上边的导航
	 */
	base.initMenu = function(option){
		var codeId = option.codeId;
		base.loadProp({name:"cmn"}).then(function(){
			var $func = $("#func-" + menuCode(codeId));
			$func.addClass("active").parents(".func").addClass("active");
			require("inspinia");
			return base.paramService("functionProvider","getFunction",{codeId:codeId});
		}).then(function(data){
			$("#nav-title").html($.i18n.prop(data.name));
			var $navLevel = $("#nav-level");
			renderNavLevel($navLevel,data);
		});
	}
	
	function renderNavLevel($navLevel,func){
		if(func.parent){
			renderNavLevel($navLevel,func.parent);
		}
		var $li = $("<li></li>").addClass("breadcrumb-item").append($("<span></span>").html($.i18n.prop(func.name)));
		$navLevel.append($li);
	}
	
	function menuCode(codeId){
		var $func = $("#func-"+codeId);
		if($func.length==0){
			return menuCode(codeId.substring(0,codeId.length-2));
		}
		return codeId;
	}
	
	/**
	 * json数据渲染到一个区域的input元素中
	 */
	base.jsonToForm = function(option){
		var $div = option.div;
		if($div==null){
			$div = $(option.selector);
		}
		$div.jsonToForm(option);
	}
	
	/**
	 * json数据渲染到一个区域的input元素中
	 * $(".selector").jsonToForm({data:{}});
	 */
	$.fn.jsonToForm = function(option){
		var exclude = option.exclude;
		var obj = option.data;
		if(obj==null){
			console.log("data is null");
			return;
		}
		var $div = this;
		var $input = this.find("input").not(exclude);
		$input.each(function(){
			switch(this.type){
			case "text":
			case "password":
			case "hidden":
				renderText.call($div,this,obj);
				break;
			case "radio":
				renderRadio.call($div,this,obj);
				break;
			case "checkbox":
				renderCheckbox.call($div,this,obj);
				break;
			}
		});
		this.find("select").not(exclude).find("option").each(function(){
			var select = this.parentNode;
			var names = select.name.split(/\./);
			var value = getObjValue(obj,names);
			if(select.multiple){
				var option = this;
				value.forEach(function(item,index){
					if(option.value == item){
						option.selected = true;
					}
				});
			}else{
				if(value == this.value){
					this.selected = true;
				}
			}
		});
		this.find("textarea").not(exclude).each(function(){
			renderText.call($div,this,obj);
		});
	}
	
	function getObjValue(obj,names){
		var name = names.shift();
		if(names.length==0){
			var value = obj[name];
			return value;
		}else{
			var value = obj[name];
			if(value){
				return getObjValue(obj[name],names);
			}
			return null;
		}
	}
	
	function renderText(input,obj){
		var names = input.name.split(/\./);
		var value = getObjValue(obj,names);
		if(value!=null){
			input.value = value;
		}else{
			input.value = "";
		}
	}
	
	function renderRadio(input,obj){
		var names = input.name.split(/\./);
		var value = getObjValue(obj,names);
		if(input.value == value){
			input.checked = true;
		}
	}
	
	function renderCheckbox(input,obj){
		var names = input.name.split(/\./);
		var arr = getObjValue(obj,names);
		if(arr instanceof Array){
			arr.forEach(function(item,index){
				if(input.value == item){
					input.checked = true;
				}
			});
		}else{
			var $checkbox = this.find(":checkbox[name='"+input.name+"']");
			if($checkbox.length == 1){
				if(arr==false || arr=="false"){
					input.checked = false;
				}else if(arr==true || arr=="true"){
					input.checked = true;
				}
			}
		}
	}
	
	/**
	 * 一个区域的input中的值转成json对象
	 */
	base.formToJson = function(option){
		var $div = option.div;
		if($div==null){
			$div = $(option.selector);
		}
		var obj = $div.formToJson(option);
		return obj;
	}
	
	/**
	 * 一个区域的input中的值转成json对象
	 * var obj = $(".selector").formToJson();
	 */
	$.fn.formToJson = function(option){
		if(!option){
			option = {};
		}
		var exclude = option.exclude;
		var $div = this;
		var $input = this.find("input").not(exclude);
		var obj = {};
		$input.each(function(){
			switch(this.type){
			case "text":
			case "password":
			case "hidden":
				setTextValue.call($div,this,obj);
				break;
			case "radio":
				setRadioValue.call($div,this,obj);
				break;
			case "checkbox":
				setCheckboxValue.call($div,this,obj);
				break;
			}
		});
		this.find("select").not(exclude).each(function(){
			var select = this;
			var names = select.name.split(/\./);
			if(this.multiple){
				var arr = [];
				$(this).find("option:selected").each(function(){
					arr.push(this.value);
				});
				setObjValue(obj,names,arr);
			}else{
				$(this).find("option:selected").each(function(){
					setObjValue(obj,names,this.value);
					return false;
				});
			}
		});
		this.find("textarea").not(exclude).each(function(){
			setTextValue.call($div,this,obj);
		});
		return obj;
	}
	
	function setObjValue(obj,names,value){
		var name = names.shift();
		if(names.length==0){
			obj[name] = value;
		}else{
			if(obj[name]==null){
				obj[name] = {};
			}
			setObjValue(obj[name],names,value);
		}
	}
	
	function setTextValue(input,obj){
		var names = input.name.split(/\./);
		setObjValue(obj,names,input.value);
	}
	
	function setRadioValue(input,obj){
		if(input.checked){
			var names = input.name.split(/\./);
			setObjValue(obj,names,input.value);
		}
	}
	
	function setCheckboxValue(input,obj){
		var names = input.name.split(/\./);
		var $checkbox = this.find(":checkbox[name='"+input.name+"']");
		if($checkbox.length == 1){
			if($checkbox[0].checked){
				var value = $checkbox[0].value;
				if(value==null || value==""){
					setObjValue(obj,names,"true");
				}else{
					setObjValue(obj,names,value);
				}
			}else{
				var value = $checkbox[0].value;
				if(value=="true"){
					setObjValue(obj,names,"false");
				}else{
					setObjValue(obj,names,null);
				}
			}
		}else{
			var arr = [];
			this.find(":checkbox[name='"+input.name+"']:checked").each(function(){
				arr.push(this.value);
			});
			setObjValue(obj,names,arr);
		}
	}
	
	/**
	 * 清空表单
	 */
	base.cleanForm = function(option){
		this.jsonToForm({
			selector:option.selector,
			exclude:option.exclude,
			data:{}
		});
	}
	
	/**
	 * 清空表单
	 * $(".selector").cleanForm();
	 */
	$.fn.cleanForm = function(option){
		if(!option){
			option = {};
		}
		this.jsonToForm({
			exclude:option.exclude,
			data:{}
		});
	}
	
	base.templateArt = function(option){
		var def = $.Deferred();
		if($("#"+option.id).length==0){
			var url = option.url ? option.url : this.contextPath()+"/art/"+option.name+".ftl"
			$.get(url,function(art){
				$("#"+option.id).remove();
				var $templateScript = $("<script></script>").attr({id:option.id,"type":"text/template"}).text(art);
				$("body").append($templateScript);
				var render = template.compile(art);
				var html = render(option.data);
				def.resolve(html);
			},"text");
		}else{
			var html = template(option.id,option.data);
			def.resolve(html);
		}
		return def.promise();
	}
	
	/**
	 * 请求参数的方式调用后台服务
	 */
	base.paramService = function(serviceName,methodName,param,method,async){
		return this.callService({
			serviceName:serviceName,
			methodName:methodName,
			param:param,
			method:method,
			async:asnyc
		});
	}
	
	/**
	 * json字符串的方式调用后台服务
	 */
	base.jsonService = function(serviceName,methodName,jsonParam,async){
		var param = JSON.stringify(jsonParam);
		return this.callService({
			serviceName:serviceName,
			methodName:methodName,
			method:"POST",
			async:async,
			requestType:"json",
			param:{json:param}
		});
	}
	
	base.callService = function(requestOption){
		if(!requestOption.serviceName){
			throw new Error("serviceName is null");
		}
		if(!requestOption.methodName){
			throw new Error("methodName is null");
		}
		if(!requestOption.method){
			requestOption.method = "GET";
		}
		requestOption.url = this.contextPath() + "/cmn/service/" + (requestOption.requestType == "json" ? "json/" : "param/")
			+ requestOption.serviceName + "/" + requestOption.methodName;
		return this.ajax(requestOption);
	}
	
	base.ajax = function(requestOption){
		if(requestOption.async == null){
			requestOption.async = true;
		}
		var def = $.Deferred();
		$.ajax({
			url:requestOption.url,
			data:requestOption.param,
			type:requestOption.method,
			async:requestOption.async,
			contentType:requestOption.contentType ? requestOption.contentType : "application/x-www-form-urlencoded",
			dataType:"json",
			success:function(resultBean){
				if(resultBean.status){
					def.resolve(resultBean.result);
				}else{
					var error = resultBean.errorData;
					console.log(error);
					if(error.code=="001"){
						location.href = base.contextPath() + "/login";
					}else{
						if(error.isToast){
							base.toastr(alertInfo.codes[error.code].toString(error.data),null,error.level);
						}
						def.reject(error);
					}
				}
			},
			error:function(jqXHR,textStatus,errorThrown){
				def.reject(textStatus);
				console.log("call service error");
			}
		});
		return def.promise();
	}
	
	/**
	 * 上传文件
	 */
	base.upload = function(requestOption){
		if(requestOption.form){
			requestOption.formData = new FormData(requestOption.form);
		}
		if(requestOption.async == null){
			requestOption.async = true;
		}
		var def = $.Deferred();
		$.ajax({
			url:requestOption.url,
			data:requestOption.formData,
			type:"POST",
			async:requestOption.async,
			processData : false,
			contentType : false,
			cache : false,
			dataType:"json",
			success:function(resultBean){
				if(resultBean.status){
					def.resolve(resultBean.result);
				}else{
					var error = resultBean.errorData;
					console.log(error);
					if(error.isToast){
						base.toastr(alertInfo.codes[error.code].toString(error.data),null,error.level);
					}
					def.reject(error);
				}
			},
			error:function(jqXHR,textStatus,errorThrown){
				def.reject(textStatus);
				console.log("call service error");
			}
		});
		return def.promise();
	}
	
	if(base.hashs().debug){
		window.base = base;
	}
	
	module.exports = base;
});
