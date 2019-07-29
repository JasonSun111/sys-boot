define("alertInfo",function(require, exports, module) {
	require("i18n.properties");
	
	var alertInfo = {};
	
	function Prop(key){
		this.key = key;
		this.toString = function(arr){
			if(arr==null){
				arr = [];
			}
			arr.unshift(this.key);
			return $.i18n.prop.apply($.i18n,arr);
		}
	}
	
	alertInfo.codes = {};
	
	alertInfo.load = function(codeMap){
		for(i in codeMap){
			if(alertInfo.codes[i]==null){
				alertInfo.codes[i] = new Prop(codeMap[i]);
			}else{
				console.log("i18n key is exists:" + i);
			}
		}
	}
	
	module.exports = alertInfo;
});
