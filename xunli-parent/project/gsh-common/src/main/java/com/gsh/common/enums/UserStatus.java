package com.gsh.common.enums;

/**
 * 用户状态
 * 
 * @author gsh
 */
public enum UserStatus
{
    OK("0", "正常"), DISABLE("1", "停用"), DELETED("2", "删除");

    private final String dict;
    private final String info;

    UserStatus(String code, String info)
    {
        this.dict = code;
        this.info = info;
    }

    public String getCode()
    {
        return dict;
    }

    public String getInfo()
    {
        return info;
    }
}
