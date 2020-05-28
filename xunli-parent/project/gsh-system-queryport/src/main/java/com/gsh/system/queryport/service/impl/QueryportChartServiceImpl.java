package com.gsh.system.queryport.service.impl;

import com.gsh.common.core.text.Convert;
import com.gsh.system.queryport.domain.QueryportChart;
import com.gsh.system.queryport.domain.QueryportMain;
import com.gsh.system.queryport.mapper.QueryportChartMapper;
import com.gsh.system.queryport.service.IQueryportChartService;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询视图columnService业务层处理
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
@Service
public class QueryportChartServiceImpl implements IQueryportChartService
{
    @Autowired
    private QueryportChartMapper queryportChartMapper;


    @Override
    public List<QueryportChart> selectQueryportChartByMainId(Long mainId) {
        QueryportChart queryportChart = new QueryportChart();
        queryportChart.setMainId(mainId);
        return queryportChartMapper.selectQueryportChartList(queryportChart);
    }

    @Override
    public List<QueryportChart> selectQueryportChartByMainidNoCache(Long mainid) {
        return queryportChartMapper.selectQueryportChartByMainidNoCache(mainid);
    }

    /**
     * 查询查询视图chart列表
     *
     * @param queryportChart 查询视图chart
     * @return 查询视图chart
     */
    @Override
    public List<QueryportChart> selectQueryportChartList(QueryportChart queryportChart)
    {
        return queryportChartMapper.selectQueryportChartList(queryportChart);
    }




    @Override
    public void copyPnrQueryportChartByMain(String id, QueryportMain newQueryportMain) throws Exception {

    }

    @Override
    public Object getChartOptionsList(QueryportMain QueryportMain, List<ListOrderedMap> resList) {
        return null;
    }

    @Override
    public int updateQueryportChart(QueryportChart queryportChart) {
        return queryportChartMapper.updateQueryportChart(queryportChart);
    }

    @Override
    public int insertQueryportChart(QueryportChart queryportChart) {
        return queryportChartMapper.insertQueryportChart(queryportChart);
    }

    @Override
    public int insertOrUpdateQueryportChart(QueryportChart queryportChart) {
        QueryportChart newQueryportChart = new QueryportChart();
        newQueryportChart.setMainId(queryportChart.getMainId());
        List<QueryportChart> queryportChartList =queryportChartMapper.selectQueryportChartList(newQueryportChart);
        if(queryportChartList.size()>0){
            return queryportChartMapper.updateQueryportChart(queryportChart);
        }else{
            return queryportChartMapper.insertQueryportChart(queryportChart);
        }
    }

    @Override
    public int insertQueryportChart(QueryportMain queryportMain) {
        QueryportChart queryportChart = new QueryportChart();
        //mainId
        queryportChart.setMainId(queryportMain.getId());
        //名称
        queryportChart.setTitle(queryportMain.getName());
        //高度
        queryportChart.setHeight("450px");
        return queryportChartMapper.insertQueryportChart(queryportChart);
    }

     /**
     * 批量删除
     * @param ids
     * @return
     */
     @Override
    public int deleteQueryportChartByIds(String ids){
         return queryportChartMapper.deleteQueryportChartByIds(Convert.toStrArray(ids));
     }
}
