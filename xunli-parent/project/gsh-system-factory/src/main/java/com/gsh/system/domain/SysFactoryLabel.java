package com.gsh.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 标签对象 sys_factory_label
 * 
 * @author gsh
 * @date 2019-10-17
 */
public class SysFactoryLabel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long lableId;

    /** 标签编码 */
    @Excel(name = "标签编码")
    private String lableCode;

    /** 标签名称 */
    @Excel(name = "标签名称")
    private String lableName;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer lableSort;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 用户是否存在此标签标识 默认不存在 */
    private boolean flag = false;

    public void setLableId(Long lableId) 
    {
        this.lableId = lableId;
    }

    public Long getLableId() 
    {
        return lableId;
    }
    public void setLableCode(String lableCode) 
    {
        this.lableCode = lableCode;
    }

    public String getLableCode() 
    {
        return lableCode;
    }
    public void setLableName(String lableName) 
    {
        this.lableName = lableName;
    }

    public String getLableName() 
    {
        return lableName;
    }
    public void setLableSort(Integer lableSort) 
    {
        this.lableSort = lableSort;
    }

    public Integer getLableSort() 
    {
        return lableSort;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("lableId", getLableId())
            .append("lableCode", getLableCode())
            .append("lableName", getLableName())
            .append("lableSort", getLableSort())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deleted", getDeleted())
            .toString();
    }
}
