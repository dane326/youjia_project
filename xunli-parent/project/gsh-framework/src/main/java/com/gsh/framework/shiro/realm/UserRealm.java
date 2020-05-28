package com.gsh.framework.shiro.realm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.gsh.common.config.Global;
import com.gsh.common.exception.user.CaptchaException;
import com.gsh.common.exception.user.RoleBlockedException;
import com.gsh.common.exception.user.UserBlockedException;
import com.gsh.common.exception.user.UserNotExistsException;
import com.gsh.common.exception.user.UserPasswordNotMatchException;
import com.gsh.common.exception.user.UserPasswordRetryLimitExceedException;
import com.gsh.common.utils.StringUtils;
import com.gsh.framework.redis.service.RedisUserOnlineService;
import com.gsh.framework.shiro.service.SysLoginService;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysMenu;
import com.gsh.system.domain.SysUser;
import com.gsh.system.service.ISysVipService;
import com.gsh.system.service.ISysMenuService;
import com.gsh.system.service.ISysRoleService;
import com.gsh.system.utils.FactoryUtils;

/**
 * 自定义Realm 处理登录 权限
 * 
 * @author gsh
 */
public class UserRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private SysLoginService loginService;
    
    @Autowired
    private RedisUserOnlineService redisUserOnlineService;
    
    @Autowired(required = false)
    private ISysVipService sysVipService;

    // session存储位置
    @Value("${gsh.onlineSessionStore}")
    private String store;

    /**
     * @param authenticationCacheName 认证
     * @param authorizationCacheName 鉴权
     */
    public UserRealm(String authenticationCacheName, String authorizationCacheName) {
        if (StringUtils.isNotEmpty(authenticationCacheName)) {
            super.setAuthenticationCacheName(authenticationCacheName);
        }
        if (StringUtils.isNotEmpty(authorizationCacheName)) {
            super.setAuthorizationCacheName(authorizationCacheName);
        }
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        //判断是考生还是考官登录
        // String loginType = ShiroUtils.getSubject().getSession().getAttribute("loginType") == null ? "" : ShiroUtils.getSubject().getSession().getAttribute("loginType").toString();

        SysUser user = ShiroUtils.getSysUser();
        // 角色列表
        Set<String> roles = new HashSet<String>();
        // 功能列表
        Set<String> menus = new HashSet<String>();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 管理员拥有所有权限0
        if (user.isAdmin()) {
            info.addRole("admin");
            info.addStringPermission("*:*:*");
        } else {
            roles = roleService.selectRoleKeys(user.getUserId());
            // 角色加入AuthorizationInfo认证对象
            info.setRoles(roles);

            menus = this.menuService.selectPermsByUserId(user.getUserId());

            // 权限加入AuthorizationInfo认证对象
            info.setStringPermissions(menus);
        }
        return info;
    }

    public Set<String> dealMenus(List<SysMenu> menus) {
        Set<String> permsSet = new HashSet<>();
        for (SysMenu perm : menus) {
            if (StringUtils.isNotEmpty(perm.getPerms())) {
                permsSet.addAll(Arrays.asList(perm.getPerms().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = "";
        if (upToken.getPassword() != null) {
            password = new String(upToken.getPassword());
        }

        SysUser user = null;
        try {
            user = loginService.login(username, password);
            
            // 进行vip验证
            if(sysVipService != null){
            	sysVipService.checkVipOnlineusersNum(Global.getContextPathNoSeparator(), FactoryUtils.getFactoryCode(user));
            }            
        } catch (CaptchaException e) {
            throw new AuthenticationException(e.getMessage(), e);
        } catch (UserNotExistsException e) {
            throw new UnknownAccountException(e.getMessage(), e);
        } catch (UserPasswordNotMatchException e) {
            throw new IncorrectCredentialsException(e.getMessage(), e);
        } catch (UserPasswordRetryLimitExceedException e) {
            throw new ExcessiveAttemptsException(e.getMessage(), e);
        } catch (UserBlockedException e) {
            throw new LockedAccountException(e.getMessage(), e);
        } catch (RoleBlockedException e) {
            throw new LockedAccountException(e.getMessage(), e);
        } catch (Exception e) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
            throw new AuthenticationException(e.getMessage(), e);
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());

        // 添加在线用户
        if("redis".equals(store)){
        	try {
        		redisUserOnlineService.addOnlineUser(user, SecurityUtils.getSubject().getSession());
        	}catch (Exception e) {
        		log.info("redis添加在线用户失败");
			}
        }

        return info;
    }

    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}