package com.gsh.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 数据追溯日志详情对象 sys_data_traces_detail
 * 
 * @author gsh
 * @date 2019-11-20
 */
public class SysDataTracesDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 日志ID */
    @Excel(name = "日志ID")
    private Long datatracesId;

    /** 字段名 */
    @Excel(name = "字段名")
    private String fieldName;

    /** 字段中文名 */
    @Excel(name = "字段中文名")
    private String fieldCnName;

    /** 旧值 */
    @Excel(name = "旧值")
    private String oldValue;

    /** 新值 */
    @Excel(name = "新值")
    private String newValue;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDatatracesId(Long datatracesId) 
    {
        this.datatracesId = datatracesId;
    }

    public Long getDatatracesId() 
    {
        return datatracesId;
    }
    public void setFieldName(String fieldName) 
    {
        this.fieldName = fieldName;
    }

    public String getFieldName() 
    {
        return fieldName;
    }
    public void setFieldCnName(String fieldCnName) 
    {
        this.fieldCnName = fieldCnName;
    }

    public String getFieldCnName() 
    {
        return fieldCnName;
    }
    public void setOldValue(String oldValue) 
    {
        this.oldValue = oldValue;
    }

    public String getOldValue() 
    {
        return oldValue;
    }
    public void setNewValue(String newValue) 
    {
        this.newValue = newValue;
    }

    public String getNewValue() 
    {
        return newValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("datatracesId", getDatatracesId())
            .append("fieldName", getFieldName())
            .append("fieldCnName", getFieldCnName())
            .append("oldValue", getOldValue())
            .append("newValue", getNewValue())
            .toString();
    }
}
