package com.gsh.system.queryport.mapper;

import com.gsh.system.queryport.domain.QueryportColumn;

import java.util.List;

/**
 * 查询视图columnMapper接口
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public interface QueryportColumnMapper 
{
    /**
     * 查询查询视图column
     * 
     * @param id 查询视图columnID
     * @return 查询视图column
     */
    public QueryportColumn selectQueryportColumnById(Long id);

    /**
     * 查询查询视图column列表
     * 
     * @param queryportColumn 查询视图column
     * @return 查询视图column集合
     */
    public List<QueryportColumn> selectQueryportColumnList(QueryportColumn queryportColumn);

    /**
     * 新增查询视图column
     * 
     * @param queryportColumn 查询视图column
     * @return 结果
     */
    public int insertQueryportColumn(QueryportColumn queryportColumn);

    /**
     * 修改查询视图column
     * 
     * @param queryportColumn 查询视图column
     * @return 结果
     */
    public int updateQueryportColumn(QueryportColumn queryportColumn);

    /**
     * 删除查询视图column
     *
     * @param id 查询视图columnID
     * @return 结果
     */
    public int deleteQueryportColumnById(Long id);

    /**
     * 批量删除查询视图column
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteQueryportColumnByIds(String[] ids);

    /**
     *逻辑删除查询视图column
     *
     * @param id 查询视图columnID
     * @return 结果
     */
    public int logicDeleteQueryportColumnById(Long id);

    /**
     * 批量逻辑删除查询视图column
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteQueryportColumnByIds(String[] ids);

    /**
     *逻辑删除查询视图column
     *
     * @param mainId 查询视图columnID
     * @return 结果
     */
    public void logicDeleteQueryportColumnByMainId(Long mainId);
}
