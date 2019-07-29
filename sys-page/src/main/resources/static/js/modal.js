/*
modal.show({
	id:"modal-editperson",
	title:"添加人员",
	closeText:"关闭",
	okText:"确定",
	showClose:true,
	showOk:true,
	width:900,
	bodyHeight:600,
	bodyHtml:function(body,def){
		body.innerHTML="";
		def.resolve(true);
	},
	onCreate:function(){},
	onShow:function(){},
	onOk:function(){},
	onClose:function(){}
});
 */
define("modal",function(require, exports, module) {
	var base=require("base");
	
	var modal={};
	
	modal.show = function(option){
		this.option = option;
		option.id = option.id ? option.id : "templatemodal";
		
		function initEvent(isCreate){
			var $modal = $("#"+option.id);
			if(!isCreate){
				$modal.find(".modal-title").html($.i18n.prop(option.title));
			}
			if(isCreate && option.onCreate){
				option.onCreate();
			}
			if(option.onOk){
				$modal.find(".btn-ok").off("click");
				$modal.find(".btn-ok").click(function(){
					if(option.onOk()){
						$modal.modal("hide");
					}
				});
			}
			if(option.onClose){
				$modal.find(".btn-close").off("click");
				$modal.find(".btn-close").click(option.onClose);
			}
		}
		
		base.templateArt({
			id:"tpl-modal",
			name:"modal",
			data:option
		}).then(function(html){
			var def = $.Deferred();
			var id = option.id;
			if($("#"+id).length==0){
				$("#"+id).remove();
				$("body").append(html);
				var $modal = $("#"+id);
				if(typeof option.bodyHtml=="function"){
					option.bodyHtml($modal.find(".modal-body")[0],def);
				}else{
					$modal.find(".modal-body").html(option.bodyHtml);
					def.resolve(true);
				}
			}else{
				def.resolve(false);
			}
			return def.promise();
		}).then(function(isCreate){
			initEvent(isCreate);
			if(option.onShow){
				option.onShow();
			}
			$("#"+option.id).modal("show");
		});
	}
	
	module.exports=modal;
});
