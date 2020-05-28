package com.gsh.system.sms.sender.impl;

import com.gsh.system.sms.domain.SysSmsConfig;
import com.gsh.system.sms.domain.SysSmsRecord;
import com.gsh.system.sms.sender.ISysSmsSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.gsh.common.utils.http.HttpUtils;

/**
 * 腾讯云 短信发送实现类
 */
@Service("SysSmsSenderTencentService")
public class SysSmsSenderTencentServiceImpl implements ISysSmsSenderService {
	
	private static final Logger logger = LoggerFactory.getLogger(SysSmsSenderTencentServiceImpl.class);

	@Override
	public boolean sendSms(SysSmsConfig sysSmsConfig, SysSmsRecord sysSmsRecord) {
		try {
			JSONObject confParam = JSONObject.parseObject(sysSmsConfig
					.getConfParam());
			// url = https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid={}&random={}
			String url = String.format(confParam.getString("Url"),
							confParam.getString("sdkappid"),
							System.currentTimeMillis());
			JSONObject param = new JSONObject();
			param.put("ext", "");
			param.put("extend", "");
			param.put("params", "[1,2,3]");
			param.put("sig", confParam.getString("sig"));
			param.put("sign", confParam.getString("sign"));
			param.put("tel", sysSmsRecord.getReceiverMobile());
			param.put("time", System.currentTimeMillis());
			param.put("tpl_id", confParam.getString("tpl_id"));
			HttpUtils.sendPost(url, param.toString());
			return true;
		} catch (Exception e) {
			logger.error("短信发送异常", e);
			return false;
		}
	}

}
