package com.gsh.system.service.impl;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.collect.Maps;
import com.gsh.common.config.FactoryConfig;
import com.gsh.common.utils.IpUtils;
import com.gsh.common.utils.StringUtils;
import com.gsh.product.config.WinxinPayConfig;
import com.gsh.product.constants.PayMethodConstants;
import com.gsh.product.utils.ProductUtils;
import com.gsh.system.domain.SysFactoryOrder;
import com.gsh.system.service.ISysFactoryPayService;

/**
 * 微信支付类
 * 
 * @author LCS
 *
 */
@Service("sysFactoryWeixinService")
public class SysFactoryWeixinServiceImpl implements ISysFactoryPayService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WinxinPayConfig winxinPayConfig;
	
	@Autowired
	private WXPay wxPay;

	@Override
	public String pay(SysFactoryOrder order) throws Exception {
		return null;
	}

	@Override
	public Map<String, Object> payPrecreate(SysFactoryOrder order) throws Exception {
		Map<String, Object> resultMap = Maps.newHashMap();
		
		Map<String, String> reqData = new HashMap<String, String>();
		// 商品描述 必填
		reqData.put("body", StringUtils.nvl(order.getOrderSubject(), ""));
		// 商品订单号 必填
		reqData.put("out_trade_no", StringUtils.nvl(order.getOrderNo(), ""));
		// 支付金额(将元转换为分) 必填
		reqData.put("total_fee", ProductUtils.changeY2F(StringUtils.nvl(order.getMoney(), "0")));
		// 终端IP
		reqData.put("spbill_create_ip", IpUtils.getHostIp());
		// 异步回调通知地址通知url必须为外网可访问的url，不能携带参数 必填
		reqData.put("notify_url", FactoryConfig.getPayNotifyUrl() + "/" + PayMethodConstants.WEIXIN);
		// 交易类型NATIVE 必填
		reqData.put("trade_type", "NATIVE");

		// 执行下单 统一下单接口unifiedOrder
		Map<String, String> responseparams = wxPay.unifiedOrder(reqData);
		if (WXPayConstants.SUCCESS.equals(responseparams.get("return_code")) && WXPayConstants.SUCCESS.equals(responseparams.get("result_code"))) {
			// 验签
			if(checkSignWithParams(responseparams)){
				resultMap.put("codeUrl", responseparams.get("code_url"));
			}			
		}
        resultMap.put("msg", responseparams.get("return_msg"));

		return resultMap;
	}

	@Override
	public Boolean checkSignWithParams(Map<String, String> params) {
		try {
			//获取签名sign
			String sign = params.get("sign");
			//重新生成新sign
			String newsign = WXPayUtil.generateSignature(params, winxinPayConfig.getKey(), winxinPayConfig.getSignType());
			// 验证签名验证是否成功
			if(!sign.equals(newsign)) {
				logger.info("微信公众号支付异步通知>>验证签名失败！");
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public Map<String, Object> queryPay(String outTradeNo, String tradeNo) throws Exception {

		return null;
	}

	@Override
	public Map<String, String> getRequestParams(HttpServletRequest request) {
		String xmlData = null;
		try {
			xmlData = ProductUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
			// 将得到xml转为map,验证签名是否成功
			return WXPayUtil.xmlToMap(xmlData);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Maps.newHashMap();
	}
}
