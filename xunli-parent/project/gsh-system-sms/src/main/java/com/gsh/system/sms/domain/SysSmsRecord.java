package com.gsh.system.sms.domain;

import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 短信发送记录对象 aiexam_sms_record
 * 
 * @author gsh
 * @date 2019-09-04
 */
public class SysSmsRecord extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 主键ID */
	private Long id;

	/** 短信接口配置ID */
	@Excel(name = "短信接口配置ID")
	private Long smsConfId;

	/** 短信接口配置名称 */
	@Excel(name = "短信接口配置名称")
	private String smsConfName;

	/** 接口响应时间 */
	@Excel(name = "接口响应时间", width = 30, dateFormat = "yyyy-MM-dd")
	private Date successTime;

	/** 成功标记 */
	@Excel(name = "成功标记")
	private Long successFlag;
	
	/** 短信内容 */
	@Excel(name = "短信类型")
	private String smsType;

	/** 短信内容 */
	@Excel(name = "短信内容")
	private String smsContent;

	/** 接收人名称 */
	@Excel(name = "接收人名称")
	private String receiverLoginName;

	/** 接收人手机号 */
	@Excel(name = "接收人手机号")
	private String receiverMobile;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setSmsConfId(Long smsConfId) {
		this.smsConfId = smsConfId;
	}

	public Long getSmsConfId() {
		return smsConfId;
	}

	public void setSmsConfName(String smsConfName) {
		this.smsConfName = smsConfName;
	}

	public String getSmsConfName() {
		return smsConfName;
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	public Date getSuccessTime() {
		return successTime;
	}

	public void setSuccessFlag(Long successFlag) {
		this.successFlag = successFlag;
	}

	public Long getSuccessFlag() {
		return successFlag;
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setReceiverLoginName(String receiverLoginName) {
		this.receiverLoginName = receiverLoginName;
	}

	public String getReceiverLoginName() {
		return receiverLoginName;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId()).append("smsConfId", getSmsConfId()).append("smsConfName", getSmsConfName()).append("successTime", getSuccessTime()).append("successFlag", getSuccessFlag()).append("smsContent", getSmsContent()).append("receiverLoginName", getReceiverLoginName()).append("receiverMobile", getReceiverMobile())
				.append("createBy", getCreateBy()).append("createByName", getCreateByName()).append("createByDeptid", getCreateByDeptid()).append("createByDeptname", getCreateByDeptname()).append("createTime", getCreateTime()).append("updateBy", getUpdateBy()).append("updateByName", getUpdateByName()).append("updateByDeptid", getUpdateByDeptid()).append("updateByDeptname", getUpdateByDeptname())
				.append("updateTime", getUpdateTime()).append("remark", getRemark()).append("deleted", getDeleted()).toString();
	}
}
