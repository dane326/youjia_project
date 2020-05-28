package com.gsh.system.mapper;

import java.util.List;
import com.gsh.system.domain.SysUserLabel;

/**
 * 用户标签映射Mapper接口
 * 
 * @author wyk
 * @date 2020-03-25
 */
public interface SysUserLabelMapper 
{
    /**
     * 查询用户标签映射
     * 
     * @param userId 用户标签映射ID
     * @return 用户标签映射
     */
    public SysUserLabel selectSysUserLabelById(Long userId);

    /**
     * 查询用户标签映射列表
     * 
     * @param sysUserLabel 用户标签映射
     * @return 用户标签映射集合
     */
    public List<SysUserLabel> selectSysUserLabelList(SysUserLabel sysUserLabel);

    /**
     * 新增用户标签映射
     * 
     * @param sysUserLabel 用户标签映射
     * @return 结果
     */
    public int insertSysUserLabel(SysUserLabel sysUserLabel);

    /**
     * 修改用户标签映射
     * 
     * @param sysUserLabel 用户标签映射
     * @return 结果
     */
    public int updateSysUserLabel(SysUserLabel sysUserLabel);

    /**
     * 删除用户标签映射
     *
     * @param userId 用户标签映射ID
     * @return 结果
     */
    public int deleteSysUserLabelById(Long userId);

    /**
     * 批量删除用户标签映射
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysUserLabelByIds(String[] userIds);

    /**
     *逻辑删除用户标签映射
     *
     * @param userId 用户标签映射ID
     * @return 结果
     */
    public int logicDeleteSysUserLabelById(Long userId);

    /**
     * 批量逻辑删除用户标签映射
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysUserLabelByIds(String[] userIds);

    public int batchSysFactoryUserLabel(List<SysUserLabel> sysFactoryUserLabels);
}
