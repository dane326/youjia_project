package com.gsh.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gsh.system.mapper.SysDataTracesDetailMapper;
import com.gsh.system.domain.SysDataTracesDetail;
import com.gsh.system.service.ISysDataTracesDetailService;
import com.gsh.common.core.text.Convert;

/**
 * 数据追溯日志详情Service业务层处理
 * 
 * @author gsh
 * @date 2019-11-20
 */
@Service
public class SysDataTracesDetailServiceImpl implements ISysDataTracesDetailService 
{
    @Autowired
    private SysDataTracesDetailMapper sysDataTracesDetailMapper;

    /**
     * 查询数据追溯日志详情
     * 
     * @param id 数据追溯日志详情ID
     * @return 数据追溯日志详情
     */
    @Override
    public SysDataTracesDetail selectSysDataTracesDetailById(Long id)
    {
        return sysDataTracesDetailMapper.selectSysDataTracesDetailById(id);
    }

    /**
     * 查询数据追溯日志详情列表
     * 
     * @param sysDataTracesDetail 数据追溯日志详情
     * @return 数据追溯日志详情
     */
    @Override
    public List<SysDataTracesDetail> selectSysDataTracesDetailList(SysDataTracesDetail sysDataTracesDetail)
    {
        return sysDataTracesDetailMapper.selectSysDataTracesDetailList(sysDataTracesDetail);
    }

    /**
     * 新增数据追溯日志详情
     * 
     * @param sysDataTracesDetail 数据追溯日志详情
     * @return 结果
     */
    @Override
    public int insertSysDataTracesDetail(SysDataTracesDetail sysDataTracesDetail)
    {
        return sysDataTracesDetailMapper.insertSysDataTracesDetail(sysDataTracesDetail);
    }

    /**
     * 修改数据追溯日志详情
     * 
     * @param sysDataTracesDetail 数据追溯日志详情
     * @return 结果
     */
    @Override
    public int updateSysDataTracesDetail(SysDataTracesDetail sysDataTracesDetail)
    {
        return sysDataTracesDetailMapper.updateSysDataTracesDetail(sysDataTracesDetail);
    }

    /**
     * 删除数据追溯日志详情对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysDataTracesDetailByIds(String ids)
    {
        return sysDataTracesDetailMapper.deleteSysDataTracesDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除数据追溯日志详情信息
     * 
     * @param id 数据追溯日志详情ID
     * @return 结果
     */
    public int deleteSysDataTracesDetailById(Long id)
    {
        return sysDataTracesDetailMapper.deleteSysDataTracesDetailById(id);
    }

    /**
     * 逻辑删除数据追溯日志详情对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteSysDataTracesDetailByIds(String ids)
    {
        return sysDataTracesDetailMapper.logicDeleteSysDataTracesDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * 逻辑删除数据追溯日志详情信息
     *
     * @param id 数据追溯日志详情ID
     * @return 结果
     */
    public int logicDeleteSysDataTracesDetailById(Long id)
    {
        return sysDataTracesDetailMapper.logicDeleteSysDataTracesDetailById(id);
    }

}
