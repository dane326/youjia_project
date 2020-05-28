package com.gsh.system.service.impl;

import java.util.List;
import com.gsh.common.utils.DateUtils;
import com.gsh.system.domain.SysDataTracesDetail;
import com.gsh.system.mapper.SysDataTracesDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gsh.system.mapper.SysDataTracesLogMapper;
import com.gsh.system.domain.SysDataTracesLog;
import com.gsh.system.service.ISysDataTracesLogService;
import com.gsh.common.core.text.Convert;

/**
 * 数据追溯日志Service业务层处理
 * 
 * @author gsh
 * @date 2019-11-20
 */
@Service
public class SysDataTracesLogServiceImpl implements ISysDataTracesLogService 
{
    @Autowired
    private SysDataTracesLogMapper sysDataTracesLogMapper;

    @Autowired
    private SysDataTracesDetailMapper sysDataTracesDetailMapper;

    /**
     * 查询数据追溯日志
     * 
     * @param id 数据追溯日志ID
     * @return 数据追溯日志
     */
    @Override
    public SysDataTracesLog selectSysDataTracesLogById(Long id)
    {
        return sysDataTracesLogMapper.selectSysDataTracesLogById(id);
    }

    /**
     * 查询数据追溯日志列表
     * 
     * @param sysDataTracesLog 数据追溯日志
     * @return 数据追溯日志
     */
    @Override
    public List<SysDataTracesLog> selectSysDataTracesLogList(SysDataTracesLog sysDataTracesLog)
    {
        return sysDataTracesLogMapper.selectSysDataTracesLogList(sysDataTracesLog);
    }

    /**
     * 查询数据追溯日志列表
     *
     * @param opObjectName 操作对象
     * @param opObjectId 操作对象ID
     * @return 数据追溯日志集合
     */
    @Override
    public List<SysDataTracesLog> selectSysDataTracesLogList(String opObjectName, Long opObjectId){
        return sysDataTracesLogMapper.selectSysDataTracesLogList(opObjectName, opObjectId);
    }

    /**
     * 新增数据追溯日志
     * 
     * @param sysDataTracesLog 数据追溯日志
     * @return 结果
     */
    @Override
    public int insertSysDataTracesLog(SysDataTracesLog sysDataTracesLog)
    {
        sysDataTracesLog.setCreateTime(DateUtils.getNowDate());
        return sysDataTracesLogMapper.insertSysDataTracesLog(sysDataTracesLog);
    }
    /**
     * 新增数据追溯日志
     *
     * @param sysDataTracesLog 数据追溯日志
     * @param sysDataTracesDetails 数据追溯日志详情
     * @return 结果
     */
    public int insertSysDataTracesLog(SysDataTracesLog sysDataTracesLog,List<SysDataTracesDetail> sysDataTracesDetails){
        sysDataTracesLog.setCreateTime(DateUtils.getNowDate());
        int rows = sysDataTracesLogMapper.insertSysDataTracesLog(sysDataTracesLog);
        sysDataTracesDetails.forEach((sysDataTracesDetail)->{
            sysDataTracesDetail.setDatatracesId(sysDataTracesLog.getId());
            sysDataTracesDetailMapper.insertSysDataTracesDetail(sysDataTracesDetail);
        });
        return rows;
    }


    /**
     * 修改数据追溯日志
     * 
     * @param sysDataTracesLog 数据追溯日志
     * @return 结果
     */
    @Override
    public int updateSysDataTracesLog(SysDataTracesLog sysDataTracesLog)
    {
        sysDataTracesLog.setUpdateTime(DateUtils.getNowDate());
        return sysDataTracesLogMapper.updateSysDataTracesLog(sysDataTracesLog);
    }

    /**
     * 删除数据追溯日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysDataTracesLogByIds(String ids)
    {
        return sysDataTracesLogMapper.deleteSysDataTracesLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除数据追溯日志信息
     * 
     * @param id 数据追溯日志ID
     * @return 结果
     */
    public int deleteSysDataTracesLogById(Long id)
    {
        return sysDataTracesLogMapper.deleteSysDataTracesLogById(id);
    }

    /**
     * 逻辑删除数据追溯日志对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteSysDataTracesLogByIds(String ids)
    {
        return sysDataTracesLogMapper.logicDeleteSysDataTracesLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 逻辑删除数据追溯日志信息
     *
     * @param id 数据追溯日志ID
     * @return 结果
     */
    public int logicDeleteSysDataTracesLogById(Long id)
    {
        return sysDataTracesLogMapper.logicDeleteSysDataTracesLogById(id);
    }

}
