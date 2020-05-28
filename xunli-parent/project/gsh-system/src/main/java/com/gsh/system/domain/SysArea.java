package com.gsh.system.domain;

import com.gsh.common.annotation.Excel;
import com.gsh.common.annotation.Id2NameClass;
import com.gsh.common.core.domain.TreeEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 地域对象 sys_area
 * 
 * @author gsh-xrl
 * @date 2019-11-12
 */
@Id2NameClass(idCol = {"area_id"}, nameCol = "area_name", table="sys_area", condition = "")
public class SysArea extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 地域ID */
    private Long areaId;

    /** 地域名称 */
    @Excel(name = "地域名称")
    private String areaName;

    /** 父节点id */
    @Excel(name = "父节点id")
    private Long parentId;

    /** 排序码 */
    @Excel(name = "排序码")
    private Integer orderNum;

    /** 排序码 */
    @Excel(name = "级别")
    private Integer areaLevel;

    private String ancestors;

    @Override
    public Integer getOrderNum() {
        return orderNum;
    }

    @Override
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public void setAreaId(Long areaId)
    {
        this.areaId = areaId;
    }

    public Long getAreaId() 
    {
        return areaId;
    }
    public void setAreaName(String areaName) 
    {
        this.areaName = areaName;
    }

    public String getAreaName() 
    {
        return areaName;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public Integer getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel) {
        this.areaLevel = areaLevel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("areaId", getAreaId())
            .append("areaName", getAreaName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("areaLevel", getAreaLevel())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
