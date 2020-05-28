package com.gsh.product.service;

import java.util.List;
import java.util.Map;

/**
 * 产品Service接口
 * 
 * @author lcs
 * @date 2020-02-17
 */
public interface ISysProductService {

	/**
	 * 查询产品列表
	 * 
	 * @param paramMap
	 * @return 产品配置集合
	 */
	public List<Map<String, Object>> selectSysProdConfList(Map<String, Object> paramMap);

	/**
	 * 根据模块名称查询产品列表
	 * 
	 * @param factoryCode
	 * @param moduleEnname 模块名称
	 * @return
	 */
	List<Map<String, Object>> selectSysProdConfListByModuleName(Long factoryCode, String moduleEnname);

	/**
     * 查询产品信息列表
     * 
     * @param paramMap
     * @return 产品信息集合
     */
    List<Map<String, Object>> selectSysProdOrderList(Map<String, Object> paramMap);
    
    /**
     * 校验数据是否合法
     * @param factoryCode
     * @param moduleEnname
     * @param beanMap
     */
    Map<String, Object> checkVipPermissionsByModuleName(Long factoryCode, String moduleEnname, List<String> configEnnames, Map<String, Object> beanMap);

    /**
     * 校验数据是否合法
     * @param factoryCode
     * @param cfName
     * @param beanMap
     */
    Map<String, Object> selectVipPermissionsByCfName(Long factoryCode, String cfName);

    /**
     * 校验是否有权限
     * @param factoryCode
     * @param configEnnames
     */
    Map<String, Object> handleVipPermissions(Long factoryCode, List<String> configEnnames);

    /**
     * 获取用户拥有的权限
     * @param factoryCode
     * @param moduleEnname
     * @return
     */
	Map<String, Object> selectVipPermissionsByModuleName(Long factoryCode, String moduleEnname);


    /**
     * 校验是否有数量权限
     * @param factoryCode
     * @param cfName
     * @param num
     * @return
     */
    Map<String, Object> checkNumVipPermissions(Long factoryCode, String cfName, int num);
    
    /**
     * 获取指定系统的产品分类信息
     * @param contextName
     * @return
     */
    List<Map<String, Object>> selectInfoListByContextName(String contextName);

    /**
     * 获取指定系统，指定级别的级别名称
     * @param contextName
     * @param vipLevel
     * @return
     */
	String selectVipNameByContextNameAndVipLevel(String contextName, String vipLevel);
	
	/**
	 * 同步权限
	 * @param params
	 * @return
	 */
	Map<String, Object> synchronizeSaveProdOrder(Map<String, Object> params);
}
