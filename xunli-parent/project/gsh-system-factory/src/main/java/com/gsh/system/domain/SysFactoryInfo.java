package com.gsh.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;

/**
 * 系统资料对象 sys_factory_info
 * 
 * @author gsh
 * @date 2019-10-31
 */
public class SysFactoryInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 组织编码 */
    private Long id;

    /** 部门ID */
    @Excel(name = "部门ID ")
    private Long factoryDeptId;

    /** 管理员 */
    @Excel(name = "管理员")
    private String loginName;

    /** 组织名称 */
    @Excel(name = "组织名称")
    private String factoryName;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String systemName;

    /** 生成秘钥 */
    @Excel(name = "生成秘钥")
    private String secretKey;

    /** 前端lLOGO */
    @Excel(name = "前端lLOGO")
    private String feLogo;

    /** 平台端LOGO */
    @Excel(name = "平台端LOGO")
    private String platformLogo;

    /** 系统LOGO */
    @Excel(name = "系统LOGO")
    private String sysLogo;

    /** 成员账号数 */
    @Excel(name = "成员账号数")
    private String accountsNum;

    /** 同时在线人数 */
    @Excel(name = "同时在线人数")
    private String onlineusersNum;
    
    /** 版本级别 */
    @Excel(name = "版本级别")
    private String versionType;
    
    /** 当前版本 */
    @Excel(name = "当前版本")
    private String versionName;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getFactoryDeptId() {
        return factoryDeptId;
    }

    public void setFactoryDeptId(Long factoryDeptId) {
        this.factoryDeptId = factoryDeptId;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setFeLogo(String feLogo) {
        this.feLogo = feLogo;
    }

    public String getFeLogo() {
        return feLogo;
    }

    public void setPlatformLogo(String platformLogo) {
        this.platformLogo = platformLogo;
    }

    public String getPlatformLogo() {
        return platformLogo;
    }

    public void setSysLogo(String sysLogo) {
        this.sysLogo = sysLogo;
    }

    public String getSysLogo() {
        return sysLogo;
    }

    public void setAccountsNum(String accountsNum) {
        this.accountsNum = accountsNum;
    }

    public String getAccountsNum() {
        return accountsNum;
    }

    public void setOnlineusersNum(String onlineusersNum) {
        this.onlineusersNum = onlineusersNum;
    }

    public String getOnlineusersNum() {
        return onlineusersNum;
    }

    public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        		.append("id", getId())
        		.append("loginName", getLoginName())
        		.append("factoryName", getFactoryName())
        		.append("systemName", getSystemName())
        		.append("secretKey", getSecretKey())
        		.append("feLogo", getFeLogo())
        		.append("platformLogo", getPlatformLogo())
        		.append("sysLogo", getSysLogo())
        		.append("accountsNum", getAccountsNum())
        		.append("onlineusersNum", getOnlineusersNum())
        		.append("createBy", getCreateBy())
        		.append("createTime", getCreateTime())
        		.append("updateBy", getUpdateBy())
        		.append("updateTime", getUpdateTime())
        		.append("remark", getRemark())
        		.append("deleted", getDeleted())
        		.append("versionType", getVersionType())
        		.append("versionName", getVersionName())
        		.toString();
    }
}