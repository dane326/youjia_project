package com.gsh.system.queryport.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 查询视图配置对象 sys_queryport_main
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public class QueryportMain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 是否首次加载 */
    @Excel(name = "是否首次加载", readConverterExp = "Y=是,N=否")
    private String ifFirstLoad;

    /** 是否验证权限 */
    @Excel(name = "是否验证权限", readConverterExp = "Y=是,N=否")
    private String isPermitted;

    /** 是否使用分页（Y是 N否） */
    @Excel(name = "是否使用分页", readConverterExp = "Y=是,N=否")
    private String pagination;

    /** 分页数量 */
    @Excel(name = "分页数量")
    private Long pagesize;

    /** 模块 */
    @Excel(name = "模块")
    private String module;

    /** 合并列 */
    @Excel(name = "合并列")
    private String mergecols;

    /** 语句 */
    @Excel(name = "语句")
    private String statement;

    /** 说明 */
    @Excel(name = "说明")
    private String adescribe;

    /** 算法说明 */
    @Excel(name = "算法说明")
    private String algorithm;

    /** 查询列 */
    private String expressionColumn;

    /** 导出列 */
    private String expressionExportColumn;

    /** 查询表 */
    private String expressionFrom;

    /** 查询条件 */
    private String expressionWhere;

    /** 分组条件 */
    private String expressionGroupby;

    /** 分组过滤 */
    private String expressionHaving;

    /** 排序 */
    private String expressionOrderby;

    /** 删除标记 */
    private String deleted;

    /** 参数配置列表 */
    private List<QueryportParam> queryportParams;

    /** 字段配置列表 */
    private List<QueryportColumn> queryportColumns;

    /** 图形配置列表 */
    private List<QueryportChart> queryportCharts;

    /** 参数配置map */
    private Map<String, QueryportParam> queryportParamsMap;

    /** 显示内容 */
    @Excel(name = "showType")
    private String showType;

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getIfFirstLoad() {
        return ifFirstLoad;
    }

    public void setIfFirstLoad(String ifFirstLoad) {
        this.ifFirstLoad = ifFirstLoad;
    }

    public String getIsPermitted() {
        return isPermitted;
    }

    public void setIsPermitted(String isPermitted) {
        this.isPermitted = isPermitted;
    }

    public String getStatus()
    {
        return status;
    }
    public void setPagination(String pagination) 
    {
        this.pagination = pagination;
    }

    public String getPagination() 
    {
        return pagination;
    }
    public void setPagesize(Long pagesize) 
    {
        this.pagesize = pagesize;
    }

    public Long getPagesize() 
    {
        return pagesize;
    }
    public void setModule(String module) 
    {
        this.module = module;
    }

    public String getModule() 
    {
        return module;
    }
    public void setMergecols(String mergecols) 
    {
        this.mergecols = mergecols;
    }

    public String getMergecols() 
    {
        return mergecols;
    }
    public void setStatement(String statement) 
    {
        this.statement = statement;
    }

    public String getStatement() 
    {
        return statement;
    }
    public void setAdescribe(String adescribe) 
    {
        this.adescribe = adescribe;
    }

    public String getAdescribe() 
    {
        return adescribe;
    }
    public void setAlgorithm(String algorithm) 
    {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() 
    {
        return algorithm;
    }
    public void setExpressionColumn(String expressionColumn) 
    {
        this.expressionColumn = expressionColumn;
    }

    public String getExpressionColumn() 
    {
        return expressionColumn;
    }
    public void setExpressionExportColumn(String expressionExportColumn) 
    {
        this.expressionExportColumn = expressionExportColumn;
    }

    public String getExpressionExportColumn() 
    {
        return expressionExportColumn;
    }
    public void setExpressionFrom(String expressionFrom) 
    {
        this.expressionFrom = expressionFrom;
    }

    public String getExpressionFrom() 
    {
        return expressionFrom;
    }
    public void setExpressionWhere(String expressionWhere) 
    {
        this.expressionWhere = expressionWhere;
    }

    public String getExpressionWhere() 
    {
        return expressionWhere;
    }
    public void setExpressionGroupby(String expressionGroupby) 
    {
        this.expressionGroupby = expressionGroupby;
    }

    public String getExpressionGroupby() 
    {
        return expressionGroupby;
    }
    public void setExpressionHaving(String expressionHaving) 
    {
        this.expressionHaving = expressionHaving;
    }

    public String getExpressionHaving() 
    {
        return expressionHaving;
    }
    public void setExpressionOrderby(String expressionOrderby) 
    {
        this.expressionOrderby = expressionOrderby;
    }

    public String getExpressionOrderby() 
    {
        return expressionOrderby;
    }


    public List<QueryportParam> getQueryportParams() {
        return queryportParams;
    }

    public void setQueryportParams(List<QueryportParam> queryportParams) {
        this.queryportParams = queryportParams;
    }

    public List<QueryportColumn> getQueryportColumns() {
        return queryportColumns;
    }

    public void setQueryportColumns(List<QueryportColumn> queryportColumns) {
        this.queryportColumns = queryportColumns;
    }

    public List<QueryportChart> getQueryportCharts() {
        return queryportCharts;
    }

    public void setQueryportCharts(List<QueryportChart> queryportCharts) {
        this.queryportCharts = queryportCharts;
    }

    public Map<String, QueryportParam> getQueryportParamsMap() {
        return queryportParamsMap;
    }

    public void setQueryportParamsMap(Map<String, QueryportParam> queryportParamsMap) {
        this.queryportParamsMap = queryportParamsMap;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("status", getStatus())
            .append("pagination", getPagination())
            .append("pagesize", getPagesize())
            .append("module", getModule())
            .append("mergecols", getMergecols())
            .append("statement", getStatement())
            .append("adescribe", getAdescribe())
            .append("algorithm", getAlgorithm())
            .append("expressionColumn", getExpressionColumn())
            .append("expressionExportColumn", getExpressionExportColumn())
            .append("expressionFrom", getExpressionFrom())
            .append("expressionWhere", getExpressionWhere())
            .append("expressionGroupby", getExpressionGroupby())
            .append("expressionHaving", getExpressionHaving())
            .append("expressionOrderby", getExpressionOrderby())
            .append("deleted", getDeleted())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
