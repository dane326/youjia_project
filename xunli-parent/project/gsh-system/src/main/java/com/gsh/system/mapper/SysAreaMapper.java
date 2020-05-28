package com.gsh.system.mapper;

import com.gsh.system.domain.SysArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 地域Mapper接口
 * 
 * @author gsh-xrl
 * @date 2019-11-12
 */
public interface SysAreaMapper 
{
    /**
     * 查询地域
     * 
     * @param areaId 地域ID
     * @return 地域
     */
    public SysArea selectSysAreaById(Long areaId);

    /**
     * 查询地域列表
     * 
     * @param sysArea 地域
     * @return 地域集合
     */
    public List<SysArea> selectSysAreaList(SysArea sysArea);

    /**
     * 新增地域
     * 
     * @param sysArea 地域
     * @return 结果
     */
    public int insertSysArea(SysArea sysArea);

    /**
     * 修改地域
     * 
     * @param sysArea 地域
     * @return 结果
     */
    public int updateSysArea(SysArea sysArea);

    /**
     * 删除地域
     * 
     * @param areaId 地域ID
     * @return 结果
     */
    public int deleteSysAreaById(Long areaId);

    /**
     * 批量删除地域
     * 
     * @param areaIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysAreaByIds(String[] areaIds);

    public SysArea checkAreaNameUnique(@Param("areaName") String areaName, @Param("parentId") Long parentId);
}
