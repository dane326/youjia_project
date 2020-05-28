package com.gsh.framework.manager.factory;

import com.gsh.common.constant.Constants;
import com.gsh.common.utils.AddressUtils;
import com.gsh.common.utils.ServletUtils;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.spring.SpringUtils;
import com.gsh.framework.redis.ShiroKeyConstants;
import com.gsh.framework.shiro.session.OnlineSession;
import com.gsh.framework.util.LogUtils;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysLogininfor;
import com.gsh.system.domain.SysOperLog;
import com.gsh.system.domain.SysUser;
import com.gsh.system.domain.SysUserOnline;
import com.gsh.system.service.ISysOperLogService;
import com.gsh.system.service.ISysUserOnlineService;
import com.gsh.system.service.impl.SysLogininforServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 异步工厂（产生任务用）
 * 
 * @author liuhulu
 *
 */
public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 同步session到数据库
     * 
     * @param session 在线用户会话
     * @return 任务task
     */
    public static TimerTask syncSessionToDb(final OnlineSession session) {
        return new TimerTask() {
            @Override
            public void run() {
                SysUserOnline online = new SysUserOnline();
                online.setSessionId(String.valueOf(session.getId()));
                online.setDeptName(session.getDeptName());
                online.setLoginName(session.getLoginName());
                online.setStartTimestamp(session.getStartTimestamp());
                online.setLastAccessTime(session.getLastAccessTime());
                online.setExpireTime(session.getTimeout());
                online.setIpaddr(session.getHost());
                online.setLoginLocation(AddressUtils.getRealAddressByIP(session.getHost()));
                online.setBrowser(session.getBrowser());
                online.setOs(session.getOs());
                online.setStatus(session.getStatus());
                PrincipalCollection principals = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                Object principalObject = principals.getPrimaryPrincipal();
                if (principalObject instanceof SysUser) {
                    SysUser user = (SysUser) principals.getPrimaryPrincipal();
                    if (user.getDept() != null) {
                        online.setFactoryCode(user.getDept().getFactoryCode());
                    }
                }
                SpringUtils.getBean(ISysUserOnlineService.class).saveOnline(online);

            }
        };
    }

    /**
     * 同步session到Redis
     * 
     * @param session 在线用户会话
     * @return 任务task
     */
    public static TimerTask syncSessionToRedis(final OnlineSession session, HashOperations<String, String, Object> hashOperations) {
        return new TimerTask() {
            @Override
            public void run() {
                if (hashOperations.getOperations().hasKey(ShiroKeyConstants.REDIS_SESSION_KEY + String.valueOf(session.getId()))) {
                    SysUserOnline online = new SysUserOnline();
                    online.setSessionId(String.valueOf(session.getId()));
                    online.setDeptName(session.getDeptName());
                    online.setLoginName(session.getLoginName());
                    online.setStartTimestamp(session.getStartTimestamp());
                    online.setLastAccessTime(session.getLastAccessTime());
                    online.setExpireTime(session.getTimeout());
                    online.setIpaddr(session.getHost());
                    online.setLoginLocation(AddressUtils.getRealAddressByIP(session.getHost()));
                    online.setBrowser(session.getBrowser());
                    online.setOs(session.getOs());
                    online.setStatus(session.getStatus());

                    String uniqueKey = session.getLoginName();
                    if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
                        PrincipalCollection principals = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                        Object principalObject = principals.getPrimaryPrincipal();
                        if (principalObject instanceof SysUser) {
                            SysUser user = (SysUser) principals.getPrimaryPrincipal();
                            if (user.getDept() != null) {
                                Long factoryCode = user.getDept().getFactoryCode();
                                online.setFactoryCode(user.getDept().getFactoryCode());
                                uniqueKey = String.valueOf(factoryCode) + "::" + online.getSessionId();
                            }
                        }
                    }

                    uniqueKey = ShiroKeyConstants.REDIS_SHIRO_ONLINE_IFNO + uniqueKey;
                    hashOperations.putAll(uniqueKey, createHashMapper().toHash(online));
                    hashOperations.getOperations().expire(uniqueKey, session.getTimeout(), TimeUnit.MILLISECONDS);

                    sys_user_logger.debug("同步在线信息");
                } else {
                    sys_user_logger.debug("无需同步在线信息");
                }
            }

        };
    }

    private static HashMapper<Object, String, Object> createHashMapper() {
        //return new DecoratingStringHashMapper<SysUserOnline>(new BeanUtilsHashMapper<SysUserOnline>(SysUserOnline.class));
        return new Jackson2HashMapper(false);
    }

    /**
     * 操作日志记录
     * 
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }

    /**
     * 记录登陆信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    /**
    public static TimerTask recordLogininfor(final String username, final String status, final String message, final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = ShiroUtils.getIp();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setLoginName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)){
                    logininfor.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(SysLogininforServiceImpl.class).insertLogininfor(logininfor);
            }
        };
    }
     */

    /**
     * 记录退出信息
     * @param factoryCode 租户编号
     * @param userName 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final Long factoryCode, final String userName, final String status, final String message, final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = ShiroUtils.getIp();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(userName));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setLoginName(userName);
                logininfor.setFactoryCode(factoryCode);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)){
                    logininfor.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(SysLogininforServiceImpl.class).insertLogininfor(logininfor);
            }
        };
    }

}