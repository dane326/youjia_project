package com.gsh.system.sms.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 短信模板配置对象 aiexam_sms_template
 * 
 * @author gsh
 * @date 2019-09-04
 */
public class SysSmsTemplate extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 主键ID */
	private Long id;

	/** 模板名称 */
	@Excel(name = "模板名称")
	private String name;

	/** 模板内容 */
	@Excel(name = "模板内容")
	private String template;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId()).append("name", getName()).append("template", getTemplate()).append("createBy", getCreateBy()).append("createByName", getCreateByName()).append("createByDeptid", getCreateByDeptid()).append("createByDeptname", getCreateByDeptname()).append("createTime", getCreateTime()).append("updateBy", getUpdateBy())
				.append("updateByName", getUpdateByName()).append("updateByDeptid", getUpdateByDeptid()).append("updateByDeptname", getUpdateByDeptname()).append("updateTime", getUpdateTime()).append("remark", getRemark()).append("deleted", getDeleted()).toString();
	}
}
