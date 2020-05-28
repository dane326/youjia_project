package com.gsh.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gsh.system.mapper.SysUserLabelMapper;
import com.gsh.system.domain.SysUserLabel;
import com.gsh.system.service.ISysUserLabelService;
import com.gsh.common.core.text.Convert;

/**
 * 用户标签映射Service业务层处理
 * 
 * @author wyk
 * @date 2020-03-25
 */
@Service
public class SysUserLabelServiceImpl implements ISysUserLabelService 
{
    @Autowired
    private SysUserLabelMapper sysUserLabelMapper;

    /**
     * 查询用户标签映射
     * 
     * @param userId 用户标签映射ID
     * @return 用户标签映射
     */
    @Override
    public SysUserLabel selectSysUserLabelById(Long userId)
    {
        return sysUserLabelMapper.selectSysUserLabelById(userId);
    }

    /**
     * 查询用户标签映射列表
     * 
     * @param sysUserLabel 用户标签映射
     * @return 用户标签映射
     */
    @Override
    public List<SysUserLabel> selectSysUserLabelList(SysUserLabel sysUserLabel)
    {
        return sysUserLabelMapper.selectSysUserLabelList(sysUserLabel);
    }

    /**
     * 新增用户标签映射
     * 
     * @param sysUserLabel 用户标签映射
     * @return 结果
     */
    @Override
    public int insertSysUserLabel(SysUserLabel sysUserLabel)
    {
        return sysUserLabelMapper.insertSysUserLabel(sysUserLabel);
    }

    /**
     * 修改用户标签映射
     * 
     * @param sysUserLabel 用户标签映射
     * @return 结果
     */
    @Override
    public int updateSysUserLabel(SysUserLabel sysUserLabel)
    {
        return sysUserLabelMapper.updateSysUserLabel(sysUserLabel);
    }

    /**
     * 删除用户标签映射对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysUserLabelByIds(String ids)
    {
        return sysUserLabelMapper.deleteSysUserLabelByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户标签映射信息
     * 
     * @param userId 用户标签映射ID
     * @return 结果
     */
    @Override
    public int deleteSysUserLabelById(Long userId)
    {
        return sysUserLabelMapper.deleteSysUserLabelById(userId);
    }

    /**
     * 逻辑删除用户标签映射对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteSysUserLabelByIds(String ids)
    {
        return sysUserLabelMapper.logicDeleteSysUserLabelByIds(Convert.toStrArray(ids));
    }

    /**
     * 逻辑删除用户标签映射信息
     *
     * @param userId 用户标签映射ID
     * @return 结果
     */
    public int logicDeleteSysUserLabelById(Long userId)
    {
        return sysUserLabelMapper.logicDeleteSysUserLabelById(userId);
    }

}
