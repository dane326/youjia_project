package com.gsh.system.queryport.mapper;

import com.gsh.system.queryport.domain.QueryportMain;

import java.util.List;

/**
 * 查询视图配置Mapper接口
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public interface QueryportMainMapper 
{
    /**
     * 查询查询视图配置
     * 
     * @param id 查询视图配置ID
     * @return 查询视图配置
     */
    public QueryportMain selectQueryportMainById(Long id);

    /**
     * 查询查询视图配置列表
     * 
     * @param queryportMain 查询视图配置
     * @return 查询视图配置集合
     */
    public List<QueryportMain> selectQueryportMainList(QueryportMain queryportMain);

    /**
     * 新增查询视图配置
     * 
     * @param queryportMain 查询视图配置
     * @return 结果
     */
    public int insertQueryportMain(QueryportMain queryportMain);

    /**
     * 修改查询视图配置
     * 
     * @param queryportMain 查询视图配置
     * @return 结果
     */
    public int updateQueryportMain(QueryportMain queryportMain);

    /**
     * 删除查询视图配置
     *
     * @param id 查询视图配置ID
     * @return 结果
     */
    public int deleteQueryportMainById(Long id);

    /**
     * 批量删除查询视图配置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteQueryportMainByIds(String[] ids);

    /**
     *逻辑删除查询视图配置
     *
     * @param id 查询视图配置ID
     * @return 结果
     */
    public int logicDeleteQueryportMainById(Long id);

    /**
     * 批量逻辑删除查询视图配置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteQueryportMainByIds(String[] ids);
}
