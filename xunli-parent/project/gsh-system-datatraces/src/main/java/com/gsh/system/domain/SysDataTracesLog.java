package com.gsh.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 数据追溯日志对象 sys_data_traces_log
 * 
 * @author gsh
 * @date 2019-11-20
 */
public class SysDataTracesLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 操作名词 */
    @Excel(name = "操作名词")
    private String opMethod;

    /** 操作对象id */
    @Excel(name = "操作对象id")
    private Long opObjectId;

    /** 操作对象名称 */
    @Excel(name = "操作对象名称")
    private String opObjectName;

    /** 关联对象id */
    @Excel(name = "关联对象id")
    private Long opForeginId;

    /** 关联对象名称 */
    @Excel(name = "关联对象名称")
    private String opForeginName;

    /** 操作内容 */
    @Excel(name = "操作内容")
    private String opContent;

    /** 旧对象 */
    @Excel(name = "旧对象")
    private String oldObject;

    /** 新对象 */
    @Excel(name = "新对象")
    private String newObject;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOpMethod(String opMethod) 
    {
        this.opMethod = opMethod;
    }

    public String getOpMethod() 
    {
        return opMethod;
    }
    public void setOpObjectId(Long opObjectId) 
    {
        this.opObjectId = opObjectId;
    }

    public Long getOpObjectId() 
    {
        return opObjectId;
    }
    public void setOpObjectName(String opObjectName) 
    {
        this.opObjectName = opObjectName;
    }

    public String getOpObjectName() 
    {
        return opObjectName;
    }
    public void setOpForeginId(Long opForeginId) 
    {
        this.opForeginId = opForeginId;
    }

    public Long getOpForeginId() 
    {
        return opForeginId;
    }
    public void setOpForeginName(String opForeginName) 
    {
        this.opForeginName = opForeginName;
    }

    public String getOpForeginName() 
    {
        return opForeginName;
    }
    public void setOpContent(String opContent) 
    {
        this.opContent = opContent;
    }

    public String getOpContent() 
    {
        return opContent;
    }
    public void setOldObject(String oldObject) 
    {
        this.oldObject = oldObject;
    }

    public String getOldObject() 
    {
        return oldObject;
    }
    public void setNewObject(String newObject) 
    {
        this.newObject = newObject;
    }

    public String getNewObject() 
    {
        return newObject;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("opMethod", getOpMethod())
            .append("opObjectId", getOpObjectId())
            .append("opObjectName", getOpObjectName())
            .append("opForeginId", getOpForeginId())
            .append("opForeginName", getOpForeginName())
            .append("opContent", getOpContent())
            .append("oldObject", getOldObject())
            .append("newObject", getNewObject())
            .append("deleted", getDeleted())
            .append("createBy", getCreateBy())
            .append("createByName", getCreateByName())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .append("createByDeptid", getCreateByDeptid())
            .append("createByDeptname", getCreateByDeptname())
            .toString();
    }
}
