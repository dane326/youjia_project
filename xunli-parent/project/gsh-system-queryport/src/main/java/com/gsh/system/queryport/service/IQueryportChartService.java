package com.gsh.system.queryport.service;


import com.gsh.system.queryport.domain.QueryportChart;
import com.gsh.system.queryport.domain.QueryportMain;
import org.apache.commons.collections.map.ListOrderedMap;

import java.util.List;

/**
 * 查询视图columnService接口
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public interface IQueryportChartService
{


    /**
     * 查询查询视图column列表
     *
     * @param queryportChart 查询视图column
     * @return 查询视图column集合
     */
    public List<QueryportChart> selectQueryportChartList(QueryportChart queryportChart);

    /**
     * findQueryportChartByMainid
     * @param mainId
     * @return
     */
    public List<QueryportChart> selectQueryportChartByMainId(Long mainId);

    /**
     * findQueryportChartByMainidNoCache
     * @param mainid
     * @return
     */
    public List<QueryportChart> selectQueryportChartByMainidNoCache(Long mainid);


    /**
     * 复制
     * @param id
     * @param newQueryportMain
     * @Exception
     */
    public void copyPnrQueryportChartByMain(String id, QueryportMain newQueryportMain) throws Exception;

    /**
     * getChartOptionsList
     * @param QueryportMain
     * @param resList
     * @return
     */
    public Object getChartOptionsList(QueryportMain QueryportMain, List<ListOrderedMap> resList);

    /**
     * 修改查询视图参数
     *
     * @param queryportChart 查询视图参数
     * @return 结果
     */
    public int updateQueryportChart(QueryportChart queryportChart);

    /**
     * 新增视图chart
     *
     * @param queryportChart 新增视图chart
     * @return 结果
     */
    public int insertOrUpdateQueryportChart(QueryportChart queryportChart);

    /**
     * 新增视图chart
     *
     * @param queryportChart 新增视图chart
     * @return 结果
     */
    public int insertQueryportChart(QueryportChart queryportChart);
    /**
     * 新增图片配置chart
     * @param queryportMain 视图配置chart
     * @return 结果
     */
    public int insertQueryportChart(QueryportMain queryportMain);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    public int deleteQueryportChartByIds(String ids);


}
