package com.gsh.system.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.AlipayApiException;
import com.gsh.system.domain.SysFactoryOrder;

public interface ISysFactoryPayService {

	/**
	 * 电脑网站支付
	 * @param order
	 * @return
	 * @throws AlipayApiException
	 */
    String pay(SysFactoryOrder order) throws Exception;
    
    /**
     * 当面付(扫码)
     * @param order
     * @return
     * @throws AlipayApiException
     */
    Map<String, Object> payPrecreate(SysFactoryOrder order) throws Exception;
    /**
     * 验签
     * @param params
     * @return
     */    
    Boolean checkSignWithParams(Map<String, String> params);

    /**
     * 查询订单状态
     * @param outTradeNo
     * @param tradeNo
     * @return
     * @throws AlipayApiException
     */
	Map<String, Object> queryPay(String outTradeNo, String tradeNo) throws Exception;

	/**
	 * 获取请求参数
	 * @param request
	 * @return
	 */
	Map<String, String> getRequestParams(HttpServletRequest request);
}
