package com.gsh.system.queryport.service.impl;

import com.gsh.common.constant.Constants;
import com.gsh.common.core.page.PageDomain;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.core.page.TableSupport;
import com.gsh.common.core.text.Convert;
import com.gsh.common.exception.BusinessException;
import com.gsh.common.utils.CacheUtils;
import com.gsh.common.utils.DateUtils;
import com.gsh.common.utils.StringUtils;
import com.gsh.system.domain.SysMenu;
import com.gsh.system.mapper.SysMenuMapper;
import com.gsh.system.mapper.SysRoleMenuMapper;
import com.gsh.system.queryport.domain.QueryportChart;
import com.gsh.system.queryport.domain.QueryportColumn;
import com.gsh.system.queryport.domain.QueryportMain;
import com.gsh.system.queryport.domain.QueryportParam;
import com.gsh.system.queryport.mapper.QueryportChartMapper;
import com.gsh.system.queryport.mapper.QueryportColumnMapper;
import com.gsh.system.queryport.mapper.QueryportMainMapper;
import com.gsh.system.queryport.mapper.QueryportParamMapper;
import com.gsh.system.queryport.service.IQueryportMainService;
import com.gsh.system.queryport.util.QueryPortMainUtil;
import com.gsh.system.service.ISysConfigService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询视图配置Service业务层处理
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
@Service
public class QueryportMainServiceImpl implements IQueryportMainService 
{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueryportMainMapper queryportMainMapper;

    @Autowired
    private QueryportChartMapper queryportChartMapper;

    @Autowired
    private QueryportParamMapper queryportParamMapper;

    @Autowired
    private QueryportColumnMapper queryportColumnMapper;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    private String displayMenuPerms = "system:queryport:display:%s";

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * 查询查询视图配置
     *
     * @param id 查询视图配置ID
     * @return 查询视图配置
     */
    @Override
    public QueryportMain selectQueryportMainById(Long id)
    {
        return queryportMainMapper.selectQueryportMainById(id);
    }

    /**
     * 查询查询视图配置
     *
     * @param mainId 查询视图配置ID
     * @return 查询视图配置
     */
    @Override
    //@Cacheable(value = "sys_queryport_main_cache", key = "#mainId", unless = "#result == null")
    public QueryportMain selectQueryportMainByMainId(Long mainId)
    {
        QueryportMain queryportMain = null;
        queryportMain = (QueryportMain) CacheUtils.get(getCacheName(), getCacheKey(String.valueOf(mainId)));
        if(StringUtils.isNotNull(queryportMain)){
            QueryportMain newQueryportMain = new QueryportMain();
            BeanUtils.copyProperties(queryportMain, newQueryportMain);
            return newQueryportMain;
        }
        queryportMain = queryportMainMapper.selectQueryportMainById(mainId);
        QueryportChart queryportChart = new QueryportChart();
        queryportChart.setMainId(mainId);
        queryportMain.setQueryportCharts(queryportChartMapper.selectQueryportChartList(queryportChart));
        QueryportParam queryportParam = new QueryportParam();
        queryportParam.setMainId(mainId);
        queryportMain.setQueryportParams(queryportParamMapper.selectQueryportParamList(queryportParam));
        QueryportColumn queryportColumn = new QueryportColumn();
        queryportColumn.setMainId(mainId);
        queryportMain.setQueryportColumns(queryportColumnMapper.selectQueryportColumnList(queryportColumn));
        Map<String,QueryportParam> paramMap = new HashMap<String,QueryportParam>(16);
        queryportMain.getQueryportParams().forEach(paramObject ->{
            paramMap.put(paramObject.getEname(), paramObject);
        });
        queryportMain.setQueryportParamsMap(paramMap);
        // 手动 加入缓存
        CacheUtils.put(getCacheName(), getCacheKey(String.valueOf(mainId)), queryportMain);
        return queryportMain;
    }

    /**
     * 查询查询视图配置列表
     * 
     * @param queryportMain 查询视图配置
     * @return 查询视图配置
     */
    @Override
    public List<QueryportMain> selectQueryportMainList(QueryportMain queryportMain)
    {
        return queryportMainMapper.selectQueryportMainList(queryportMain);
    }

    /**
     * 处理查询视图 权限资源
     *
     * @param queryportMain 查询视图配置
     * @return 结果
     */
    @Override
    public void handleQueryportMainMenuRes(QueryportMain queryportMain){
        SysMenu menu = new SysMenu();
        menu.setPerms(String.format(displayMenuPerms, String.valueOf(queryportMain.getId())));
        // 根据 是否验证权限 新增或者删除menu
        if("Y".equals(queryportMain.getIsPermitted())){
            if(sysMenuMapper.selectMenuByPerms(menu.getPerms()) == null) {
                menu.setParentId(2207L);
                menu.setMenuType("F");
                menu.setOrderNum(String.valueOf(queryportMain.getId()));
                menu.setVisible("0");
                menu.setMenuName(queryportMain.getName());
                sysMenuMapper.insertMenu(menu);
            }
        }else{
            removeQueryportMainMenuRerms(queryportMain);
        }
    }

    /**
     * 移除 查询视图权限配置
     *
     * @param queryportMain 查询视图配置
     * @return 结果
     */
    @Override
    public void removeQueryportMainMenuRerms(QueryportMain queryportMain){
        String perms = String.format(displayMenuPerms, String.valueOf(queryportMain.getId()));
        SysMenu sysMenu = sysMenuMapper.selectMenuByPerms(perms);
        if(sysMenu != null) {
            if (roleMenuMapper.selectCountRoleMenuByMenuId(sysMenu.getMenuId()) > 0){
                throw new BusinessException("此配置已分配权限，操作失败");
            }else {
                sysMenuMapper.deleteMenuByPerms(sysMenu.getPerms());
            }
        }
    }

    /**
     *
     * @param mainId 验证视图配置 权限
     * @return 结果
     */
    public void validateQueryportMainPerms(Long mainId){
        QueryportMain queryportMain = selectQueryportMainByMainId(mainId);
        String perms = String.format(displayMenuPerms, String.valueOf(mainId));
        //验证权限
        Boolean flag = SecurityUtils.getSubject().isPermitted(perms);
        if(!flag && "Y".equals(queryportMain.getIsPermitted())){
            throw new AuthorizationException(String.format("[%s]", queryportMain.getName()));
        }
    }


    /**
     * 新增查询视图配置
     * 
     * @param queryportMain 查询视图配置
     * @return 结果
     */
    @Override
    public int insertQueryportMain(QueryportMain queryportMain)
    {
        queryportMain.setCreateTime(DateUtils.getNowDate());
        QueryPortMainUtil.parserExpressionToMain(queryportMain);
        queryportMainMapper.insertQueryportMain(queryportMain);
        //handleQueryportMainMenuRes(queryportMain);
        return 0;
    }

    /**
     * 修改查询视图配置
     * 
     * @param queryportMain 查询视图配置
     * @return 结果
     */
    @Override
    //@CacheEvict(value = "sys_queryport_main_cache", key = "#queryportMain.id")
    public int updateQueryportMain(QueryportMain queryportMain)
    {
        CacheUtils.remove(getCacheName(), getCacheKey(String.valueOf(queryportMain.getId())));
        //handleQueryportMainMenuRes(queryportMain);
        queryportMain.setUpdateTime(DateUtils.getNowDate());
        if(StringUtils.isNotNull(queryportMain.getStatement())){
            QueryPortMainUtil.parserExpressionToMain(queryportMain);
        }
        return queryportMainMapper.updateQueryportMain(queryportMain);
    }

    /**
     * 删除查询视图配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteQueryportMainByIds(String ids)
    {
        return queryportMainMapper.deleteQueryportMainByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除查询视图配置信息
     * 
     * @param id 查询视图配置ID
     * @return 结果
     */
    @Override
    @CacheEvict(value = "sys_queryport_main_cache", key = "#id")
    public int deleteQueryportMainById(Long id)
    {
        return queryportMainMapper.deleteQueryportMainById(id);
    }

    /**
     * 逻辑删除查询视图配置对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteQueryportMainByIds(String ids)
    {
        String[] idArray = Convert.toStrArray(ids);
        Arrays.asList(idArray).forEach(mainId ->{
            logicDeleteQueryportMainById(Long.parseLong(mainId));
        });
        return idArray.length;
    }

    /**
     * 逻辑删除查询视图配置信息
     *
     * @param id 查询视图配置ID
     * @return 结果
     */
    @CacheEvict(value = "sys_queryport_main_cache", key = "#id")
    public int logicDeleteQueryportMainById(Long id)  {
        //removeQueryportMainMenuRerms(queryportMainMapper.selectQueryportMainById(id));
        queryportParamMapper.logicDeleteQueryportParamByMainId(id);
        //queryportChartMapper.logicDeleteQueryportChartByMainId(id);
        queryportColumnMapper.logicDeleteQueryportColumnByMainId(id);
        return queryportMainMapper.logicDeleteQueryportMainById(id);
    }

    /**
     * 不分页的查询方法
     * @param queryportMain
     * @return
     */
    @Override
    public TableDataInfo queryportMainForList(QueryportMain queryportMain) {
        //组织查询语句 和 条件
        StringBuffer queryStatement = new StringBuffer();
        List<Object> queryParamList = QueryPortMainUtil.dealQueryPortParameter(queryportMain, queryStatement);
        if(queryStatement.toString().toUpperCase().indexOf(" LIMIT ") == -1){
            queryStatement.append(" LIMIT 0," + sysConfigService.selectConfigByKey("sys.queryport.size.limit"));
        }
        log.info(queryStatement.toString());
        List<?> queryResultList = jdbcTemplate.queryForList(queryStatement.toString(),queryParamList.toArray());
        TableDataInfo rspData = new TableDataInfo(queryResultList, queryResultList.size());
        rspData.setCode(0);
        return rspData;
    }

    /**
     * queryport查询
     * @param queryportMain
     * @return
     */
    @Override
    public TableDataInfo queryportMainForPage(QueryportMain queryportMain) {
        if("N".equals(queryportMain.getPagination())){
            return queryportMainForList(queryportMain);
        }
        StringBuffer queryStatement = new StringBuffer();
        List<Object> queryParamList = QueryPortMainUtil.dealQueryPortParameter(queryportMain, queryStatement);
        log.info(queryStatement.toString());
        //获取记录条数
        String countStatement = "select count(1) as count from (" + queryStatement.toString() + ") temp";
        Long total = (Long) jdbcTemplate.queryForMap(countStatement, queryParamList.toArray()).get("count");
        //分页使用jdbc直接执行
        List<?> resultList = jdbcTemplate.queryForList(parseLimit(queryStatement).toString(), queryParamList.toArray());
        TableDataInfo rspData = new TableDataInfo(resultList, Integer.parseInt(total.toString()));
        rspData.setCode(0);
        return rspData;
    }

    /**
     * 追加分页参数
     * @param queryStatement
     * @return
     */
    private StringBuffer parseLimit(StringBuffer queryStatement){
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (pageDomain.getPageNum() == null) {
            pageDomain.setPageNum(0);
        }
        if (pageDomain.getPageSize() == null) {
            Integer pageSize = Integer.parseInt(sysConfigService.selectConfigByKey("sys.queryport.page.size"));
            pageDomain.setPageSize(pageSize);
        }
        queryStatement.append(" limit ");
        if(pageDomain.getPageNum() == 0) {
            queryStatement.append("0");
        } else {
            queryStatement.append((pageDomain.getPageNum()-1)*pageDomain.getPageSize());
        }
        queryStatement.append(" , ");
        queryStatement.append(pageDomain.getPageSize());
        return queryStatement;
    }



    /**
     * 获取cache name
     *
     * @return 缓存名
     */
    private String getCacheName()
    {
        return Constants.SYS_QUERYPORT_CACHE;
    }

    /**
     * 设置cache key
     *
     * @param key 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String key)
    {
        return Constants.SYS_QUERYPORT_KEY + key;
    }


}
