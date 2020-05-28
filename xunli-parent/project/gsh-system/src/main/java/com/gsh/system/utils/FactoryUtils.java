package com.gsh.system.utils;

import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.gsh.common.config.FactoryConfig;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.bean.BeanUtils;
import com.gsh.system.domain.SysUser;

public class FactoryUtils {
	public static Long getFactoryCode(){
        return getFactoryCode(getSysUser());
    }
	
	public static Long getFactoryCode(SysUser user){
        return user!=null && user.getDept() !=null ? user.getDept().getFactoryCode() : null;
    }

    public static Subject getSubject()
    {
        return SecurityUtils.getSubject();
    }

    public static Session getSession()
    {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout()
    {
        getSubject().logout();
    }

    public static Boolean isPlatformFactoryCode(){
        return FactoryConfig.getPlatformFactoryCode().equals(getFactoryCode());
    }

    public static SysUser getSysUser()
    {
        SysUser user = null;
        Object obj = getSubject().getPrincipal();
        if (StringUtils.isNotNull(obj))
        {
            user = new SysUser();
            BeanUtils.copyBeanProp(user, obj);
        }
        return user;
    }
    /**
     * 生成随机盐
     */
    public static String randomSalt()
    {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        return hex;
    }

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }


    public static boolean matches(SysUser user, String newPassword)
    {
        return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
    }

    public static String encryptPassword(String username, String password, String salt)
    {
        return new Md5Hash(username + password + salt).toHex().toString();
    }
    
    /**
     * 是否需要显示租户树
     * @return
     */
    public static Boolean getFactoryTreeMode(){
    	return FactoryConfig.getFactoryDisplayMode() && getFactoryCode().equals(FactoryConfig.getPlatformFactoryCode());
    }
}
