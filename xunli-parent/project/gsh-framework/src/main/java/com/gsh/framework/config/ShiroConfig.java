package com.gsh.framework.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gsh.common.utils.StringUtils;
import com.gsh.framework.redis.ShiroKeyConstants;
import com.gsh.framework.shiro.cache.RedisCacheManager;
import com.gsh.framework.shiro.realm.UserRealm;
import com.gsh.framework.shiro.session.OnlineSessionDAO;
import com.gsh.framework.shiro.session.OnlineSessionFactory;
import com.gsh.framework.shiro.session.RedisSessionDAO;
import com.gsh.framework.shiro.web.filter.LogoutFilter;
import com.gsh.framework.shiro.web.filter.captcha.CaptchaValidateFilter;
import com.gsh.framework.shiro.web.filter.kickout.KickoutSessionFilter;
import com.gsh.framework.shiro.web.filter.kickout.RedisKickoutSessionFilter;
import com.gsh.framework.shiro.web.filter.online.OnlineSessionFilter;
import com.gsh.framework.shiro.web.filter.sync.SyncOnlineSessionFilter;
import com.gsh.framework.shiro.web.listener.RedisSessionListener;
import com.gsh.framework.shiro.web.session.RedisSessionManager;
import com.gsh.framework.shiro.web.session.SpringSessionValidationScheduler;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * 权限配置加载
 *
 * @author gsh
 */
@Configuration
public class ShiroConfig {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    // Session超时时间，单位为毫秒（默认30分钟）
    @Value("${shiro.session.expireTime}")
    private int expireTime;

    // 相隔多久检查一次session的有效性，单位毫秒，默认就是10分钟
    @Value("${shiro.session.validationInterval}")
    private int validationInterval;

    // 同一个用户最大会话数
    @Value("${shiro.session.maxSession}")
    private int maxSession;

    // 踢出之前登录的/之后登录的用户，默认踢出之前登录的用户
    @Value("${shiro.session.kickoutAfter}")
    private boolean kickoutAfter;

    // 验证码开关
    @Value("${shiro.user.captchaEnabled}")
    private boolean captchaEnabled;

    // 移动设备登录验证码开关
    @Value("${shiro.user.mobileCaptchaEnabled}")
    private boolean mobileCaptchaEnabled;

    // 验证码类型
    @Value("${shiro.user.captchaType}")
    private String captchaType;

    // 设置Cookie的域名
    @Value("${shiro.cookie.domain}")
    private String domain;

    // 设置cookie的有效访问路径
    @Value("${shiro.cookie.path}")
    private String path;

    // 设置HttpOnly属性
    @Value("${shiro.cookie.httpOnly}")
    private boolean httpOnly;

    // 设置Cookie的过期时间，秒为单位
    @Value("${shiro.cookie.maxAge}")
    private int maxAge;

    // 登录地址
    @Value("${shiro.user.loginUrl}")
    private String loginUrl;

    // 权限认证失败地址
    @Value("${shiro.user.unauthorizedUrl}")
    private String unauthorizedUrl;

    // session存储位置
    @Value("${gsh.onlineSessionStore}")
    private String store;

    /**
     * 返回配置文件流 避免ehcache配置文件一直被占用，无法完全销毁项目重新部署
     */
    protected InputStream getCacheManagerConfigFileInputStream() {
        String configFile = "classpath:ehcache/ehcache-shiro.xml";
        InputStream inputStream = null;
        try {
            inputStream = ResourceUtils.getInputStreamForPath(configFile);
            byte[] b = IOUtils.toByteArray(inputStream);
            InputStream in = new ByteArrayInputStream(b);
            return in;
        } catch (IOException e) {
            throw new ConfigurationException("Unable to obtain input stream for cacheManagerConfigFile [" + configFile + "]", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 缓存管理器 使用Ehcache实现
     */
    @Bean
    @ConditionalOnProperty(name = "gsh.onlineSessionStore", havingValue = "db")
    public CacheManager getEhCacheManager() {
        net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.getCacheManager("gsh");
        EhCacheManager em = new EhCacheManager();
        if (StringUtils.isNull(cacheManager)) {
            em.setCacheManager(new net.sf.ehcache.CacheManager(getCacheManagerConfigFileInputStream()));
            return em;
        } else {
            em.setCacheManager(cacheManager);
            return em;
        }
    }

    /**
     * 缓存管理器
     * 
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager shiroCacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setExpire(expireTime);
        return cacheManager;
    }

    /**
     * 自定义Realm
     */
    @Bean
    public UserRealm userRealm(CacheManager cacheManager) {
        UserRealm userRealm = new UserRealm(ShiroKeyConstants.REDIS_CACHE_AUTHEN_KEY_PREFIX, ShiroKeyConstants.REDIS_CACHE_AUTHOR_KEY_PREFIX);
        userRealm.setCacheManager(cacheManager);

        //开启认证缓存
        //userRealm.setAuthenticationCachingEnabled(true);
        return userRealm;
    }

    /**
     * 自定义sessionDAO会话
     */
    @Bean
    @ConditionalOnProperty(name = "gsh.onlineSessionStore", havingValue = "db")
    public AbstractSessionDAO sessionDAO() {
        return new OnlineSessionDAO();
    }

    @Bean
    @ConditionalOnMissingBean(AbstractSessionDAO.class)
    public AbstractSessionDAO redisSessionDAO() {
        return new RedisSessionDAO();
    }

    /**
     * 自定义sessionFactory会话
     */
    @Bean
    public OnlineSessionFactory sessionFactory() {
        return new OnlineSessionFactory();
    }

    /**
     * 自定义sessionFactory调度器
     */
    @Bean
    public SpringSessionValidationScheduler sessionValidationScheduler(ValidatingSessionManager sessionManager) {
        return new SpringSessionValidationScheduler(sessionManager);
    }

    /**
     * 会话管理器
     */
    @Bean
    public DefaultWebSessionManager sessionManager(CacheManager cacheManager, AbstractSessionDAO sessionDAO) {
        DefaultWebSessionManager manager = new RedisSessionManager();
        // 加入缓存管理器
        manager.setCacheManager(cacheManager);
        // 删除过期的session
        manager.setDeleteInvalidSessions(true);
        // 设置全局session超时时间
        manager.setGlobalSessionTimeout(expireTime * 60 * 1000);
        // 去掉 JSESSIONID
        manager.setSessionIdUrlRewritingEnabled(false);
        // 是否定时检查session
        if ("db".equals(store)) {
            manager.setSessionValidationSchedulerEnabled(true);
        }
        // 自定义SessionDao
        manager.setSessionDAO(sessionDAO);
        // 自定义sessionFactory
        manager.setSessionFactory(sessionFactory());

        return manager;
    }

    /**
     * 安全管理器
     */
    @Bean
    public DefaultWebSecurityManager securityManager(UserRealm userRealm, CacheManager cacheManager, DefaultWebSessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(userRealm);
        // 记住我
        securityManager.setRememberMeManager(rememberMeManager());
        // 注入缓存管理器;
        securityManager.setCacheManager(cacheManager);
        // session管理器
        // 定义要使用的无效的Session定时调度器 (避免循环引用)
        if ("db".equals(store)) {
            sessionManager.setSessionValidationScheduler(sessionValidationScheduler(sessionManager));
        }

        // 自定义session过期监听器
        if ("redis".equals(store)) {
            RedisSessionListener redisSessionListener = new RedisSessionListener((RedisSessionDAO) sessionManager.getSessionDAO(), cacheManager);
            sessionManager.setSessionListeners(Arrays.asList(redisSessionListener));
        }
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * 退出过滤器
     */
    public LogoutFilter logoutFilter(CacheManager cacheManager) {
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setCacheManager(cacheManager);
        logoutFilter.setLoginUrl(loginUrl);
        return logoutFilter;
    }

    /**
     * Shiro过滤器配置
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // Shiro的核心安全接口,这个属性是必须的
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 身份认证失败，则跳转到登录页面的配置
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        // 权限认证失败，则跳转到指定页面
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        // Shiro连接约束配置，即过滤链的定义
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 对静态资源设置匿名访问
        filterChainDefinitionMap.put("/favicon.ico**", "anon");
        filterChainDefinitionMap.put("/gsh.png**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/docs/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/ajax/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/gsh/**", "anon");
        filterChainDefinitionMap.put("/alipay/**", "anon");
        filterChainDefinitionMap.put("/captcha/captchaImage**", "anon");
        // 注册相关
        filterChainDefinitionMap.put("/register", "anon,captchaValidate");
        // 退出 logout地址，shiro去清除session
        filterChainDefinitionMap.put("/logout", "logout");
        // 不需要拦截的访问
        filterChainDefinitionMap.put("/login", "anon,captchaValidate");
        filterChainDefinitionMap.put("/register", "anon,captchaValidate");
        filterChainDefinitionMap.put("/resetPassword", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/factory/info/register", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/factory/info/resetPassword", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getValidateCode", "anon,captchaValidate");
        filterChainDefinitionMap.put("/userpaper/paper/generateUpaper", "anon,captchaValidate");
        filterChainDefinitionMap.put("/paper/paper/publicPaper", "anon,captchaValidate");
        filterChainDefinitionMap.put("/userpaper/mark/autoMarkUpaper", "anon,captchaValidate");
        filterChainDefinitionMap.put("/userpaper/mark/markSingleQ", "anon,captchaValidate");
        // 订单
        filterChainDefinitionMap.put("/info/*/price", "anon");
        filterChainDefinitionMap.put("/order/**", "anon");
        // 系统权限列表
        // filterChainDefinitionMap.putAll(SpringUtils.getBean(IMenuService.class).selectPermsAll());

        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("onlineSession", onlineSessionFilter());
        filters.put("syncOnlineSession", syncOnlineSessionFilter());
        if ("db".equals(store)) {
            filters.put("kickout", kickoutSessionFilter(securityManager));
        } else {
            filters.put("kickout", redisKickoutSessionFilter(securityManager));
        }
        filters.put("captchaValidate", captchaValidateFilter());

        // 注销成功，则跳转到指定页面
        filters.put("logout", logoutFilter(securityManager.getCacheManager()));
        shiroFilterFactoryBean.setFilters(filters);

        // 所有请求需要认证
        filterChainDefinitionMap.put("/**", "user,kickout,onlineSession,syncOnlineSession");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 自定义在线用户处理过滤器
     */
    @Bean
    public OnlineSessionFilter onlineSessionFilter() {
        OnlineSessionFilter onlineSessionFilter = new OnlineSessionFilter();
        onlineSessionFilter.setLoginUrl(loginUrl);
        return onlineSessionFilter;
    }

    /**
     * 自定义在线用户同步过滤器
     */
    @Bean
    public SyncOnlineSessionFilter syncOnlineSessionFilter() {
        return new SyncOnlineSessionFilter();
    }

    /**
     * 自定义验证码过滤器
     */
    @Bean
    public CaptchaValidateFilter captchaValidateFilter() {
        CaptchaValidateFilter captchaValidateFilter = new CaptchaValidateFilter();
        captchaValidateFilter.setCaptchaEnabled(captchaEnabled);
        captchaValidateFilter.setMobileCaptchaEnabled(mobileCaptchaEnabled);
        captchaValidateFilter.setCaptchaType(captchaType);
        return captchaValidateFilter;
    }

    /**
     * cookie 属性设置
     */
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(maxAge * 24 * 60 * 60);
        return cookie;
    }

    /**
     * 记住我
     */
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("fCq+/xW488hMTCD+cmJ3aQ=="));
        return cookieRememberMeManager;
    }

    /**
     * 同一个用户多设备登录限制
     */
    public KickoutSessionFilter kickoutSessionFilter(DefaultWebSecurityManager securityManager) {
        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
        kickoutSession(kickoutSessionFilter, securityManager);
        return kickoutSessionFilter;
    }

    /**
     * 同一个用户多设备登录限制 redis版
     */
    public KickoutSessionFilter redisKickoutSessionFilter(DefaultWebSecurityManager securityManager) {
        RedisKickoutSessionFilter kickoutSessionFilter = new RedisKickoutSessionFilter();
        kickoutSession(kickoutSessionFilter, securityManager);
        return kickoutSessionFilter;
    }

    private void kickoutSession(KickoutSessionFilter kickoutSessionFilter, DefaultWebSecurityManager securityManager) {
        kickoutSessionFilter.setCacheManager(securityManager.getCacheManager());
        kickoutSessionFilter.setSessionManager(securityManager.getSessionManager());
        // 同一个用户最大的会话数，默认-1无限制；比如2的意思是同一个用户允许最多同时两个人登录
        kickoutSessionFilter.setMaxSession(maxSession);
        // 是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序
        kickoutSessionFilter.setKickoutAfter(kickoutAfter);
        // 被踢出后重定向到的地址；
        kickoutSessionFilter.setKickoutUrl("/login?kickout=1");
    }

    /**
     * thymeleaf模板引擎和shiro框架的整合
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * 开启Shiro注解通知器
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
