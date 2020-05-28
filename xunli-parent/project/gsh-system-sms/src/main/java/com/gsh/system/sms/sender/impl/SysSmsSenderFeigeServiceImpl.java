package com.gsh.system.sms.sender.impl;

import com.alibaba.fastjson.JSONObject;
import com.gsh.common.utils.DateUtils;
import com.gsh.system.sms.domain.SysSmsConfig;
import com.gsh.system.sms.domain.SysSmsRecord;
import com.gsh.system.sms.sender.ISysSmsSenderService;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 飞鸽的短信发送实现类
 * @author Administrator
 *
 */
@Service("SysSmsSenderFeigeService")
public class SysSmsSenderFeigeServiceImpl implements ISysSmsSenderService {

	private static final String RESULT_CODE_OK = "0";
	private static final String RESULT_CODE = "Code";
	private static final Logger logger = LoggerFactory.getLogger(SysSmsSenderFeigeServiceImpl.class);

	/**
	 * 实现发送短信的方法
	 */
	@Override
	public boolean sendSms(SysSmsConfig sysSmsConfig, SysSmsRecord sysSmsRecord) {
		try {
			CloseableHttpClient client = null;
			CloseableHttpResponse response = null;
			try {
				JSONObject confParam = JSONObject.parseObject(sysSmsConfig.getConfParam());
				String content = "";
				//if("94403".equals(confParam.getString("TemplateId"))){
				//	content = sysSmsRecord.getSmsContent();
				//}
				content = sysSmsRecord.getSmsContent();
				//多个变量以||进行分隔，按顺序排列 ，如：变量1||变量2
				List<BasicNameValuePair> formparams = new ArrayList<>();
				formparams.add(new BasicNameValuePair("Account", confParam.getString("Account")));
				// 登录后台  首页显示
				formparams.add(new BasicNameValuePair("Pwd", confParam.getString("Pwd")));
				formparams.add(new BasicNameValuePair("TemplateId", confParam.getString("TemplateId")));
				formparams.add(new BasicNameValuePair("Content", content));
				formparams.add(new BasicNameValuePair("Mobile", sysSmsRecord.getReceiverMobile()));
				// 登录后台  添加签名获取id
				formparams.add(new BasicNameValuePair("SignId", confParam.getString("SignId")));
				HttpPost httpPost = new HttpPost(confParam.getString("Url"));
				httpPost.setEntity(new UrlEncodedFormEntity(formparams,"UTF-8"));
				client = HttpClients.createDefault();
				response = client.execute(httpPost);
				String result = EntityUtils.toString(response.getEntity());
				//String result = "{Code:0}";
				logger.info(result);
				JSONObject resultJson = JSONObject.parseObject(result);
				if (RESULT_CODE_OK.equals(resultJson.getString(RESULT_CODE))) {
					sysSmsRecord.setSmsConfId(sysSmsConfig.getId());
					sysSmsRecord.setSmsConfName(sysSmsConfig.getName());
					sysSmsRecord.setSuccessTime(DateUtils.getNowDate());
					sysSmsRecord.setSuccessFlag(1L);
					return true;
				}else{
					throw new Exception("短信发送失败");
				}
			} finally {
				if (response != null) {
					response.close();
				}
				if (client != null) {
					client.close();
				}
			}
		} catch (Exception e) {
			logger.error("短信发送异常", e);
			return false;
		}
	}

}
