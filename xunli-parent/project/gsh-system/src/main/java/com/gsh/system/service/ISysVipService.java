package com.gsh.system.service;

/**
 * vipService接口
 * 
 * @author gsh
 * @date 2020-04-04
 */
public interface ISysVipService {
	/**
	 * 判断同时在线人数是否超限
	 * @param appName
	 * @param factoryCode
	 * @return
	 */
	public Boolean checkVipOnlineusersNum(String appName, Long factoryCode);
	
	/**
	 * 判断成员账号数是否超限
	 * @param appName
	 * @param factoryCode
	 * @return
	 */
	public Boolean checkVipAccountsNum(String appName, Long factoryCode);

}
