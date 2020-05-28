package com.gsh.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 附件对象 sys_accessories
 * 
 * @author gsh
 * @date 2019-09-24
 */
public class SysAccessories extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 模块名称 */
    @Excel(name = "模块名称")
    private String moduleName;

    /** 附件名称 */
    @Excel(name = "附件名称")
    private String fileName;

    /** 扩展名 */
    @Excel(name = "扩展名")
    private String extension;
    
    /** 扩展名 */
    @Excel(name = "路径")
    private String path;

    /** 内容 */
    @Excel(name = "内容")
    private byte[] content;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setModuleName(String moduleName) 
    {
        this.moduleName = moduleName;
    }

    public String getModuleName() 
    {
        return moduleName;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setExtension(String extension) 
    {
        this.extension = extension;
    }

    public String getExtension() 
    {
        return extension;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setContent(byte[] content) 
    {
        this.content = content;
    }

    public byte[] getContent() 
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("moduleName", getModuleName())
            .append("fileName", getFileName())
            .append("extension", getExtension())
            .append("path", getPath())
            .append("content", getContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deleted", getDeleted())
            .toString();
    }
}
