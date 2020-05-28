package com.gsh.system.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.gsh.common.config.FactoryConfig;
import com.gsh.common.config.Global;
import com.gsh.common.core.domain.BaseEntity;
import com.gsh.common.core.text.Convert;
import com.gsh.common.utils.DateUtils;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.spring.SpringUtils;
import com.gsh.product.constants.OrderTypeConstants;
import com.gsh.product.service.ISysProductService;
import com.gsh.system.domain.SysConfig;
import com.gsh.system.domain.SysDept;
import com.gsh.system.domain.SysFactoryInfo;
import com.gsh.system.domain.SysUser;
import com.gsh.system.mapper.SysConfigMapper;
import com.gsh.system.mapper.SysDeptMapper;
import com.gsh.system.mapper.SysFactoryInfoMapper;
import com.gsh.system.service.ISysConfigService;
import com.gsh.system.service.ISysFactoryInfoService;
import com.gsh.system.service.ISysUserService;
import com.gsh.system.utils.FactoryUtils;

/**
 * 系统资料Service业务层处理
 * 
 * @author gsh
 * @date 2019-10-18
 */
@Service("factoryInfo")
public class SysFactoryInfoServiceImpl implements ISysFactoryInfoService {
    @Autowired
    private SysFactoryInfoMapper sysFactoryInfoMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysConfigMapper configMapper;
    
    @Autowired
    private ISysConfigService configService;
    
    @Autowired
    private ISysProductService sysProductService;

    /**
     * 查询系统资料
     * 
     * @param id 系统资料ID
     * @return 系统资料
     */
    @Override
    @Cacheable(value = "sys_factory_info_cache", key = "#id+''", unless = "#result == null")
    public SysFactoryInfo selectSysFactoryInfoById(Long id) {
        return sysFactoryInfoMapper.selectSysFactoryInfoById(id);
    }

    /**
     * 查询系统资料列
     * 
     * @param sysFactoryInfo 系统资料
     * @return 系统资料
     */
    @Override
    public List<SysFactoryInfo> selectSysFactoryInfoList(SysFactoryInfo sysFactoryInfo) {
        return sysFactoryInfoMapper.selectSysFactoryInfoList(sysFactoryInfo);
    }

    /**
     * 新增系统资料
     * 
     * @param sysFactoryInfo 系统资料
     * @return 结果
     */
    @Override
    public int insertSysFactoryInfo(SysFactoryInfo sysFactoryInfo) {
        sysFactoryInfo.setCreateTime(DateUtils.getNowDate());
        return sysFactoryInfoMapper.insertSysFactoryInfo(sysFactoryInfo);
    }

    /**
     * 修改系统资料
     * 
     * @param sysFactoryInfo 系统资料
     * @return 结果
     */
    @Override
    @CacheEvict(value = "sys_factory_info_cache", key = "#sysFactoryInfo.id+''")
    public int updateSysFactoryInfo(SysFactoryInfo sysFactoryInfo) {
        sysFactoryInfo.setUpdateTime(DateUtils.getNowDate());
        if(StringUtils.isNotEmpty(sysFactoryInfo.getFactoryName())){
            SysDept dept = new SysDept();
            dept.setFactoryCode(sysFactoryInfo.getId());
            dept.setParentId(FactoryConfig.getDeptRootId());
            deptMapper.selectDeptList(dept).forEach(sysDept -> {
                sysDept.setDeptName(sysFactoryInfo.getFactoryName());
                deptMapper.updateDept(sysDept);
            });
        }
        return sysFactoryInfoMapper.updateSysFactoryInfo(sysFactoryInfo);
    }

    /**
     * 删除系统资料对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysFactoryInfoByIds(String ids) {
        return sysFactoryInfoMapper.deleteSysFactoryInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除系统资料信息
     * 
     * @param id 系统资料ID
     * @return 结果
     */
    @Override
    public int deleteSysFactoryInfoById(Long id) {
        return sysFactoryInfoMapper.deleteSysFactoryInfoById(id);
    }

    /**
     * 逻辑删除系统资料对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteSysFactoryInfoByIds(String ids) {
        return sysFactoryInfoMapper.logicDeleteSysFactoryInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 逻辑删除系统资料信息
     *
     * @param id 系统资料ID
     * @return 结果
     */
    @Override
    public int logicDeleteSysFactoryInfoById(Long id) {
        return sysFactoryInfoMapper.logicDeleteSysFactoryInfoById(id);
    }

    @Override
    @Transactional
    public int insertFactoryInfoDeptUser(BaseEntity entity, Boolean createFactoryInfoFlag) {
        SysUser user = null;
        SysFactoryInfo sysFactoryInfo = null;
        if(entity instanceof SysFactoryInfo){
            sysFactoryInfo = (SysFactoryInfo)entity;
            user = new SysUser();
            user.setUserName(sysFactoryInfo.getLoginName());
            user.setLoginName(sysFactoryInfo.getLoginName());
            user.setPassword(configService.selectConfigByKey("sys.user.initPassword"));
        }else{
            user = (SysUser)entity;
            sysFactoryInfo = new SysFactoryInfo();
            sysFactoryInfo.setLoginName(user.getLoginName());
            sysFactoryInfo.setFactoryName("我的组织结构");
        }
        //为用户赋予默认角色及权限
        user.setSalt(FactoryUtils.randomSalt());
        user.setPassword(FactoryUtils.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        //新增系统资料
        if (createFactoryInfoFlag) {
            // 默认根节点是100
            SysDept deptInfo = deptMapper.selectDeptById(FactoryConfig.getDeptRootId());
            if (deptInfo != null) {
                //新增系统资料
            	sysFactoryInfo.setAccountsNum(FactoryConfig.getProdDefaultAccountNum());
            	sysFactoryInfo.setOnlineusersNum(FactoryConfig.getProdDefaultOnlineUserNum());
            	sysFactoryInfo.setVersionType(FactoryConfig.getProdDefaultVersion());
            	sysFactoryInfo.setVersionName(FactoryConfig.getProdDefaultVersionName());
                sysFactoryInfo.setCreateBy(user.getLoginName());
                sysFactoryInfoMapper.insertSysFactoryInfo(sysFactoryInfo);
                //新增参数信息
                initSysConfigsForFactory(sysFactoryInfo.getId());
                //新增组织结构
                SysDept sysDept = new SysDept();
                sysDept.setParentId(deptInfo.getDeptId());
                sysDept.setAncestors(deptInfo.getAncestors());
                sysDept.setFactoryCode(sysFactoryInfo.getId());
                sysDept.setDeptName(sysFactoryInfo.getFactoryName());
                sysDept.setCreateBy(user.getLoginName());
                deptMapper.insertDept(sysDept);
                //设置组织的部门
                sysFactoryInfo.setFactoryDeptId(sysDept.getDeptId());
                sysFactoryInfoMapper.updateSysFactoryInfo(sysFactoryInfo);
                //设置用户部门id
                user.setDeptId(sysDept.getDeptId());
                
                // 向订单系统同步数据（同步默认的vip权限数据），目前暂时3条数据
                if(FactoryConfig.syncDataToProdSwitch()){
                	Map<String, Object> params = new HashMap<String, Object>();
                    params.put("contextName", Global.getContextPathNoSeparator());
                    params.put("factoryCode", sysFactoryInfo.getId());
                    params.put("createBy", user.getLoginName());
                    params.put("createByName", user.getUserName());
                    //学员数
                    params.put("orderType", OrderTypeConstants.ACCOUNTS);
                    params.put("orderValue", FactoryConfig.getProdDefaultAccountNum());
                    sysProductService.synchronizeSaveProdOrder(params);
                    //在线人数
                    params.put("orderType", OrderTypeConstants.ONLINEUSERS);
                    params.put("orderValue", FactoryConfig.getProdDefaultOnlineUserNum());
                    sysProductService.synchronizeSaveProdOrder(params);
                    //vip级别
                    params.put("orderType", OrderTypeConstants.VERSIONS);
                    params.put("orderValue", FactoryConfig.getProdDefaultVersion());
            		LocalDateTime localDate = LocalDateTime.now().plusDays(FactoryConfig.getProdDefaultVipDays());
                    params.put("expirationTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDate));
                    sysProductService.synchronizeSaveProdOrder(params);
                }                
            }
        }        
        //应用管理员 2
        user.setRoleIds(FactoryConfig.getDefaultRoleIds());
        user.setFactoryCode(sysFactoryInfo.getId());
        
        return sysUserService.insertUser(user);
    }

    /**
     * 新增参数信息
     * @return
     */
    public void initSysConfigsForFactory(Long factoryCode ){
        SysConfig config = new SysConfig();
        config.setFactoryCode(FactoryConfig.getPlatformFactoryCode());
        config.setConfigType("N");
        List<SysConfig> configList=configMapper.selectConfigList(config);
        for (SysConfig sysConfig : configList){
            sysConfig.setFactoryCode(factoryCode);
            sysConfig.setCreateTime(DateUtils.getNowDate());
            configMapper.insertConfig(sysConfig);
        }
    }

    @Override
    public String getFieldValue(String fieldName) {
        SysFactoryInfo factoryInfo = SpringUtils.getAopProxy(this).selectSysFactoryInfoById(FactoryUtils.getFactoryCode());
        if (factoryInfo == null) {
            return "";
        }
        JSONObject jsonObj = (JSONObject) JSONObject.toJSON(factoryInfo);
        return jsonObj.getString(fieldName);
    }

    @Override
    public String getFieldValue(Long id, String fieldName) {
        SysFactoryInfo factoryInfo = SpringUtils.getAopProxy(this).selectSysFactoryInfoById(id);
        if (factoryInfo == null) {
            return "";
        }
        JSONObject jsonObj = (JSONObject) JSONObject.toJSON(factoryInfo);
        return jsonObj.getString(fieldName);
    }
}
