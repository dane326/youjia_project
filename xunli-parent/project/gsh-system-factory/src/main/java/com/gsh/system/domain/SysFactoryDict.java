package com.gsh.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.TreeEntity;

/**
 * 组织字典对象 sys_factory_dict
 * 
 * @author gsh
 * @date 2020-04-04
 */
public class SysFactoryDict extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    private Long dictCode;

    /** 字典名称 */
    @Excel(name = "字典名称")
    private String dictName;

    /** 字典排序 */
    @Excel(name = "字典排序")
    private Integer dictSort;

    /** 字典标签 */
    @Excel(name = "字典标签")
    private String dictLabel;

    /** 样式属性（其他样式扩展） */
    @Excel(name = "样式属性", readConverterExp = "其=他样式扩展")
    private String cssClass;

    /** 表格回显样式 */
    @Excel(name = "表格回显样式")
    private String listClass;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 字典数据父节点 */
    @Excel(name = "字典数据父节点")
    private Long parentCode;

    /** 是否默认（Y是 N否） */
    @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
    private String isDefault;

    /** 字典类型 */
    @Excel(name = "字典类型")
    private String dictType;

    /** 字典键值 */
    @Excel(name = "字典键值")
    private String dictValue;

    /** 字典类型 */
    @Excel(name = "父类字典类型")
    private String parentType;

    /** 字典键值 */
    @Excel(name = "父类字典键值")
    private String parentValue;

    /** 子节点数量 */
    @Excel(name = "子节点数量")
    private Long childNodesNum;

    public Long getChildNodesNum() {
        return childNodesNum;
    }

    public void setChildNodesNum(Long childNodesNum) {
        this.childNodesNum = childNodesNum;
    }

    public String getParentValue() {
        return parentValue;
    }

    public void setParentValue(String parentValue) {
        this.parentValue = parentValue;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public void setDictCode(Long dictCode)
    {
        this.dictCode = dictCode;
    }

    public Long getDictCode() 
    {
        return dictCode;
    }
    public void setDictName(String dictName) 
    {
        this.dictName = dictName;
    }

    public String getDictName() 
    {
        return dictName;
    }
    public void setDictSort(Integer dictSort) 
    {
        this.dictSort = dictSort;
    }

    public Integer getDictSort() 
    {
        return dictSort;
    }
    public void setDictLabel(String dictLabel) 
    {
        this.dictLabel = dictLabel;
    }

    public String getDictLabel() 
    {
        return dictLabel;
    }
    public void setCssClass(String cssClass) 
    {
        this.cssClass = cssClass;
    }

    public String getCssClass() 
    {
        return cssClass;
    }
    public void setListClass(String listClass) 
    {
        this.listClass = listClass;
    }

    public String getListClass() 
    {
        return listClass;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setParentCode(Long parentCode) 
    {
        this.parentCode = parentCode;
    }

    public Long getParentCode() 
    {
        return parentCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictCode", getDictCode())
            .append("dictName", getDictName())
            .append("dictSort", getDictSort())
            .append("dictLabel", getDictLabel())
            .append("cssClass", getCssClass())
            .append("listClass", getListClass())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("parentCode", getParentCode())
            .toString();
    }
}
