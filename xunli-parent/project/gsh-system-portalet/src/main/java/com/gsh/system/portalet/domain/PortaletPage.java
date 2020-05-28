package com.gsh.system.portalet.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 自定义portal页面对象 sys_portalet_page
 * 
 * @author gsh
 * @date 2020-03-17
 */
public class PortaletPage extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 主键id */
	private Long id;

	/** 父Id */
	@Excel(name = "父Id")
	private Long parentId;

	/** 资源名称 */
	@Excel(name = "资源名称")
	private String cnName;

	/** 资源类别 */
	@Excel(name = "资源类别")
	private String resType;

	/** 系统Code */
	@Excel(name = "系统编码")
	private String systemCode;

	/** 系统名称 */
	@Excel(name = "系统名称")
	private String systemName;

	/** 模块Code */
	@Excel(name = "模块编码")
	private String moduleCode;

	/** 模块名称 */
	@Excel(name = "模块名称")
	private String moduleName;

	/** 资源Id */
	@Excel(name = "资源编码")
	private Long resId;

	/** 宽 */
	@Excel(name = "宽")
	private Long width;

	/** 高 */
	@Excel(name = "高")
	private Long height;

	/** 行 */
	@Excel(name = "行")
	private Long row;

	/** 列 */
	@Excel(name = "列")
	private Long col;

	/** 显示顺序 */
	@Excel(name = "显示顺序")
	private Long sortNo;

	/** 删除 */
	@Excel(name = "删除")
	private String deleted;

	/** 创建人 */
	@Excel(name = "创建人")
	private String createByName;

	private PortaletRes portaletRes;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getCnName() {
		return cnName;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getResType() {
		return resType;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setResId(Long resId) {
		this.resId = resId;
	}

	public Long getResId() {
		return resId;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

	public Long getWidth() {
		return width;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public Long getHeight() {
		return height;
	}

	public void setRow(Long row) {
		this.row = row;
	}

	public Long getRow() {
		return row;
	}

	public void setCol(Long col) {
		this.col = col;
	}

	public Long getCol() {
		return col;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	public Long getSortNo() {
		return sortNo;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getCreateByName() {
		return createByName;
	}

	public PortaletRes getPortaletRes() {
		return portaletRes;
	}

	public void setPortaletRes(PortaletRes portaletRes) {
		this.portaletRes = portaletRes;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("cnName", getCnName())
            .append("resType", getResType())
            .append("systemCode", getSystemCode())
            .append("systemName", getSystemName())
            .append("moduleCode", getModuleCode())
            .append("moduleName", getModuleName())
            .append("resId", getResId())
            .append("width", getWidth())
            .append("height", getHeight())
            .append("row", getRow())
            .append("col", getCol())
            .append("sortNo", getSortNo())
            .append("deleted", getDeleted())
            .append("createBy", getCreateBy())
            .append("createByName", getCreateByName())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
