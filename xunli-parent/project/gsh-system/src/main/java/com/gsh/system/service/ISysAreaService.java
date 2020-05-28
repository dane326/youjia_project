package com.gsh.system.service;

import com.gsh.common.core.domain.Ztree;
import com.gsh.system.domain.SysArea;

import java.util.List;

/**
 * 地域Service接口
 * 
 * @author gsh-xrl
 * @date 2019-11-12
 */
public interface ISysAreaService 
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
     * 批量删除地域
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysAreaByIds(String ids);

    /**
     * 删除地域信息
     * 
     * @param areaId 地域ID
     * @return 结果
     */
    public int deleteSysAreaById(Long areaId);

    /**
     * 查询地域树列表
     * 
     * @return 所有地域信息
     */
    public List<Ztree> selectSysAreaTree();

    /**
     * 地域唯一性校验
     * @param sysArea
     * @return
     */
    public String checkAreaNameUnique(SysArea sysArea);

}
