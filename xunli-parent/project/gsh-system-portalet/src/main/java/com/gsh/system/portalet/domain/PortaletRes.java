package com.gsh.system.portalet.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 可配置资源信息对象 sys_portalet_res
 * 
 * @author gsh
 * @date 2020-03-16
 */
public class PortaletRes extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键Id */
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

    /** 系统编码 */
    @Excel(name = "系统编码")
    private String systemCode;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String systemName;

    /** 模块编码 */
    @Excel(name = "模块编码")
    private String moduleCode;

    /** 模块名称 */
    @Excel(name = "模块名称")
    private String moduleName;

    /** 资源名称 */
    @Excel(name = "资源名称")
    private String resName;

    /** url */
    @Excel(name = "url")
    private String url;

    /** 宽 */
    @Excel(name = "宽")
    private Long width;

    /** 高 */
    @Excel(name = "高")
    private Long height;

    /** 视图Code */
    @Excel(name = "视图Code")
    private String queryportId;

    /** 图片 */
    @Excel(name = "图片")
    private String imgUrl;

    /** 图片宽 */
    @Excel(name = "图片宽")
    private Long imgWidth;

    /** 图片高 */
    @Excel(name = "图片高")
    private Long imgHeight;

    /** html */
    @Excel(name = "html")
    private String html;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long sortNo;

    /** 删除 */
    @Excel(name = "删除")
    private String deleted;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createByName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setCnName(String cnName) 
    {
        this.cnName = cnName;
    }

    public String getCnName() 
    {
        return cnName;
    }
    public void setResType(String resType) 
    {
        this.resType = resType;
    }

    public String getResType() 
    {
        return resType;
    }
    public void setSystemCode(String systemCode) 
    {
        this.systemCode = systemCode;
    }

    public String getSystemCode() 
    {
        return systemCode;
    }
    public void setSystemName(String systemName) 
    {
        this.systemName = systemName;
    }

    public String getSystemName() 
    {
        return systemName;
    }
    public void setModuleCode(String moduleCode) 
    {
        this.moduleCode = moduleCode;
    }

    public String getModuleCode() 
    {
        return moduleCode;
    }
    public void setModuleName(String moduleName) 
    {
        this.moduleName = moduleName;
    }

    public String getModuleName() 
    {
        return moduleName;
    }
    public void setResName(String resName) 
    {
        this.resName = resName;
    }

    public String getResName() 
    {
        return resName;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setWidth(Long width) 
    {
        this.width = width;
    }

    public Long getWidth() 
    {
        return width;
    }
    public void setHeight(Long height) 
    {
        this.height = height;
    }

    public Long getHeight() 
    {
        return height;
    }
    public void setQueryportId(String queryportId) 
    {
        this.queryportId = queryportId;
    }

    public String getQueryportId() 
    {
        return queryportId;
    }
    public void setImgUrl(String imgUrl) 
    {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() 
    {
        return imgUrl;
    }
    public void setImgWidth(Long imgWidth) 
    {
        this.imgWidth = imgWidth;
    }

    public Long getImgWidth() 
    {
        return imgWidth;
    }
    public void setImgHeight(Long imgHeight) 
    {
        this.imgHeight = imgHeight;
    }

    public Long getImgHeight() 
    {
        return imgHeight;
    }
    public void setHtml(String html) 
    {
        this.html = html;
    }

    public String getHtml() 
    {
        return html;
    }
    public void setSortNo(Long sortNo) 
    {
        this.sortNo = sortNo;
    }

    public Long getSortNo() 
    {
        return sortNo;
    }
    public void setDeleted(String deleted) 
    {
        this.deleted = deleted;
    }

    public String getDeleted() 
    {
        return deleted;
    }
    public void setCreateByName(String createByName) 
    {
        this.createByName = createByName;
    }

    public String getCreateByName() 
    {
        return createByName;
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
            .append("resName", getResName())
            .append("url", getUrl())
            .append("width", getWidth())
            .append("height", getHeight())
            .append("queryportId", getQueryportId())
            .append("imgUrl", getImgUrl())
            .append("imgWidth", getImgWidth())
            .append("imgHeight", getImgHeight())
            .append("html", getHtml())
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