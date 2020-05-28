package com.gsh.framework.shiro.session;

import java.io.Serializable;
import java.util.Date;

import org.apache.shiro.session.mgt.SimpleSession;

import com.gsh.common.constant.ShiroConstants;
import com.gsh.common.enums.OnlineStatus;

/**
 * 在线用户会话属性
 * 
 * @author gsh
 */
public class OnlineSession extends SimpleSession {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 用户名称 */
    private String loginName;

    /** 部门名称 */
    private String deptName;

    /** 用户头像 */
    private String avatar;

    /** 登录IP地址 */
    private String host;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 在线状态 */
    private OnlineStatus status = OnlineStatus.on_line;

    /** 属性是否改变 优化session数据同步 */
    private transient boolean attributeChanged = false;

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
        this.markAttributeChanged();
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
        this.markAttributeChanged();
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
        this.markAttributeChanged();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        this.markAttributeChanged();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
        this.markAttributeChanged();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
        this.markAttributeChanged();
    }

    public OnlineStatus getStatus() {
        return status;
    }

    public void setStatus(OnlineStatus status) {
        this.status = status;
        this.markAttributeChanged();
    }

    public void markAttributeChanged() {
        this.attributeChanged = true;
    }

    public void resetAttributeChanged() {
        this.attributeChanged = false;
    }

    public boolean isAttributeChanged() {
        return attributeChanged;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        this.markAttributeChanged();
    }

    @Override
    public void setAttribute(Object key, Object value) {
        super.setAttribute(key, value);
        if (needMarkAttributeChanged(key)) {
            this.markAttributeChanged();
        }
    }

    @Override
    public Object removeAttribute(Object key) {
        Object removed = super.removeAttribute(key);
        if (needMarkAttributeChanged(key)) {
            this.markAttributeChanged();
        }
        return removed;
    }
    
    @Override
    public void setId(Serializable id) {
        super.setId(id);
        this.markAttributeChanged();
    }

    @Override
    public void setStopTimestamp(Date stopTimestamp) {
        super.setStopTimestamp(stopTimestamp);
        this.markAttributeChanged();
    }

    @Override
    public void setExpired(boolean expired) {
        super.setExpired(expired);
        this.markAttributeChanged();
    }

    @Override
    public void setTimeout(long timeout) {
        super.setTimeout(timeout);
        this.markAttributeChanged();
    }
    
    /**
     * 停止
     */
    @Override
    public void stop() {
        super.stop();
        this.markAttributeChanged();
    }

    /**
     * 设置过期
     */
    @Override
    protected void expire() {
        this.stop();
        this.setExpired(true);
    }

    private boolean needMarkAttributeChanged(Object attributeKey) {
        if (attributeKey == null) {
            return false;
        }
        String attributeKeyStr = attributeKey.toString();
        // 优化 flash属性没必要持久化
        if (attributeKeyStr.startsWith("org.springframework")) {
            return false;
        }
        if (attributeKeyStr.startsWith("javax.servlet")) {
            return false;
        }
        if (attributeKeyStr.equals(ShiroConstants.CURRENT_USERNAME)) {
            return false;
        }
        return true;
    }

}
