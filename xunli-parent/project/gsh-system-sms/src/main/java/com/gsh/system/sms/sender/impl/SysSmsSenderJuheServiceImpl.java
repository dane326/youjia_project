package com.gsh.system.sms.sender.impl;

import com.alibaba.fastjson.JSONObject;
import com.gsh.common.utils.DateUtils;
import com.gsh.system.sms.domain.SysSmsConfig;
import com.gsh.system.sms.domain.SysSmsRecord;
import com.gsh.system.sms.sender.ISysSmsSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 聚合网络的短信发送实现类
 * @author Administrator
 *
 */
@Service("SysSmsSenderJuheService")
public class SysSmsSenderJuheServiceImpl implements ISysSmsSenderService {

	private static final String HTTP_POST_NAME = "POST";

	private static final String HTTP_GET_NAME = "GET";

	private static final String ERROR_CODE = "error_code";

	private static final Logger logger = LoggerFactory.getLogger(SysSmsSenderJuheServiceImpl.class);

	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	/**
	 * 实现发送短信的方法
	 */
	@Override
	public boolean sendSms(SysSmsConfig sysSmsConfig, SysSmsRecord sysSmsRecord) {
		JSONObject confParam = JSONObject.parseObject(sysSmsConfig.getConfParam());

		String result = null;
		// 请求参数
		Map<String, Object> params = new HashMap<String, Object>(16);
		// 接收短信的手机号码
		params.put("mobile", sysSmsRecord.getReceiverMobile());
		// 短信模板ID，请参考个人中心短信模板设置
		params.put("tpl_id", confParam.getString("tpl_id"));
		// 变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a
		// href="http://www.juhe.cn/news/index/id/50"
		// target="_blank">详细说明></a>
		params.put("tpl_value", confParam.getString("tpl_value"));
		// 应用APPKEY(应用详细页查询)
		params.put("key", confParam.getString("key"));
		// 返回数据的格式,xml或json，默认json
		params.put("dtype", confParam.getString("dtype"));

		try {
			//result = net(confParam.getString("Url"), params, "GET");
			result = "{error_code:0}";
			JSONObject object = JSONObject.parseObject(result);
			if (object.getInteger(ERROR_CODE) == 0) {
				sysSmsRecord.setSmsConfId(sysSmsConfig.getId());
				sysSmsRecord.setSmsConfName(sysSmsConfig.getName());
				sysSmsRecord.setSuccessTime(DateUtils.getNowDate());
				sysSmsRecord.setSuccessFlag(1L);
				return true;
			} else {
				throw new Exception("短信发送失败");
			}
		} catch (Exception e) {
			logger.error("短信发送异常", e);
			return false;
		}
	}
	
	/**
	 * 1.屏蔽词检查测
	 * @param sysSmsConfig
	 * @param sysSmsRecord
	 */
	public static void sendBlack(SysSmsConfig sysSmsConfig, SysSmsRecord sysSmsRecord) {
		JSONObject confParam = JSONObject.parseObject(sysSmsConfig.getConfParam());
		String result = null;
		// 请求参数
		Map<String, Object> params = new HashMap<String, Object>(16);
		// 需要检测的短信内容，需要UTF8 URLENCODE
		params.put("word", "");
		// 应用APPKEY(应用详细页查询)
		params.put("key", confParam.getString("key"));
		try {
			result = net(confParam.getString("black_url"), params, HTTP_GET_NAME);
			JSONObject object = JSONObject.parseObject(result);
			if (object.getInteger(ERROR_CODE) == 0) {
				System.out.println(object.get("result"));
			} else {
				System.out.println(object.get(ERROR_CODE) + ":" + object.get("reason"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	/**
	 *
	 * @param strUrl
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param method
	 *            请求方法
	 * @return 网络请求字符串
	 * @throws Exception
	 */
	public static String net(String strUrl, Map<String, Object> params, String method) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals(HTTP_GET_NAME)) {
				strUrl = strUrl + "?" + urlencode(params);
			}
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals(HTTP_GET_NAME)) {
				conn.setRequestMethod(HTTP_GET_NAME);
			} else {
				conn.setRequestMethod(HTTP_POST_NAME);
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals(HTTP_POST_NAME)) {
				try {
					DataOutputStream out = new DataOutputStream(conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	/**
	 * 将map型转为请求参数型
	 * @param Mapdata
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String urlencode(Map<String, Object> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
