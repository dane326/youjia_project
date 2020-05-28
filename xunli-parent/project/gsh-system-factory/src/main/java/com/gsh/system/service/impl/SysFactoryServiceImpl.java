package com.gsh.system.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.gsh.common.config.Global;
import com.gsh.common.utils.StringUtils;
import com.gsh.product.service.IRestService;
import com.gsh.system.domain.SysFactoryOrder;
import com.gsh.system.service.ISysFactoryService;

/**
 * 订单管理Service业务层处理
 * 
 * @author gsh
 * @date 2019-10-29
 */
@Service
public class SysFactoryServiceImpl implements ISysFactoryService {
	private String prodRestUrl;
	@Value("${gsh.prodRestUrl}")
	public void setProdRestUrl(String prodRestUrl) {
		this.prodRestUrl = prodRestUrl;
	}

	@Autowired
	private IRestService restService;
	
	@Override
	public Map<String, Object> synchronizeInsertPayOrder(SysFactoryOrder sysFactoryOrder) {
		if(StringUtils.isBlank(sysFactoryOrder.getContextName())){
			sysFactoryOrder.setContextName(Global.getContextPathNoSeparator());
		}
		return JSON.parseObject(restService.postRestService(prodRestUrl + "/order/system/prod/sync/factoryOrder/insert", sysFactoryOrder));
	}
	
	@Override
	public Map<String, Object> synchronizeUpdatePayOrder(SysFactoryOrder sysFactoryOrder) {
		if(StringUtils.isBlank(sysFactoryOrder.getContextName())){
			sysFactoryOrder.setContextName(Global.getContextPathNoSeparator());
		}
		return JSON.parseObject(restService.postRestService(prodRestUrl + "/order/system/prod/sync/factoryOrder/update", sysFactoryOrder));
	}

}