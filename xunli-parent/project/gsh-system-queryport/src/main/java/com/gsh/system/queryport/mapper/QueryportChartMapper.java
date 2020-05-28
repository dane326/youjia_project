package com.gsh.system.queryport.mapper;


import com.gsh.system.queryport.domain.QueryportChart;

import java.util.List;

/**
 * 查询视图columnMapper接口
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public interface QueryportChartMapper
{
    /**
     * 查询查询视图chart列表
     *
     * @param queryportChart 查询视图chart
     * @return 查询视图column集合
     */
    public List<QueryportChart> selectQueryportChartList(QueryportChart queryportChart);

    /**
     * findQueryportChartByMainidNoCache
     * @param mainid
     * @return
     */
    public List<QueryportChart> selectQueryportChartByMainidNoCache(Long mainid);

    /**
     * 修改查询视图chart
     *
     * @param queryportChart 查询视图column
     * @return 结果
     */
    public int updateQueryportChart(QueryportChart queryportChart);

    /**
     * 新增视图chart
     *
     * @param queryportChart 新增视图chart
     * @return 结果
     */
    public int insertQueryportChart(QueryportChart queryportChart);

     /**
     * 批量删除
     * @param ids
     * @return
     */
    public int deleteQueryportChartByIds(String[] ids);


}
