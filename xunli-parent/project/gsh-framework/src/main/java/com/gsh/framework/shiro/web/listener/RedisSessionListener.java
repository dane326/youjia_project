package com.gsh.framework.shiro.web.listener;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gsh.framework.redis.ShiroKeyConstants;
import com.gsh.framework.shiro.session.RedisSessionDAO;

/**
 * 会话过期
 * 
 * @author gsh
 */
public class RedisSessionListener extends SessionListenerAdapter {
    private Logger log = LoggerFactory.getLogger(getClass());
    
    private RedisSessionDAO sessionDao;
    private CacheManager cacheManager;
    
    public RedisSessionListener(RedisSessionDAO sessionDao, CacheManager cacheManager){
        this.sessionDao = sessionDao;
        this.cacheManager = cacheManager;
    }

    /*
     * 会话过期时触发
     * 因session不能序列化，故以json传输
     */
    @Override
    public void onExpiration(Session session) {
        log.debug("会话过期：" + session.getId());

        if (session != null && session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
            //删除session
            this.sessionDao.delete(session);

            PrincipalCollection principals = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            //清除权限缓存
            this.cacheManager.getCache(ShiroKeyConstants.REDIS_CACHE_AUTHOR_KEY_PREFIX).remove(principals);
            
        }
    }

    /*
     * 退出/会话过期时触发
     * 因session不能序列化，故以json传输
     */
    @Override
    public void onStop(Session session) {       
        log.debug("会话停止：" + session.getId());
        if (session != null && session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
          //删除session
            this.sessionDao.delete(session);

            PrincipalCollection principals = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            //清除权限缓存
            this.cacheManager.getCache(ShiroKeyConstants.REDIS_CACHE_AUTHOR_KEY_PREFIX).remove(principals);
        }
    }
}