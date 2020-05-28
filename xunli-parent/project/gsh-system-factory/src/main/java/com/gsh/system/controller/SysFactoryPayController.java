package com.gsh.system.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayConstants;
import com.google.api.client.util.Maps;
import com.gsh.common.annotation.Log;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.spring.SpringUtils;
import com.gsh.product.constants.AlipayCallback;
import com.gsh.product.constants.PayMethodConstants;
import com.gsh.product.utils.ProductUtils;
import com.gsh.system.domain.SysFactoryInfo;
import com.gsh.system.domain.SysFactoryOrder;
import com.gsh.system.service.ISysFactoryInfoService;
import com.gsh.system.service.ISysFactoryOrderService;
import com.gsh.system.service.ISysFactoryPayService;

/**
 * 订单管理Controller
 * 
 * @author gsh
 * @date 2019-10-29
 */
@Controller
public class SysFactoryPayController extends BaseController {

	@Autowired
    private ISysFactoryInfoService sysFactoryInfoService;
	
	@Autowired
	private ISysFactoryOrderService sysFactoryOrderService;
	
	/**
     * 显示系统资料
     */
    @RequiresPermissions("system:factory:info:detail")
    @GetMapping("/order/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap) {
        SysFactoryInfo sysFactoryInfo = sysFactoryInfoService.selectSysFactoryInfoById(id);
        mmap.put("sysFactoryInfo", sysFactoryInfo);
        return "/system/factory/info/detail";
    }

	/**
	 * 更新订单状态 支付系统回调
	 */
	@PostMapping("/order/notify/{payMethod}")
	@Log(title = "订单管理", businessType = BusinessType.UPDATE)
	@ResponseBody
	public String payOver(@PathVariable("payMethod") String payMethod, HttpServletRequest request) {
		if(PayMethodConstants.ALIPAY.equals(payMethod)){
			ISysFactoryPayService sysFactoryPayService = (ISysFactoryPayService)SpringUtils.getBean("sysFactoryAlipayService");
			// 校验请求合法性
			Map<String, String> params = sysFactoryPayService.getRequestParams(request);
			if(sysFactoryPayService.checkSignWithParams(params)){
				if(AlipayCallback.TRADE_SUCCESS.equals(params.get("trade_status")) || AlipayCallback.TRADE_FINISHED.equals(params.get("trade_status"))){
					SysFactoryOrder sysFactoryOrder = new SysFactoryOrder();
					sysFactoryOrder.setOrderNo(params.get("out_trade_no"));
					List<SysFactoryOrder> factoryOrders = sysFactoryOrderService.selectSysFactoryOrderList(sysFactoryOrder);
					if (CollectionUtils.isNotEmpty(factoryOrders)) {
						sysFactoryOrder = factoryOrders.get(0);
						// 订单金额一致
						if(new BigDecimal(sysFactoryOrder.getMoney()).compareTo(new BigDecimal(params.get("total_amount"))) == 0){
							// 此处最好再次判断订单状态
							
							sysFactoryOrder.setPayStatus("1");
							sysFactoryOrder.setTradeNo(params.get("trade_no"));
							sysFactoryOrderService.updateSysFactoryOrderWithBusiness(sysFactoryOrder);
							return ProductUtils.generatePaySuccessReplyParams();
						}						
					}
				}
			}	
			return ProductUtils.generatePayErrorReplyParams();
		} else if(PayMethodConstants.WEIXIN.equals(payMethod)){
			//商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致
			ISysFactoryPayService sysFactoryPayService = (ISysFactoryPayService)SpringUtils.getBean("sysFactoryWeixinService");
			Map<String, String> params = sysFactoryPayService.getRequestParams(request);
			if(sysFactoryPayService.checkSignWithParams(params)){
				if(WXPayConstants.SUCCESS.equals(params.get("return_code")) && WXPayConstants.SUCCESS.equals(params.get("result_code"))){
					SysFactoryOrder sysFactoryOrder = new SysFactoryOrder();
					sysFactoryOrder.setOrderNo(params.get("out_trade_no"));
					List<SysFactoryOrder> factoryOrders = sysFactoryOrderService.selectSysFactoryOrderList(sysFactoryOrder);
					if (CollectionUtils.isNotEmpty(factoryOrders)) {
						sysFactoryOrder = factoryOrders.get(0);
						// 订单金额一致
						if(new BigDecimal(ProductUtils.changeY2F(sysFactoryOrder.getMoney())).compareTo(new BigDecimal(params.get("total_fee"))) == 0){
							// 此处最好再次判断订单状态
							
							sysFactoryOrder.setPayStatus("1");
							sysFactoryOrder.setTradeNo(params.get("transaction_id"));
							sysFactoryOrderService.updateSysFactoryOrderWithBusiness(sysFactoryOrder);
							return ProductUtils.generatePaySuccessReplyXML();
						}						
					}					
				}				
			}
			return ProductUtils.generatePayErrorReplyXML();
		}
		return "error";		
	}

	@GetMapping("/order/QRcode/pay/{orderNo}")
	public String qrcodePay(@PathVariable("orderNo") String orderNo, ModelMap mmap, HttpServletRequest request) throws Exception {
		String payAgent = ProductUtils.getUserAgentWap(request);
		if(!"unknown".equals(payAgent)){
			SysFactoryOrder sysFactoryOrder = new SysFactoryOrder();
			sysFactoryOrder.setOrderNo(orderNo);
			List<SysFactoryOrder> factoryOrders = sysFactoryOrderService.selectSysFactoryOrderList(sysFactoryOrder);
			if (CollectionUtils.isNotEmpty(factoryOrders)) {
				sysFactoryOrder = factoryOrders.get(0);
			}
			Map<String, Object> resultMap = Maps.newHashMap();
			if(PayMethodConstants.ALIPAY.equals(payAgent)){
				resultMap = ((ISysFactoryPayService)SpringUtils.getBean("sysFactoryAlipayService")).payPrecreate(sysFactoryOrder);
				
				sysFactoryOrder.setPayMethod(PayMethodConstants.ALIPAY);
				sysFactoryOrderService.updateSysFactoryOrder(sysFactoryOrder);				
			} else if(PayMethodConstants.WEIXIN.equals(payAgent)){				
				resultMap = ((ISysFactoryPayService)SpringUtils.getBean("sysFactoryWeixinService")).payPrecreate(sysFactoryOrder);
				
				sysFactoryOrder.setPayMethod(PayMethodConstants.WEIXIN);
				sysFactoryOrderService.updateSysFactoryOrder(sysFactoryOrder);
			}
			
			String codeUrl = (String) StringUtils.nvl(resultMap.get("codeUrl"), "");
			if(StringUtils.isNotBlank(codeUrl)){
				return "redirect:" + codeUrl;
			}
			mmap.put("msg", resultMap.get("msg"));
			logger.error(JSON.toJSONString(resultMap));
		}
		
		return "system/factory/order/nopay" ;		
	}
}
