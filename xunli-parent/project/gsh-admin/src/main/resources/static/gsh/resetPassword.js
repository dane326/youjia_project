
$(function() {
    //初始化验证手机
    $.validator.addMethod("isPhone", function(value, element) {
        return this.optional( element ) || /^1\d{10}$/.test( value );
    }, "请填写正确的手机号码");//可以自定义默认提示信息
    validateRule();
    $('#yzm').click(function() {
        var yzmflag = $("#signupForm").validate().element($("#phonenumber"));
        if(!yzmflag){
        return false;
        }
        var phonenumber = $.common.trim($("input[name='phonenumber']").val());
        //这个会有个弹窗

        $.operate.submitV2(ctx + "getValidateCode?phonenumber="+phonenumber+"&codeType=reset", "get", "json", "",
            function(r) {
                if (r.code == 0) {
                    $("#yzm").attr("disabled",true); //防止多次点击
                    timeCount($('#yzm'),60);
                    $.modal.msgSuccess("发送成功！");
                } else {
                    $(".code").val("");
                    $.modal.msg(r.msg);
                }
            });

    });
});

//计时函数
function timeCount(_this,timeleft){
    timeleft-=1
    if (timeleft>0){
        _this.text(timeleft+" 秒后重发");
        setTimeout(function(){
            timeCount(_this,timeleft)
        },1000)
    }
    else {
        _this.text("重新发送");
        _this.removeAttr("disabled");
    }
}


$.validator.setDefaults({
    submitHandler: function() {
        resetPassword();
    }
});

function resetPassword() {
	$.modal.loading($("#btnSubmit").data("loading"));
	var phonenumber = $.common.trim($("input[name='phonenumber']").val());
    var password = $.common.trim($("input[name='password']").val());
    var validateCode = $("input[name='validateCode']").val();
    $.ajax({
        type: "post",
        url:  ctx + "system/factory/info/resetPassword",
        data: {
            "phonenumber": phonenumber,
            "password": password,
            "loginName":phonenumber,
            "userName":phonenumber,
            "validateCode" : validateCode,
        },
        success: function(r) {
            if (r.code == 0) {
                $.modal.closeLoading();
                $.modal.msg(r.msg);
                //跳转登录
                location.href = ctx + 'login';
            } else {
                $.modal.closeLoading();
                $(".code").val("");
                $.modal.msg(r.msg);
            }

        }
    });
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            phonenumber: {
                required: true,
                isPhone: true
            },
            password: {
                required: true,
                minlength: 6
            },
            password_confirm: {
                required: true,
                minlength: 6,
                equalTo: "#password"
            },
            validateCode: {
                required: true
            }
        },
        messages: {
            phonenumber: {
                isPhone: icon + "请输入正确手机号",
                required: icon +"请输入手机号",
            },
            password: {
                required: icon + "请输入您的密码",
                minlength: jQuery.validator.format(icon +"最少输入{0}字符")
            },
            password_confirm: {
                required: icon +"请输入确认密码",
                minlength: jQuery.validator.format(icon +"最少输入{0}字符"),
                equalTo: icon +"请输入相同的密码"
            },
            validateCode: {
                required: icon + "请输入验证码",
            }
        },
        errorPlacement: function(error, element) {
            if($(element).attr("name") == "validateCode"){
                $(".yzm").after(error);
            }else{
                $(element).after(error);
            }
        }
    })
}


function getParam(paramName) {
    var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}