package com.gsh.system.sms.sender;

import com.gsh.system.sms.domain.SysSmsConfig;
import com.gsh.system.sms.domain.SysSmsRecord;

/**
 * 发送短信接口
 * @author Administrator
 *
 */
public interface ISysSmsSenderService {

	/**
	 * sendSms
	 * @param sysSmsConfig
	 * @param sysSmsRecord
	 * @return
	 */
	public boolean sendSms(SysSmsConfig sysSmsConfig, SysSmsRecord sysSmsRecord);

}
