package com.gsh.system.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.SignItem;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.parser.json.JsonConverter;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.google.common.collect.Maps;
import com.gsh.common.config.FactoryConfig;
import com.gsh.common.utils.StringUtils;
import com.gsh.product.config.AlipayConfig;
import com.gsh.product.constants.PayMethodConstants;
import com.gsh.product.utils.ProductUtils;
import com.gsh.system.domain.SysFactoryOrder;
import com.gsh.system.service.ISysFactoryPayService;

/**
 * 支付宝支付类
 * @author LCS
 *
 */
@Service("sysFactoryAlipayService")
public class SysFactoryAlipayServiceImpl implements ISysFactoryPayService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AlipayConfig alipayConfig;
	
	@Autowired
	private AlipayClient alipayClient;

    @Override
    public String pay(SysFactoryOrder order) throws AlipayApiException {
        AlipayTradePagePayModel pagePayModel = new AlipayTradePagePayModel();
        //商户订单号，商户网站订单系统中唯一订单号，必填
        pagePayModel.setOutTradeNo(StringUtils.nvl(order.getOrderNo(),""));
        //付款金额，必填
        pagePayModel.setTotalAmount(StringUtils.nvl(order.getMoney(),"0"));
        //订单名称，必填
        pagePayModel.setSubject(StringUtils.nvl(order.getOrderSubject(),""));
        
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(FactoryConfig.getPayReturnUrl() + "/" + order.getFactoryCode());
        alipayRequest.setNotifyUrl(FactoryConfig.getPayNotifyUrl() + "/" + PayMethodConstants.ALIPAY);
        alipayRequest.setBizModel(pagePayModel);
        
        //请求
        AlipayTradePagePayResponse aliResponse = alipayClient.pageExecute(alipayRequest);
        return aliResponse.getBody();
    }

	@Override
	public Map<String, Object> payPrecreate(SysFactoryOrder order) throws AlipayApiException {
		Map<String, Object> resultMap = Maps.newHashMap();
		
        AlipayTradePrecreateModel precreateModel = new AlipayTradePrecreateModel();
        //商户订单号，商户网站订单系统中唯一订单号，必填
        precreateModel.setOutTradeNo(StringUtils.nvl(order.getOrderNo(),""));
        //付款金额，必填
        precreateModel.setTotalAmount(StringUtils.nvl(order.getMoney(),"0"));
        //订单名称，必填
        precreateModel.setSubject(StringUtils.nvl(order.getOrderSubject(),""));
        
        //设置请求参数
        AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
        alipayRequest.setNotifyUrl(FactoryConfig.getPayNotifyUrl() + "/" + PayMethodConstants.ALIPAY);
        alipayRequest.setBizModel(precreateModel);
        
        //请求
        AlipayTradePrecreateResponse aliResponse = alipayClient.execute(alipayRequest);
        if(aliResponse.isSuccess()){
        	// 验签
        	SignItem signItem = new JsonConverter().getSignItem(alipayRequest, aliResponse.getBody());
        	if(AlipaySignature.rsaCheck(signItem.getSignSourceDate(), signItem.getSign(), alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType())){
        		resultMap.put("codeUrl", aliResponse.getQrCode());
        	}        	
        }
        resultMap.put("msg", aliResponse.getSubMsg());
        return resultMap;
	}
	
	@Override
	public Map<String, String> getRequestParams(HttpServletRequest request){
		return ProductUtils.parseParams(request.getParameterMap());
	}

	@Override
	public Boolean checkSignWithParams(Map<String, String> params) {
        try {
			if(AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType())){
				return true;
			}
		} catch (AlipayApiException e) {
			logger.error(e.getMessage(), e);
		}
        return false;
	}
	
	@Override
	public Map<String, Object> queryPay(String outTradeNo, String tradeNo) throws AlipayApiException {
		Map<String, Object> resultMap = Maps.newHashMap();
        AlipayTradeQueryModel queryModel = new AlipayTradeQueryModel();
        queryModel.setOutTradeNo(outTradeNo);
        
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
        alipayRequest.setBizModel(queryModel);

        AlipayTradeQueryResponse aliResponse = alipayClient.execute(alipayRequest);
		if(aliResponse.isSuccess()){
			resultMap.put("tradeStatus", aliResponse.getTradeStatus());
			resultMap.put("tradeNo", aliResponse.getTradeNo());
        	return resultMap;
        }
		resultMap.put("msg", aliResponse.getSubMsg());
        return resultMap;
	}
}
