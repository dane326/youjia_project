package com.gsh.system.queryport.mapper;


import com.gsh.system.queryport.domain.QueryportParam;

import java.util.List;

/**
 * 查询视图参数Mapper接口
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public interface QueryportParamMapper 
{
    /**
     * 查询查询视图参数
     * 
     * @param id 查询视图参数ID
     * @return 查询视图参数
     */
    public QueryportParam selectQueryportParamById(Long id);

    /**
     * 查询查询视图参数列表
     * 
     * @param queryportParam 查询视图参数
     * @return 查询视图参数集合
     */
    public List<QueryportParam> selectQueryportParamList(QueryportParam queryportParam);

    /**
     * 新增查询视图参数
     * 
     * @param queryportParam 查询视图参数
     * @return 结果
     */
    public int insertQueryportParam(QueryportParam queryportParam);

    /**
     * 修改查询视图参数
     * 
     * @param queryportParam 查询视图参数
     * @return 结果
     */
    public int updateQueryportParam(QueryportParam queryportParam);

    /**
     * 删除查询视图参数
     *
     * @param id 查询视图参数ID
     * @return 结果
     */
    public int deleteQueryportParamById(Long id);

    /**
     * 批量删除查询视图参数
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteQueryportParamByIds(String[] ids);

    /**
     *逻辑删除查询视图参数
     *
     * @param id 查询视图参数ID
     * @return 结果
     */
    public int logicDeleteQueryportParamById(Long id);

    /**
     * 批量逻辑删除查询视图参数
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteQueryportParamByIds(String[] ids);

    /**
     *逻辑删除查询视图参数
     *
     * @param mainId 查询视图参数ID
     * @return 结果
     */
    public void logicDeleteQueryportParamByMainId(Long mainId);
}
