package com.gsh.system.queryport.service.impl;

import java.util.List;
import com.gsh.common.utils.DateUtils;
import com.gsh.system.queryport.domain.QueryportMain;
import com.gsh.system.queryport.domain.QueryportParam;
import com.gsh.system.queryport.mapper.QueryportParamMapper;
import com.gsh.system.queryport.service.IQueryportParamService;
import com.gsh.system.queryport.util.QueryPortMainUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import com.gsh.common.core.text.Convert;

/**
 * 查询视图参数Service业务层处理
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
@Service
public class QueryportParamServiceImpl implements IQueryportParamService
{
    @Autowired
    private QueryportParamMapper queryportParamMapper;

    /**
     * 查询查询视图参数
     * 
     * @param id 查询视图参数ID
     * @return 查询视图参数
     */
    @Override
    public QueryportParam selectQueryportParamById(Long id)
    {
        return queryportParamMapper.selectQueryportParamById(id);
    }

    /**
     * 查询查询视图参数列表
     *
     * @param queryportParam 查询视图参数
     * @return 查询视图参数
     */
    @Override
    public List<QueryportParam> selectQueryportParamList(QueryportParam queryportParam)
    {
        return queryportParamMapper.selectQueryportParamList(queryportParam);
    }

    /**
     * 查询查询视图参数列表
     *
     * @param mainId 查询视图参数
     * @return 查询视图参数
     */
    @Override
    public List<QueryportParam> selectQueryportParamList(Long mainId)
    {
        QueryportParam queryportParam = new QueryportParam();
        queryportParam.setMainId(mainId);
        return queryportParamMapper.selectQueryportParamList(queryportParam);
    }

    /**
     * 新增查询视图参数
     *
     * @param queryportParam 查询视图参数
     * @return 结果
     */
    @Override
    public int insertQueryportParam(QueryportParam queryportParam)
    {
        queryportParam.setCreateTime(DateUtils.getNowDate());
        return queryportParamMapper.insertQueryportParam(queryportParam);
    }

    /**
     * 新增查询视图参数
     *
     * @param queryportMain 查询视图参数
     * @return 结果
     */
    @Override
    public int insertQueryportParam(QueryportMain queryportMain)
    {
        //解析语句
        List<String> paramList = QueryPortMainUtil.parserParam(queryportMain.getStatement(), QueryPortMainUtil.PARAM_REGX, false);
        //save param
        int num = 0;
        QueryportParam queryportParam = null;
        for(String param:paramList){
            queryportParam = new QueryportParam();
            queryportParam.setMainId(queryportMain.getId());
            queryportParam.setEname(param);
            queryportParam.setCname(param);
            queryportParam.setDisplayOrder(num++);
            queryportParam.setCreateTime(DateUtils.getNowDate());
            queryportParam.setParamType("text");
            queryportParam.setViewFlag("Y");
            queryportParamMapper.insertQueryportParam(queryportParam);
        }
        return 1;
    }

    /**
     * 修改查询视图参数
     *
     * @param queryportParam 查询视图参数
     * @return 结果
     */
    @Override
    public int updateQueryportParam(QueryportParam queryportParam)
    {
        return queryportParamMapper.updateQueryportParam(queryportParam);
    }

    /**
     * 修改查询视图参数
     * 
     * @param queryportMain 查询视图参数
     * @return 结果
     */
    @Override
    public int updateQueryportParam(QueryportMain queryportMain)
    {
        //解析语句
        List<String> paramList = QueryPortMainUtil.parserParam(queryportMain.getStatement(), QueryPortMainUtil.PARAM_REGX, false);
        List<QueryportParam> queryportParamList = selectQueryportParamList(queryportMain.getId());
        //删除未匹配到的记录
        for(QueryportParam p : queryportParamList){
            if(!paramList.contains(p.getEname())){
                queryportParamMapper.deleteQueryportParamById(p.getId());
            }
        }
        //save param
        for(String param:paramList){
            if(validateParamNotExist(queryportMain.getId(),param)){
                QueryportParam queryportParam = new QueryportParam();
                queryportParam.setMainId(queryportMain.getId());
                queryportParam.setEname(param);
                queryportParam.setCname(param);
                queryportParamMapper.insertQueryportParam(queryportParam);
            }
        }
        return 1;
    }

    /**
     * 判断解析的参数，是否已经存在
     * @param mainId
     * @param param
     * @return
     */
    public boolean validateParamNotExist(Long mainId, String param) {
        List<QueryportParam> paramList = selectQueryportParamList(mainId);
        for(QueryportParam p : paramList){
            if(param.equals(p.getEname())){
                return false;
            }
        }
        return true;
    }

    /**
     * 删除查询视图参数对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteQueryportParamByIds(String ids)
    {
        return queryportParamMapper.deleteQueryportParamByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除查询视图参数信息
     * 
     * @param id 查询视图参数ID
     * @return 结果
     */
    @Override
    public int deleteQueryportParamById(Long id)
    {
        return queryportParamMapper.deleteQueryportParamById(id);
    }

    /**
     * 逻辑删除查询视图参数对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteQueryportParamByIds(String ids)
    {
        return queryportParamMapper.logicDeleteQueryportParamByIds(Convert.toStrArray(ids));
    }

    /**
     * 逻辑删除查询视图参数信息
     *
     * @param id 查询视图参数ID
     * @return 结果
     */
    public int logicDeleteQueryportParamById(Long id)
    {
        return queryportParamMapper.logicDeleteQueryportParamById(id);
    }

}
