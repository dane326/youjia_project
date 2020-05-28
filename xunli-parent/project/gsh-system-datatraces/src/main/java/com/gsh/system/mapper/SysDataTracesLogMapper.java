package com.gsh.system.mapper;

import com.gsh.system.domain.SysDataTracesLog;
import java.util.List;

/**
 * 数据追溯日志Mapper接口
 * 
 * @author gsh
 * @date 2019-11-20
 */
public interface SysDataTracesLogMapper 
{
    /**
     * 查询数据追溯日志
     * 
     * @param id 数据追溯日志ID
     * @return 数据追溯日志
     */
    public SysDataTracesLog selectSysDataTracesLogById(Long id);

    /**
     * 查询数据追溯日志列表
     * 
     * @param sysDataTracesLog 数据追溯日志
     * @return 数据追溯日志集合
     */
    public List<SysDataTracesLog> selectSysDataTracesLogList(SysDataTracesLog sysDataTracesLog);

    /**
     * 查询数据追溯日志列表
     *
     * @param opObjectName 操作对象
     * @param opObjectId 操作对象ID
     * @return 数据追溯日志集合
     */
    public List<SysDataTracesLog> selectSysDataTracesLogList(String opObjectName, Long opObjectId);

    /**
     * 新增数据追溯日志
     * 
     * @param sysDataTracesLog 数据追溯日志
     * @return 结果
     */
    public int insertSysDataTracesLog(SysDataTracesLog sysDataTracesLog);

    /**
     * 修改数据追溯日志
     * 
     * @param sysDataTracesLog 数据追溯日志
     * @return 结果
     */
    public int updateSysDataTracesLog(SysDataTracesLog sysDataTracesLog);

    /**
     * 删除数据追溯日志
     *
     * @param id 数据追溯日志ID
     * @return 结果
     */
    public int deleteSysDataTracesLogById(Long id);

    /**
     * 批量删除数据追溯日志
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDataTracesLogByIds(String[] ids);

    /**
     *逻辑删除数据追溯日志
     *
     * @param id 数据追溯日志ID
     * @return 结果
     */
    public int logicDeleteSysDataTracesLogById(Long id);

    /**
     * 批量逻辑删除数据追溯日志
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysDataTracesLogByIds(String[] ids);
}
