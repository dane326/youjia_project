package com.gsh.system.service.impl;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gsh.common.annotation.FactoryCode;
import com.gsh.common.config.FactoryConfig;
import com.gsh.common.config.Global;
import com.gsh.common.core.domain.AjaxResult.Type;
import com.gsh.common.core.text.Convert;
import com.gsh.common.exception.VipPermissionException;
import com.gsh.common.utils.spring.SpringUtils;
import com.gsh.product.constants.OrderTypeConstants;
import com.gsh.product.service.ISysProductService;
import com.gsh.system.domain.SysFactoryInfo;
import com.gsh.system.domain.SysFactoryOrder;
import com.gsh.system.mapper.SysFactoryInfoMapper;
import com.gsh.system.mapper.SysFactoryOrderMapper;
import com.gsh.system.service.ISysFactoryOrderService;
import com.gsh.system.service.ISysFactoryService;

/**
 * 订单管理Service业务层处理
 * 
 * @author gsh
 * @date 2019-10-29
 */
@Service
public class SysFactoryOrderServiceImpl implements ISysFactoryOrderService {
    @Autowired
    private SysFactoryOrderMapper sysFactoryOrderMapper;
    
    @Autowired
    private SysFactoryInfoMapper sysFactoryInfoMapper;
    
    @Autowired
    private ISysFactoryService sysFactoryService;
    
    @Autowired
    private ISysProductService sysProductService;

    /**
     * 查询订单管理
     * 
     * @param id 订单管理ID
     * @return 订单管理
     */
    @Override
    public SysFactoryOrder selectSysFactoryOrderById(Long id) {
        return sysFactoryOrderMapper.selectSysFactoryOrderById(id);
    }

    /**
     * 查询订单管理列表
     * 
     * @param sysFactoryOrder 订单管理
     * @return 订单管理
     */
    @Override
    @FactoryCode(tableAlias = {})
    public List<SysFactoryOrder> selectSysFactoryOrderList(SysFactoryOrder sysFactoryOrder) {
        return sysFactoryOrderMapper.selectSysFactoryOrderList(sysFactoryOrder);
    }

    /**
     * 新增订单管理
     * 
     * @param sysFactoryOrder 订单管理
     * @return 结果
     */
    @Override
    public int insertSysFactoryOrder(SysFactoryOrder sysFactoryOrder) {
    	//向产品系统同步数据 
    	if(FactoryConfig.syncDataToProdSwitch() && !FactoryConfig.prodDBUnifiedSwitch()){
    		Map<String, Object> resultMap = sysFactoryService.synchronizeInsertPayOrder(sysFactoryOrder);
        	if(MapUtils.isNotEmpty(resultMap) && Type.ERROR.value() == Integer.valueOf(String.valueOf(resultMap.get("code")))){
    			String exMsg = String.valueOf(resultMap.get("msg"));
    			if(StringUtils.isNotBlank(exMsg)){
    				throw new VipPermissionException(exMsg);
    			}
    		}
    	}    	
        return sysFactoryOrderMapper.insertSysFactoryOrder(sysFactoryOrder);
    }

    /**
     * 修改订单管理
     * 
     * @param sysFactoryOrder 订单管理
     * @return 结果
     */
    @Override
    public int updateSysFactoryOrder(SysFactoryOrder sysFactoryOrder) {
    	//向产品系统同步数据 
    	if(FactoryConfig.syncDataToProdSwitch() && !FactoryConfig.prodDBUnifiedSwitch()){
    		Map<String, Object> resultMap = sysFactoryService.synchronizeUpdatePayOrder(sysFactoryOrder);
        	if(MapUtils.isNotEmpty(resultMap) && Type.ERROR.value() == Integer.valueOf(String.valueOf(resultMap.get("code")))){
    			String exMsg = String.valueOf(resultMap.get("msg"));
    			if(StringUtils.isNotBlank(exMsg)){
    				throw new VipPermissionException(exMsg);
    			}
    		}
    	}
    	
        return sysFactoryOrderMapper.updateSysFactoryOrder(sysFactoryOrder);
    }
    
    /**
     * 修改订单管理
     * 
     * @param sysFactoryOrder 订单管理
     * @return 结果
     */
    @Override
    public int updateSysFactoryOrderWithBusiness(SysFactoryOrder sysFactoryOrder) {
    	// 更新info的状态
    	if(sysFactoryOrder.getFactoryCode() != null){
    		SysFactoryInfo sysFactoryInfo = new SysFactoryInfo();
    		sysFactoryInfo.setContextName(sysFactoryOrder.getContextName());
    		sysFactoryInfo.setFactoryCode(sysFactoryOrder.getFactoryCode());    		
    		List<SysFactoryInfo> infos = sysFactoryInfoMapper.selectSysFactoryInfoList(sysFactoryInfo);
    		if(CollectionUtils.isNotEmpty(infos)){
    			sysFactoryInfo = infos.get(0);
    					
    			Map<String, Object> params = new HashMap<String, Object>();
    			
    			if(OrderTypeConstants.ACCOUNTS.equals(sysFactoryOrder.getOrderType())){
        			sysFactoryInfo.setAccountsNum((Integer.parseInt(sysFactoryOrder.getOrderValue()) + Integer.parseInt(sysFactoryInfo.getAccountsNum())) + "");
        			params.put("orderValue", sysFactoryInfo.getAccountsNum());
    			} else if(OrderTypeConstants.ONLINEUSERS.equals(sysFactoryOrder.getOrderType())){
        			sysFactoryInfo.setOnlineusersNum((Integer.parseInt(sysFactoryOrder.getOrderValue()) + Integer.parseInt(sysFactoryInfo.getOnlineusersNum())) + "");
        			params.put("orderValue", sysFactoryInfo.getOnlineusersNum());
        		} else if(OrderTypeConstants.VERSIONS.equals(sysFactoryOrder.getOrderType())){
        			sysFactoryInfo.setVersionType(sysFactoryOrder.getOrderValue());
        			sysFactoryInfo.setVersionName(sysProductService.selectVipNameByContextNameAndVipLevel(Global.getContextPathNoSeparator(), sysFactoryOrder.getOrderValue()));
        			params.put("orderValue", sysFactoryInfo.getVersionType());
        		}
        		
        		sysFactoryInfoMapper.updateSysFactoryInfo(sysFactoryInfo);
        		
        		//同步数据
				if (FactoryConfig.syncDataToProdSwitch()) {
					params.put("contextName", Global.getContextPathNoSeparator());
					params.put("factoryCode", sysFactoryInfo.getId());
					params.put("createBy", sysFactoryInfo.getCreateBy());
					params.put("createByName", sysFactoryInfo.getCreateByName());
					params.put("orderType", sysFactoryOrder.getOrderType());
					params.put("expirationTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(sysFactoryOrder.getBuyPeriod().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
					sysProductService.synchronizeSaveProdOrder(params);
				}     		
    		}
    	}
    	
    	return SpringUtils.getAopProxy(this).updateSysFactoryOrder(sysFactoryOrder);
    }

    /**
     * 删除订单管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysFactoryOrderByIds(String ids) {
        return sysFactoryOrderMapper.deleteSysFactoryOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除订单管理信息
     * 
     * @param id 订单管理ID
     * @return 结果
     */
    public int deleteSysFactoryOrderById(Long id) {
        return sysFactoryOrderMapper.deleteSysFactoryOrderById(id);
    }

    /**
     * 逻辑删除订单管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteSysFactoryOrderByIds(String ids) {
        return sysFactoryOrderMapper.logicDeleteSysFactoryOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 逻辑删除订单管理信息
     *
     * @param id 订单管理ID
     * @return 结果
     */
    public int logicDeleteSysFactoryOrderById(Long id) {
        return sysFactoryOrderMapper.logicDeleteSysFactoryOrderById(id);
    }

}
