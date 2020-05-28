package com.gsh.system.queryport.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 查询视图column对象 sys_queryport_column
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public class QueryportColumn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** mainId */
    @Excel(name = "mainId")
    private Long mainId;

    /** 是否汇总 */
    @Excel(name = "是否汇总")
    private String totalFlag;

    /** 是否显示 */
    @Excel(name = "是否显示")
    private String viewFlag;

    /** 英文名称 */
    @Excel(name = "英文名称")
    private String ename;

    /** 中文名称 */
    @Excel(name = "中文名称")
    private String cname;

    /** 是否可导出 */
    @Excel(name = "是否可导出")
    private String exportflag;

    /** 是否允许为空 */
    @Excel(name = "是否允许为空")
    private String allowBlank;

    /** 转换类型 */
    @Excel(name = "转换类型")
    private String convertType;

    /** 链接 */
    @Excel(name = "链接")
    private String columnLink;

    /** 顺序 */
    @Excel(name = "顺序")
    private Integer displayOrder;

    /** 删除标记 */
    @Excel(name = "删除标记")
    private String deleted;

    /** 类型 参数1 */
    @Excel(name = "类型 参数1")
    private String typeParam1;

    /** 类型 参数2 */
    @Excel(name = "类型 参数2")
    private String typeParam2;

    /** 类型 参数3 */
    @Excel(name = "类型 参数3")
    private String typeParam3;

    /** 类型 参数4 */
    @Excel(name = "类型 参数4")
    private String typeParam4;

    public String getTypeParam1() {
        return typeParam1;
    }

    public void setTypeParam1(String typeParam1) {
        this.typeParam1 = typeParam1;
    }

    public String getTypeParam2() {
        return typeParam2;
    }

    public void setTypeParam2(String typeParam2) {
        this.typeParam2 = typeParam2;
    }

    public String getTypeParam3() {
        return typeParam3;
    }

    public void setTypeParam3(String typeParam3) {
        this.typeParam3 = typeParam3;
    }

    public String getTypeParam4() {
        return typeParam4;
    }

    public void setTypeParam4(String typeParam4) {
        this.typeParam4 = typeParam4;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMainId(Long mainId) 
    {
        this.mainId = mainId;
    }

    public String getTotalFlag() {
        return totalFlag;
    }

    public void setTotalFlag(String totalFlag) {
        this.totalFlag = totalFlag;
    }

    public Long getMainId() 
    {
        return mainId;
    }
    public void setViewFlag(String viewFlag) 
    {
        this.viewFlag = viewFlag;
    }

    public String getViewFlag() 
    {
        return viewFlag;
    }
    public void setEname(String ename) 
    {
        this.ename = ename;
    }

    public String getEname() 
    {
        return ename;
    }
    public void setCname(String cname) 
    {
        this.cname = cname;
    }

    public String getCname() 
    {
        return cname;
    }
    public void setExportflag(String exportflag) 
    {
        this.exportflag = exportflag;
    }

    public String getExportflag() 
    {
        return exportflag;
    }
    public void setAllowBlank(String allowBlank) 
    {
        this.allowBlank = allowBlank;
    }

    public String getAllowBlank() 
    {
        return allowBlank;
    }
    public void setConvertType(String convertType) 
    {
        this.convertType = convertType;
    }

    public String getConvertType() 
    {
        return convertType;
    }
    public void setColumnLink(String columnLink) 
    {
        this.columnLink = columnLink;
    }

    public String getColumnLink() 
    {
        return columnLink;
    }
    public void setDisplayOrder(Integer displayOrder) 
    {
        this.displayOrder = displayOrder;
    }

    public Integer getDisplayOrder() 
    {
        return displayOrder;
    }

    @Override
    public String toString() {
        return "QueryportColumn{" +
                "id=" + id +
                ", mainId=" + mainId +
                ", viewFlag='" + viewFlag + '\'' +
                ", ename='" + ename + '\'' +
                ", cname='" + cname + '\'' +
                ", exportflag='" + exportflag + '\'' +
                ", allowBlank='" + allowBlank + '\'' +
                ", convertType='" + convertType + '\'' +
                ", columnLink='" + columnLink + '\'' +
                ", displayOrder=" + displayOrder +
                ", deleted='" + deleted + '\'' +
                ", typeParam1='" + typeParam1 + '\'' +
                ", typeParam2='" + typeParam2 + '\'' +
                ", typeParam3='" + typeParam3 + '\'' +
                ", typeParam4='" + typeParam4 + '\'' +
                '}';
    }
}
