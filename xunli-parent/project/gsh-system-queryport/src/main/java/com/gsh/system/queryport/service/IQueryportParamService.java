package com.gsh.system.queryport.service;

import com.gsh.system.queryport.domain.QueryportMain;
import com.gsh.system.queryport.domain.QueryportParam;

import java.util.List;

/**
 * 查询视图参数Service接口
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public interface IQueryportParamService 
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
     * 查询查询视图参数列表
     *
     * @param mainId 查询视图参数
     * @return 查询视图参数集合
     */
    public List<QueryportParam> selectQueryportParamList(Long mainId);

    /**
     * 新增查询视图参数
     *
     * @param queryportParam 查询视图参数
     * @return 结果
     */
    public int insertQueryportParam(QueryportParam queryportParam);

    /**
     * 新增查询视图参数
     *
     * @param queryportMain 查询视图参数
     * @return 结果
     */
    public int insertQueryportParam(QueryportMain queryportMain);

    /**
     * 修改查询视图参数
     *
     * @param queryportParam 查询视图参数
     * @return 结果
     */
    public int updateQueryportParam(QueryportParam queryportParam);

    /**
     * 修改查询视图参数
     *
     * @param queryportMain 查询视图参数
     * @return 结果
     */
    public int updateQueryportParam(QueryportMain queryportMain);

    /**
     * 批量删除查询视图参数
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteQueryportParamByIds(String ids);

    /**
     * 删除查询视图参数信息
     * 
     * @param id 查询视图参数ID
     * @return 结果
     */
    public int deleteQueryportParamById(Long id);
    /**
     * 批量逻辑删除查询视图参数
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteQueryportParamByIds(String ids);

    /**
     * 逻辑删除查询视图参数信息
     *
     * @param id 查询视图参数ID
     * @return 结果
     */
    public int logicDeleteQueryportParamById(Long id);
}
