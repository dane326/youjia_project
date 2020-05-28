package com.gsh.system.mapper;

import com.gsh.system.domain.SysDataTracesDetail;
import java.util.List;

/**
 * 数据追溯日志详情Mapper接口
 * 
 * @author gsh
 * @date 2019-11-20
 */
public interface SysDataTracesDetailMapper 
{
    /**
     * 查询数据追溯日志详情
     * 
     * @param id 数据追溯日志详情ID
     * @return 数据追溯日志详情
     */
    public SysDataTracesDetail selectSysDataTracesDetailById(Long id);

    /**
     * 查询数据追溯日志详情列表
     * 
     * @param sysDataTracesDetail 数据追溯日志详情
     * @return 数据追溯日志详情集合
     */
    public List<SysDataTracesDetail> selectSysDataTracesDetailList(SysDataTracesDetail sysDataTracesDetail);

    /**
     * 新增数据追溯日志详情
     * 
     * @param sysDataTracesDetail 数据追溯日志详情
     * @return 结果
     */
    public int insertSysDataTracesDetail(SysDataTracesDetail sysDataTracesDetail);

    /**
     * 修改数据追溯日志详情
     * 
     * @param sysDataTracesDetail 数据追溯日志详情
     * @return 结果
     */
    public int updateSysDataTracesDetail(SysDataTracesDetail sysDataTracesDetail);

    /**
     * 删除数据追溯日志详情
     *
     * @param id 数据追溯日志详情ID
     * @return 结果
     */
    public int deleteSysDataTracesDetailById(Long id);

    /**
     * 批量删除数据追溯日志详情
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDataTracesDetailByIds(String[] ids);

    /**
     *逻辑删除数据追溯日志详情
     *
     * @param id 数据追溯日志详情ID
     * @return 结果
     */
    public int logicDeleteSysDataTracesDetailById(Long id);

    /**
     * 批量逻辑删除数据追溯日志详情
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysDataTracesDetailByIds(String[] ids);
}
