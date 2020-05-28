package com.gsh.common.exception.user;

/**
 * 用户密码不正确或不符合规范异常类
 * 
 * @author gsh
 */
public class UserPasswordLengthNotMatchException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserPasswordLengthNotMatchException()
    {
        super("user.password.length.not.match", null);
    }
}
