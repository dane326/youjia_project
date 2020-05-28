package com.gsh.framework.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 *  wuyingkai 2019/11/7.
 */
public class DefaultUsernamepasswordToken extends UsernamePasswordToken {
    /**
     * 判断登录类型
     */
    private String loginType;

    public String getLoginType() {
        return this.loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
    /**
     * Shiro 构造方法
     */
    public DefaultUsernamepasswordToken(String username, String password,Boolean remeberMe) {
        super(username, password,remeberMe);
    }

    public DefaultUsernamepasswordToken() {

    }

}
