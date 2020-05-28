package com.gsh.product.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gsh.common.config.Global;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.domain.AjaxResult.Type;
import com.gsh.common.exception.VipPermissionException;
import com.gsh.product.service.IRestService;
import com.gsh.product.service.ISysProductService;

/**
 * 产品Service业务层处理
 * 
 * @author lcs
 * @date 2020-02-17
 */
@Service
public class SysProductServiceImpl implements ISysProductService {

	private String prodRestUrl;

	public String getProdRestUrl() {
		return prodRestUrl;
	}
	@Value("${gsh.prodRestUrl}")
	public void setProdRestUrl(String prodRestUrl) {
		this.prodRestUrl = prodRestUrl;
	}

	@Autowired
	private IRestService restService;
	
	@Override
	public List<Map<String, Object>> selectSysProdConfList(Map<String, Object> paramMap) {
		return JSON.parseObject(restService.postRestService(prodRestUrl + "/order/system/prod/conf/list", paramMap), new TypeReference<List<Map<String, Object>>>(){});
	}

	@Override
	public List<Map<String, Object>> selectSysProdOrderList(Map<String, Object> paramMap) {
		return JSON.parseObject(restService.postRestService(prodRestUrl + "/order/system/prod/order/list", paramMap), new TypeReference<List<Map<String, Object>>>(){});
	}
	
	@Override
	public List<Map<String, Object>> selectSysProdConfListByModuleName(Long factoryCode, String moduleEnname) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("deleted", "0");
		paramMap.put("moduleEnname", moduleEnname);
		paramMap.put("factoryCode", factoryCode);
		paramMap.put("contextName", Global.getContextPathNoSeparator());
		return JSON.parseObject(restService.postRestService(prodRestUrl + "/order/system/prod/conf/list", paramMap), new TypeReference<List<Map<String, Object>>>(){});
	}
	
	@Override
	public Map<String, Object> handleVipPermissions(Long factoryCode, List<String> configEnnames) {
		String url = prodRestUrl + String.format("/order/system/prod/handle/code/%s", Global.getContextPathNoSeparator(), factoryCode);
		AjaxResult resultMap = JSON.parseObject(restService.postRestService(url, configEnnames), AjaxResult.class);
		if(MapUtils.isNotEmpty(resultMap) && Type.ERROR.value() == Integer.valueOf(String.valueOf(resultMap.get("code")))){
			String exMsg = String.valueOf(resultMap.get("msg"));
			if(StringUtils.isNotBlank(exMsg)){
				throw new VipPermissionException(exMsg);
			}
		}
		
		return resultMap;
	}

	@Override
	public Map<String, Object> checkVipPermissionsByModuleName(Long factoryCode, String moduleEnname, List<String> configEnnames, Map<String, Object> beanMap) {
	    Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("beanMap",beanMap);
        paramMap.put("configEnnames",configEnnames);
        String url = prodRestUrl + String.format("/order/system/prod/check/code/%s/%s/app/%s/bean", Global.getContextPathNoSeparator(), factoryCode, moduleEnname);
		AjaxResult resultMap = JSON.parseObject(restService.postRestService(url, paramMap), AjaxResult.class);
		if(MapUtils.isNotEmpty(resultMap) && Type.ERROR.value() == Integer.valueOf(String.valueOf(resultMap.get("code")))){
			String exMsg = String.valueOf(resultMap.get("msg"));
			if(StringUtils.isNotBlank(exMsg)){
				throw new VipPermissionException(exMsg);
			}
		}
		
		return resultMap;
	}

    @Override
    public Map<String, Object> checkNumVipPermissions( Long factoryCode, String cfName, int num){
    	String url = prodRestUrl + String.format("/order/system/prod/check/code/%s/%s/conf/%s/%s", Global.getContextPathNoSeparator(), factoryCode, cfName, num);
        AjaxResult resultMap = JSON.parseObject(restService.postRestService(url, new HashMap<>()), AjaxResult.class);
        if(MapUtils.isNotEmpty(resultMap) && Type.ERROR.value() == Integer.valueOf(String.valueOf(resultMap.get("code")))){
            String exMsg = String.valueOf(resultMap.get("msg"));
            if(StringUtils.isNotBlank(exMsg)){
                throw new VipPermissionException(exMsg);
            }
        }
        return resultMap;
    }

	@Override
	public Map<String, Object> selectVipPermissionsByModuleName(Long factoryCode, String moduleEnname) {
		String url = prodRestUrl + String.format("/order/system/prod/get/code/%s/%s/app/%s", Global.getContextPathNoSeparator(), factoryCode, moduleEnname);
		return JSON.parseObject(restService.getRestService(url, null));
	}

	@Override
	public Map<String, Object> selectVipPermissionsByCfName(Long factoryCode, String cfName) {
		String url = prodRestUrl + String.format("/order/system/prod/select/code/%s/%s/conf/%s", Global.getContextPathNoSeparator(), factoryCode, cfName);
		return JSON.parseObject(restService.getRestService(url, null));
	}
	
	@Override
	public List<Map<String, Object>> selectInfoListByContextName(String contextName) {
		String url = prodRestUrl + String.format("/order/system/prod/info/%s/list", contextName);
		return JSON.parseObject(restService.getRestService(url, null), new TypeReference<List<Map<String, Object>>>(){});
	}
	
	@Override
	public String selectVipNameByContextNameAndVipLevel(String contextName, String vipLevel) {
		String url = prodRestUrl + String.format("/order/system/prod/info/%s/vipLevel/%s", contextName, vipLevel);
		return restService.getRestService(url, null);
	}
	
	@Override
	public Map<String, Object> synchronizeSaveProdOrder(Map<String, Object> params) {
		return JSON.parseObject(restService.postRestService(prodRestUrl + "/order/system/prod/order/save", params));
	}
}
