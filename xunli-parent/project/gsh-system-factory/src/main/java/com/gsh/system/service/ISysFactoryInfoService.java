package com.gsh.system.service;

import java.util.List;

import com.gsh.common.core.domain.BaseEntity;
import com.gsh.system.domain.SysFactoryInfo;

/**
 * 系统资料Service接口
 * 
 * @author gsh
 * @date 2019-10-18
 */
public interface ISysFactoryInfoService 
{
    /**
     * 查询系统资料
     * 
     * @param id 系统资料ID
     * @return 系统资料
     */
    public SysFactoryInfo selectSysFactoryInfoById(Long id);

    /**
     * 查询系统资料列表
     * 
     * @param sysFactoryInfo 系统资料
     * @return 系统资料集合
     */
    public List<SysFactoryInfo> selectSysFactoryInfoList(SysFactoryInfo sysFactoryInfo);

    /**
     * 新增系统资料
     * 
     * @param sysFactoryInfo 系统资料
     * @return 结果
     */
    public int insertSysFactoryInfo(SysFactoryInfo sysFactoryInfo);

    /**
     * 修改系统资料
     * 
     * @param sysFactoryInfo 系统资料
     * @return 结果
     */
    public int updateSysFactoryInfo(SysFactoryInfo sysFactoryInfo);

    /**
     * 批量删除系统资料
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFactoryInfoByIds(String ids);

    /**
     * 删除系统资料信息
     * 
     * @param id 系统资料ID
     * @return 结果
     */
    public int deleteSysFactoryInfoById(Long id);
    /**
     * 批量逻辑删除系统资料
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysFactoryInfoByIds(String ids);

    /**
     * 逻辑删除系统资料信息
     *
     * @param id 系统资料ID
     * @return 结果
     */
    public int logicDeleteSysFactoryInfoById(Long id);

    /**
     * 插入entity
     * @param entity
     * @param createFactoryInfoFlag
     * @return
     */
    public int insertFactoryInfoDeptUser(BaseEntity entity, Boolean createFactoryInfoFlag);

    /**
     * 根据属性获取属性的值
     * @param fieldName
     * @return
     */
    public String getFieldValue(String fieldName);

    /**
     * 根据属性获取属性的值
     * @param id
     * @param fieldName
     * @return
     */
    public String getFieldValue(Long id, String fieldName);

}
