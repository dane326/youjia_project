package com.gsh.system.mapper;

import com.gsh.system.domain.SysFactoryInfo;
import java.util.List;

/**
 * 系统资料Mapper接口
 * 
 * @author gsh
 * @date 2019-10-18
 */
public interface SysFactoryInfoMapper 
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
     * 删除系统资料
     *
     * @param id 系统资料ID
     * @return 结果
     */
    public int deleteSysFactoryInfoById(Long id);

    /**
     * 批量删除系统资料
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFactoryInfoByIds(String[] ids);

    /**
     *逻辑删除系统资料
     *
     * @param id 系统资料ID
     * @return 结果
     */
    public int logicDeleteSysFactoryInfoById(Long id);

    /**
     * 批量逻辑删除系统资料
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysFactoryInfoByIds(String[] ids);


    public List<SysFactoryInfo> selectAllSysFactoryInfo();
}
