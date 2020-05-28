package com.gsh.system.queryport.domain;

import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 查询视图参数对象 sys_queryport_param
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
public class QueryportParam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** mainId */
    @Excel(name = "mainId")
    private Long mainId;

    /** 是否显示 */
    @Excel(name = "是否显示")
    private String viewFlag;

    /** 英文名称 */
    @Excel(name = "英文名称")
    private String ename;

    /** 中文名称 */
    @Excel(name = "中文名称")
    private String cname;

    /** 是否允许为空 */
    @Excel(name = "是否允许为空")
    private String allowBlank;

    /** 类型 */
    @Excel(name = "类型")
    private String paramType;

    /** 类型 */
    @Excel(name = "填写方式")
    private String inputMode;

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

    /** 参数值 */
    @Excel(name = "参数值")
    private String paramValue;

    /** 默认值 */
    @Excel(name = "默认值")
    private String defaultValue;

    /** 顺序 */
    @Excel(name = "顺序")
    private Integer displayOrder;

    /** 删除标记 */
    @Excel(name = "删除标记")
    private String deleted;

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
    public void setAllowBlank(String allowBlank) 
    {
        this.allowBlank = allowBlank;
    }

    public String getAllowBlank() 
    {
        return allowBlank;
    }

    public String getInputMode() {
        return inputMode;
    }

    public void setInputMode(String inputMode) {
        this.inputMode = inputMode;
    }

    public void setParamType(String paramType)
    {
        this.paramType = paramType;
    }

    public String getParamType()
    {
        return paramType;
    }
    public void setTypeParam1(String typeParam1) 
    {
        this.typeParam1 = typeParam1;
    }

    public String getTypeParam1() 
    {
        return typeParam1;
    }
    public void setTypeParam2(String typeParam2) 
    {
        this.typeParam2 = typeParam2;
    }

    public String getTypeParam2() 
    {
        return typeParam2;
    }
    public void setTypeParam3(String typeParam3) 
    {
        this.typeParam3 = typeParam3;
    }

    public String getTypeParam3() 
    {
        return typeParam3;
    }
    public void setTypeParam4(String typeParam4) 
    {
        this.typeParam4 = typeParam4;
    }

    public String getTypeParam4() 
    {
        return typeParam4;
    }
    public void setParamValue(String paramValue) 
    {
        this.paramValue = paramValue;
    }

    public String getParamValue() 
    {
        return paramValue;
    }
    public void setDefaultValue(String defaultValue) 
    {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() 
    {
        return defaultValue;
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
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("mainId", getMainId())
            .append("viewFlag", getViewFlag())
            .append("ename", getEname())
            .append("cname", getCname())
            .append("allowBlank", getAllowBlank())
            .append("inputMode",getInputMode())
            .append("paramType", getParamType())
            .append("typeParam1", getTypeParam1())
            .append("typeParam2", getTypeParam2())
            .append("typeParam3", getTypeParam3())
            .append("typeParam4", getTypeParam4())
            .append("paramValue", getParamValue())
            .append("defaultValue", getDefaultValue())
            .append("displayOrder", getDisplayOrder())
            .append("deleted", getDeleted())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
