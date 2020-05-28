package com.gsh.system.service;

import java.util.Map;

import com.gsh.system.domain.SysFactoryOrder;

/**
 * 订单Service接口
 * 
 * @author lcs
 * @date 2020-02-17
 */
public interface ISysFactoryService {

	/**
	 * 同步订单
	 * @param sysFactoryOrder
	 * @return
	 */
	Map<String, Object> synchronizeInsertPayOrder(SysFactoryOrder sysFactoryOrder);

	/**
	 * 更新订单
	 * @param sysFactoryOrder
	 * @return
	 */
	Map<String, Object> synchronizeUpdatePayOrder(SysFactoryOrder sysFactoryOrder);
	
}
