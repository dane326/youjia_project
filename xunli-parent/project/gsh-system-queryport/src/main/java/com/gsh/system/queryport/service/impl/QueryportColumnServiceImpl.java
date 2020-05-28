package com.gsh.system.queryport.service.impl;

import java.util.List;
import com.gsh.common.utils.DateUtils;
import com.gsh.common.utils.StringUtils;
import com.gsh.system.queryport.domain.QueryportMain;
import com.gsh.system.queryport.util.QueryPortMainUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import com.gsh.system.queryport.mapper.QueryportColumnMapper;
import com.gsh.system.queryport.domain.QueryportColumn;
import com.gsh.system.queryport.service.IQueryportColumnService;
import com.gsh.common.core.text.Convert;

/**
 * 查询视图columnService业务层处理
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
@Service
public class QueryportColumnServiceImpl implements IQueryportColumnService 
{
    @Autowired
    private QueryportColumnMapper queryportColumnMapper;

    /**
     * 查询查询视图column
     * 
     * @param id 查询视图columnID
     * @return 查询视图column
     */
    @Override
    public QueryportColumn selectQueryportColumnById(Long id)
    {
        return queryportColumnMapper.selectQueryportColumnById(id);
    }

    /**
     * 查询查询视图column列表
     *
     * @param queryportColumn 查询视图column
     * @return 查询视图column
     */
    @Override
    public List<QueryportColumn> selectQueryportColumnList(QueryportColumn queryportColumn)
    {
        return queryportColumnMapper.selectQueryportColumnList(queryportColumn);
    }

    /**
     * 查询查询视图column列表
     *
     * @param mainId 查询视图column
     * @return 查询视图column
     */
    @Override
    public List<QueryportColumn> selectQueryportColumnList(Long mainId)
    {
        QueryportColumn queryportColumn = new QueryportColumn();
        queryportColumn.setMainId(mainId);
        return queryportColumnMapper.selectQueryportColumnList(queryportColumn);
    }

    /**
     * 新增查询视图column
     *
     * @param queryportColumn 查询视图column
     * @return 结果
     */
    @Override
    public int insertQueryportColumn(QueryportColumn queryportColumn)
    {
        queryportColumn.setCreateTime(DateUtils.getNowDate());
        return queryportColumnMapper.insertQueryportColumn(queryportColumn);
    }

    /**
     * 新增查询视图column
     *
     * @param queryportMain 查询视图column
     * @return 结果
     */
    @Override
    public int insertQueryportColumn(QueryportMain queryportMain)
    {
        try {
            //解析语句
            List<String> columnList = QueryPortMainUtil.parserColumn(queryportMain.getStatement());
            //save column
            int num = 0;
            QueryportColumn queryportColumn = null;
            for(String column:columnList){
                queryportColumn = new QueryportColumn();
                queryportColumn.setMainId(queryportMain.getId());
                queryportColumn.setEname(column);
                queryportColumn.setCname(column);
                queryportColumn.setDisplayOrder(num++);
                queryportColumn.setViewFlag("Y");
                queryportColumnMapper.insertQueryportColumn(queryportColumn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 修改查询视图column
     *
     * @param queryportColumn 查询视图column
     * @return 结果
     */
    @Override
    public int updateQueryportColumn(QueryportColumn queryportColumn)
    {
        queryportColumn.setUpdateTime(DateUtils.getNowDate());
        return queryportColumnMapper.updateQueryportColumn(queryportColumn);
    }

    /**
     * 修改查询视图column
     *
     * @param queryportMain 查询视图column
     * @return 结果
     */
    @Override
    public int updateQueryportColumn(QueryportMain queryportMain) throws Exception {
        List<String> columnList = QueryPortMainUtil.parserColumn(queryportMain.getStatement());
        List<QueryportColumn> queryportColumnList = selectQueryportColumnList(queryportMain.getId());
        //删除未匹配到的记录
        for(QueryportColumn c : queryportColumnList){
            if(!columnList.contains(c.getEname())){
                queryportColumnMapper.deleteQueryportColumnById(c.getId());
            }
        }
        //save column 
        for(String column:columnList){
            if(validateColumnNotExist(queryportMain.getId(),column)){
                QueryportColumn queryportColumn = new QueryportColumn();
                queryportColumn.setMainId(queryportMain.getId());
                queryportColumn.setEname(column);
                queryportColumn.setCname(column);
                queryportColumn.setViewFlag("Y");
                queryportColumnMapper.insertQueryportColumn(queryportColumn);
            }
        }
        return 1;
    }



    /**
     * 判断解析的列，是否已经存在
     * @param id
     * @param column
     * @return
     */
    public boolean validateColumnNotExist(Long id, String column) {
        List<QueryportColumn> columnList = selectQueryportColumnList(id);
        for(QueryportColumn c : columnList){
            if(column.equals(c.getEname())){
                return false;
            }
        }
        return true;
    }

    /**
     * 删除查询视图column对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteQueryportColumnByIds(String ids)
    {
        return queryportColumnMapper.deleteQueryportColumnByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除查询视图column信息
     * 
     * @param id 查询视图columnID
     * @return 结果
     */
    @Override
    public int deleteQueryportColumnById(Long id)
    {
        return queryportColumnMapper.deleteQueryportColumnById(id);
    }

    /**
     * 逻辑删除查询视图column对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteQueryportColumnByIds(String ids)
    {
        return queryportColumnMapper.logicDeleteQueryportColumnByIds(Convert.toStrArray(ids));
    }

    /**
     * 逻辑删除查询视图column信息
     *
     * @param id 查询视图columnID
     * @return 结果
     */
    public int logicDeleteQueryportColumnById(Long id)
    {
        return queryportColumnMapper.logicDeleteQueryportColumnById(id);
    }

}
