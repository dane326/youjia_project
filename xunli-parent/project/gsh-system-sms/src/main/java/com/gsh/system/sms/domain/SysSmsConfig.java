package com.gsh.system.sms.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 短信接口配置对象 aiexam_sms_config
 * 
 * @author gsh
 * @date 2019-09-04
 */
public class SysSmsConfig extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 主键ID */
	private Long id;
	
	/** 配置名称 */
	@Excel(name = "短信类型")
	private String type;

	/** 配置名称 */
	@Excel(name = "配置名称")
	private String name;

	/** 接口实现类 */
	@Excel(name = "接口实现类")
	private String beanName;

	/** 接口参数 */
	@Excel(name = "接口参数")
	private String confParam;

	/** 剩余次数 */
	@Excel(name = "剩余次数")
	private Long surplusNum;

	/** 方法名称 */
	@Excel(name = "方法名称")
	private String methodName;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setConfParam(String confParam) {
		this.confParam = confParam;
	}

	public String getConfParam() {
		return confParam;
	}

	public void setSurplusNum(Long surplusNum) {
		this.surplusNum = surplusNum;
	}

	public Long getSurplusNum() {
		return surplusNum;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodName() {
		return methodName;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId()).append("name", getName()).append("beanName", getBeanName()).append("confParam", getConfParam()).append("surplusNum", getSurplusNum()).append("methodName", getMethodName()).append("createBy", getCreateBy()).append("createByName", getCreateByName()).append("createByDeptid", getCreateByDeptid())
				.append("createByDeptname", getCreateByDeptname()).append("createTime", getCreateTime()).append("updateBy", getUpdateBy()).append("updateByName", getUpdateByName()).append("updateByDeptid", getUpdateByDeptid()).append("updateByDeptname", getUpdateByDeptname()).append("updateTime", getUpdateTime()).append("remark", getRemark()).append("deleted", getDeleted()).toString();
	}
}
