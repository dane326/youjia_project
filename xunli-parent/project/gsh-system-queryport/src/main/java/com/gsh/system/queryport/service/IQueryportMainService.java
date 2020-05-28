package com.gsh.system.queryport.service;

import com.gsh.common.core.page.TableDataInfo;
import com.gsh.system.queryport.domain.QueryportMain;

import java.util.List;

/**
 * 查询视图配置Service接口
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public interface IQueryportMainService 
{
    /**
     * 查询查询视图配置
     * 
     * @param id 查询视图配置ID
     * @return 查询视图配置
     */
    public QueryportMain selectQueryportMainById(Long id);

    /**
     * 查询查询视图配置
     *
     * @param mainId 查询视图配置ID
     * @return 查询视图配置
     */
    public QueryportMain selectQueryportMainByMainId(Long mainId);

    /**
     * 查询查询视图配置列表
     * 
     * @param queryportMain 查询视图配置
     * @return 查询视图配置集合
     */
    public List<QueryportMain> selectQueryportMainList(QueryportMain queryportMain);

    /**
     *
     * @param queryportMain 查询视图配置
     * @return 结果
     */
    public void handleQueryportMainMenuRes(QueryportMain queryportMain);

    /**
     * 移除 查询视图权限配置
     *
     * @param queryportMain 查询视图配置
     * @return 结果
     */
    public void removeQueryportMainMenuRerms(QueryportMain queryportMain);

    /**
     *
     * @param mainId 验证视图配置 权限
     * @return 结果
     */
    public void validateQueryportMainPerms(Long mainId);

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
     * 批量删除查询视图配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteQueryportMainByIds(String ids);

    /**
     * 删除查询视图配置信息
     * 
     * @param id 查询视图配置ID
     * @return 结果
     */
    public int deleteQueryportMainById(Long id);
    /**
     * 批量逻辑删除查询视图配置
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteQueryportMainByIds(String ids);

    /**
     * 逻辑删除查询视图配置信息
     *
     * @param id 查询视图配置ID
     * @return 结果
     */
    public int logicDeleteQueryportMainById(Long id);

    /**
     * queryportMainForList
     *
     * @param queryportMain
     * @return 结果
     */
    public TableDataInfo queryportMainForList(QueryportMain queryportMain);

    /**
     * queryportMainForList
     *
     * @param queryportMain
     * @return 结果
     */
    public TableDataInfo queryportMainForPage(QueryportMain queryportMain);
}
