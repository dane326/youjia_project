/*this is basic form validation using for validation person's basic information author:Clara Guo data:2017/07/20*/
jQuery.validator.setDefaults({
	errorPlacement: function(error, element) {
		if($(element).attr("data-accessories-min-size")){
			$(element).prev().addClass("has-error");
			$(element).prev().find("input[class='file-caption-name']").after(error.attr("style","right: 2px;"));
		}else{
			$(element).after(error);
		}
	}
});
$(document).ready(function(){
	//校验附件最少
	jQuery.validator.addMethod("data-accessories-min-size",function(value, element, param){
		var values = value.split(",");
		return values.length >= param;
	},"最少上传{0}个附件");
	//校验附件最大
	jQuery.validator.addMethod("data-accessories-max-size",function(value, element, param){
		var values = value ? value.split(",").length : 0;
		console.log(values)
		return values <= param;
	},"最多上传{0}个附件");
});