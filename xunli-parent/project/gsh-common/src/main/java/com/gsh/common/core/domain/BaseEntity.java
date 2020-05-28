package com.gsh.common.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gsh.common.annotation.Excel;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 * 
 * @author gsh
 */
public class BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 租户编码 */
    @Excel(name = "租户编码")
    private Long factoryCode;
    
    /** 系统名称 */
    @Excel(name = "系统名称")
    private String contextName;

    /** 搜索值 */
    private String searchValue;

    /** 创建者 */
    private String createBy;

    /** 创建人姓名 */
    @Excel(name = "创建人",type = Excel.Type.EXPORT)
    private String createByName;

    /** 创建人部门ID */
    private Long createByDeptid;

    /** 创建人部门名称 */
    private String createByDeptname;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间",dateFormat = "yyyy-MM-dd HH:mm:ss",type = Excel.Type.EXPORT)
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新人姓名 */
    private String updateByName;

    /** 修改人部门ID */
    private Long updateByDeptid;

    /** 修改人部门名称 */
    private String updateByDeptname;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**逻辑删除标识：1-删除，0-未删除*/
    private String deleted;

    /** 备注 */
    private String remark;

    /** 请求参数 */
    private Map<String, Object> params;
    
    public Long getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(Long factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getSearchValue()
    {
        return searchValue;
    }

    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }


    public String getCreateByDeptname() {
        return createByDeptname;
    }

    public void setCreateByDeptname(String createByDeptname) {
        this.createByDeptname = createByDeptname;
    }

    public Long getCreateByDeptid() {
        return createByDeptid;
    }

    public void setCreateByDeptid(Long createByDeptid) {
        this.createByDeptid = createByDeptid;
    }

    public Long getUpdateByDeptid() {
        return updateByDeptid;
    }

    public void setUpdateByDeptid(Long updateByDeptid) {
        this.updateByDeptid = updateByDeptid;
    }

    public String getUpdateByDeptname() {
        return updateByDeptname;
    }

    public void setUpdateByDeptname(String updateByDeptname) {
        this.updateByDeptname = updateByDeptname;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}
    
}
