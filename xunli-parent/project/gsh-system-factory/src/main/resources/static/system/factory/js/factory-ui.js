/**
 * factoryUI
 */

var factoryUI = {
	/**
	 * 小数位不够，用0补足位数
	 * <param name="number">要处理的数字</param>
	 * <param name="bitNum">生成的小数位数</param>
	 */
	changeDecimalPadZero: function(number, bitNum) {
        var f_x = parseFloat(number);
        if (isNaN(f_x)) {
            return 0;
        }
        var s_x = number.toString();
        var pos_decimal = s_x.indexOf('.');
        if (pos_decimal < 0) {
            pos_decimal = s_x.length;
            s_x += '.';
        }
        while (s_x.length <= pos_decimal + bitNum) {
            s_x += '0';
        }
        return s_x;
    },
	payPost: function(url, data, callback) {
        var config = {
            url: url,
            type: "post",
            dataType: "json",
            data: data,
            beforeSend: function () {
            	$(window.frameElement).closest("div.layui-layer-iframe").block({ message: '<div class="loaderbox"><div class="loading-activity"></div>正在处理中，请稍后...</div>' });
            },
            success: function(result) {
                if (typeof callback == "function") {
                    callback(result);
                } else {
                	$(window.frameElement).closest("div.layui-layer-iframe").unblock();
                }
            }
        };
        $.ajax(config);
    },
    showQRcode: function(result) {
    	if (result.code == web_status.SUCCESS) {
			$(window.frameElement).closest("div.layui-layer-iframe").block({ message: $.common.sprintf('<div class="loaderbox"><img src="%s" width="200px" height="200px"/></div>', ctx + result.data.codeUrl) });
			
			var myTimer = setInterval(function(){ 
				$.operate.onlyGet(ctx + "system/factory/order/pay/query/" + result.data.orderNo, function(result){
					if(result.data.payStatus && result.data.payStatus !== '0'){
						clearInterval(myTimer);
						$(window.frameElement).closest("div.layui-layer-iframe").unblock();
						$.modal.msgReload("支付成功,正在刷新数据请稍后……", modal_status.SUCCESS);
					}
				});
			}, 5000);
			
			setTimeout(function(){
				clearInterval(myTimer);
				$(window.frameElement).closest("div.layui-layer-iframe").unblock();
				$.modal.msgReload("正在刷新数据请稍后……", modal_status.SUCCESS);
            }, 180000);
        } else {
        	if (result.code == web_status.WARNING) {
                $.modal.alertWarning(result.msg)
            } else if (result.code == web_status.DENIED) {
                $.modal.msgDenied(result.msg)
            }  else {
                $.modal.alertError(result.msg);
            }
        }
    }
};










