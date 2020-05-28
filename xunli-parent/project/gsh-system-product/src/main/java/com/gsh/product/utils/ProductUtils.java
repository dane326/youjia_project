package com.gsh.product.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.spring.SpringUtils;
import com.gsh.product.service.impl.SysProductServiceImpl;

public class ProductUtils {
	public static Object getFieldValueByName(Object source, String propertyName) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		return src.getPropertyValue(propertyName);
	}

	/**
	 * VIP权限验证数量
	 * @param factoryCode
	 * @param cfName
	 * @param num
	 * @return
	 */
	public static Map<String, Object> checkNumVipPermissions(Long factoryCode, String cfName, int num){
		SysProductServiceImpl sysProductServiceImpl = SpringUtils.getBean("sysProductServiceImpl");
		return sysProductServiceImpl.checkNumVipPermissions(factoryCode, cfName, num);
	}

	/**
	 * 解析alipay异步通知的参数
	 * @param requestParams 样例：
	 * {
	 *	"gmt_create": ["2017-07-14 14:38:54"],
	 *	"charset": ["utf-8"]
	 *	...
	 *	}
	 */
	public static Map<String, String> parseParams(Map<?, ?> requestParams){
		Map<String,String> params = new HashMap<String,String>();
		for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
		    //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		return params;
	}
	
	/**
	 * 获取客户端为 微信/支付宝
	 */
	public static String getUserAgentWap(HttpServletRequest request) {
		String agent = ((HttpServletRequest) request).getHeader("user-agent");
		if (StringUtils.isNotBlank(agent)) {
			agent = agent.toLowerCase();
			if (agent.indexOf("micromessenger") >= 0) {
				return "weixin";
			} else if (agent.indexOf("alipayclient") >= 0) {
				return "alipay";
			} else {
				return "unknown";
			}
		}
		return "unknown";
	}
	
	/**
	 * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
	 * @param amount
	 * @return
	 */
	public static String changeY2F(String amount) {
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
		// 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
		}
		return amLong.toString();
	}
	
	/**
	 *  将流转换为xml 
	 * @param in
	 * @param charset
	 * @throws IOException
	 */
	public static String copyToString(InputStream in, Charset charset) throws IOException {
		StringBuilder out = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(in, charset);
		char[] buffer = new char[4096];
		int bytesRead = -1;
		while ((bytesRead = reader.read(buffer)) != -1) {
			out.append(buffer, 0, bytesRead);
		}
		return out.toString();
	}
	
	/**
	 * 支付结果成功时，发给微信支付的参数
	 * @return
	 */
	public static String generatePaySuccessReplyXML() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<xml>").append("<return_code><![CDATA[SUCCESS]]></return_code>")
		.append("<return_msg><![CDATA[OK]]></return_msg>").append("</xml>");
		return stringBuffer.toString();
	}
	
	/**
	 * 支付结果失败时，发给微信支付的参数
	 * @return
	 */
	public static String generatePayErrorReplyXML() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<xml>").append("<return_code><![CDATA[FAIL]]></return_code>")
		.append("<return_msg><![CDATA[ERROR]]></return_msg>").append("</xml>");
		return stringBuffer.toString();
	}

	/**
	 * 支付宝结果成功时，发给支付宝的参数
	 * @return
	 */
	public static String generatePaySuccessReplyParams() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("success");
		return stringBuffer.toString();
	}
	/**
	 * 支付宝结果失败时，发给支付宝的参数
	 * @return
	 */
	public static String generatePayErrorReplyParams() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("failure");
		return stringBuffer.toString();
	}
}
