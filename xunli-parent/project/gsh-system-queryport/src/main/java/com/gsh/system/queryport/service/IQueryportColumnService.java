package com.gsh.system.queryport.service;

import com.gsh.system.queryport.domain.QueryportColumn;
import com.gsh.system.queryport.domain.QueryportMain;

import java.util.List;

/**
 * 查询视图columnService接口
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public interface IQueryportColumnService 
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
     * 查询查询视图column列表
     *
     * @param mainId 查询视图column
     * @return 查询视图column集合
     */
    public List<QueryportColumn> selectQueryportColumnList(Long mainId);

    /**
     * 新增查询视图column
     *
     * @param queryportColumn 查询视图column
     * @return 结果
     */
    public int insertQueryportColumn(QueryportColumn queryportColumn);

    /**
     * 新增查询视图column
     *
     * @param queryportMain 查询视图column
     * @return 结果
     */
    public int insertQueryportColumn(QueryportMain queryportMain);

    /**
     * 修改查询视图column
     *
     * @param queryportColumn 查询视图column
     * @return 结果
     */
    public int updateQueryportColumn(QueryportColumn queryportColumn);

    /**
     * 修改查询视图column
     *
     * @param queryportMain 查询视图column
     * @return 结果
     */
    public int updateQueryportColumn(QueryportMain queryportMain) throws Exception;

    /**
     * 批量删除查询视图column
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteQueryportColumnByIds(String ids);

    /**
     * 删除查询视图column信息
     * 
     * @param id 查询视图columnID
     * @return 结果
     */
    public int deleteQueryportColumnById(Long id);
    /**
     * 批量逻辑删除查询视图column
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteQueryportColumnByIds(String ids);

    /**
     * 逻辑删除查询视图column信息
     *
     * @param id 查询视图columnID
     * @return 结果
     */
    public int logicDeleteQueryportColumnById(Long id);

}
