package com.gsh.framework.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.gsh.framework.manager.AsyncManager;
import com.gsh.framework.manager.factory.AsyncFactory;
import com.gsh.framework.redis.ShiroKeyConstants;
import com.gsh.framework.redis.dao.RedisShiroDAO;

/*
 * 充分利用shiro的session校验机制（从request获取session时自动校验）
 * 参考AbstractValidatingSessionManager类
 */
public class RedisSessionDAO extends AbstractSessionDAO implements SyncOnlineSessionDAO {
    private static final Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

    @Autowired
    private RedisShiroDAO redisShiroDAO;

    /**
     * The Redis key prefix for the sessions
     */
    private String keyPrefix = ShiroKeyConstants.REDIS_SESSION_KEY;

    /**
     * doReadSession be called about 10 times when login.
     * Save Session in ThreadLocal to resolve this problem.
     * sessionInMemoryTimeout is expiration of Session in ThreadLocal.
     * The default value is 1000 milliseconds (1s).
     * Most of time, you don't need to change it.
     */
    private long sessionInMemoryTimeout = 1000L;

    private boolean sessionInMemoryEnabled = true;
    @SuppressWarnings("rawtypes")
    private static ThreadLocal sessionsInThread = new ThreadLocal();

    /**
     * 同步session到数据库的周期 单位为毫秒（默认1分钟）
     */
    @Value("${shiro.session.dbSyncPeriod}")
    private int dbSyncPeriod;

    /**
     * 上次同步数据库的时间戳
     */
    private static final String LAST_SYNC_DB_TIMESTAMP = RedisSessionDAO.class.getName() + "LAST_SYNC_DB_TIMESTAMP";

    private String getKey(Serializable sessionId) {
        return keyPrefix + sessionId.toString();
    }

    private String getKey(Session session) {
        return keyPrefix + session.getId().toString();
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        OnlineSession onlineSession = (OnlineSession) session;
        // 如果会话过期/停止 没必要再更新了
        if (onlineSession instanceof ValidatingSession && !((ValidatingSession) onlineSession).isValid()) {
            return;
        }

        // 更新完后 重置标识
        if (!onlineSession.isAttributeChanged()) {
            return;
        }
        onlineSession.resetAttributeChanged();

        if (this.sessionInMemoryEnabled) {
            this.setSessionToThreadLocal(session.getId(), session);
        }

        String key = getKey(session);
        logger.debug("update session " + key);
        redisShiroDAO.getShiroSessionValueOperations().set(key, session, session.getTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void delete(Session session) {
        redisShiroDAO.getShiroSessionValueOperations().getOperations().delete(getKey(session));
    }

    @Override
    public Collection<Session> getActiveSessions() {
        /**
         * 可能影响性能
         */
        List<Session> values = redisShiroDAO.getShiroSessionValueOperations().multiGet(redisShiroDAO.getShiroSessionValueOperations().getOperations().keys(keyPrefix + "*"));
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptySet();
        } else {
            return values;
        }
    }

    protected void assignSessionId(Session session, Serializable sessionId) {
        ((OnlineSession) session).setId(sessionId.toString().replace("-", ""));
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);

        assignSessionId(session, sessionId);
        update(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            logger.warn("session id is null");
            return null;
        }

        if (this.sessionInMemoryEnabled) {
            Session session = getSessionFromThreadLocal(sessionId);
            if (session != null) {
                return session;
            }
        }

        String key = getKey(sessionId);
        logger.debug("read session " + key);
        return (OnlineSession) redisShiroDAO.getShiroSessionValueOperations().get(key);
    }

    @SuppressWarnings("unchecked")
    private void setSessionToThreadLocal(Serializable sessionId, Session s) {
        Map<Serializable, SessionInMemory> sessionMap = (Map<Serializable, SessionInMemory>) sessionsInThread.get();
        if (sessionMap == null) {
            sessionMap = new HashMap<Serializable, SessionInMemory>();
            sessionsInThread.set(sessionMap);
        }

        removeExpiredSessionInMemory(sessionMap);

        SessionInMemory sessionInMemory = new SessionInMemory();
        sessionInMemory.setCreateTime(new Date());
        sessionInMemory.setSession(s);
        sessionMap.put(sessionId, sessionInMemory);
    }

    private void removeExpiredSessionInMemory(Map<Serializable, SessionInMemory> sessionMap) {
        Iterator<Serializable> it = sessionMap.keySet().iterator();
        while (it.hasNext()) {
            Serializable sessionId = it.next();
            SessionInMemory sessionInMemory = sessionMap.get(sessionId);
            if (sessionInMemory == null) {
                it.remove();
                continue;
            }
            long liveTime = getSessionInMemoryLiveTime(sessionInMemory);
            if (liveTime > sessionInMemoryTimeout) {
                it.remove();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Session getSessionFromThreadLocal(Serializable sessionId) {

        if (sessionsInThread.get() == null) {
            return null;
        }

        Map<Serializable, SessionInMemory> sessionMap = (Map<Serializable, SessionInMemory>) sessionsInThread.get();
        SessionInMemory sessionInMemory = sessionMap.get(sessionId);
        if (sessionInMemory == null) {
            return null;
        }
        long liveTime = getSessionInMemoryLiveTime(sessionInMemory);
        if (liveTime > sessionInMemoryTimeout) {
            sessionMap.remove(sessionId);
            return null;
        }

        // logger.debug("read session from memory");
        return sessionInMemory.getSession();
    }

    private long getSessionInMemoryLiveTime(SessionInMemory sessionInMemory) {
        Date now = new Date();
        return now.getTime() - sessionInMemory.getCreateTime().getTime();
    }

    public long getSessionInMemoryTimeout() {
        return sessionInMemoryTimeout;
    }

    public void setSessionInMemoryTimeout(long sessionInMemoryTimeout) {
        this.sessionInMemoryTimeout = sessionInMemoryTimeout;
    }

    public boolean isSessionInMemoryEnabled() {
        return sessionInMemoryEnabled;
    }

    public void setSessionInMemoryEnabled(boolean sessionInMemoryEnabled) {
        this.sessionInMemoryEnabled = sessionInMemoryEnabled;
    }

    /**
     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
     */
    public void syncToDb(OnlineSession onlineSession) {
        Date lastSyncTimestamp = (Date) onlineSession.getAttribute(LAST_SYNC_DB_TIMESTAMP);
        if (lastSyncTimestamp != null) {
            boolean needSync = true;
            long deltaTime = onlineSession.getLastAccessTime().getTime() - lastSyncTimestamp.getTime();
            if (deltaTime < dbSyncPeriod * 60 * 1000) {
                // 时间差不足 无需同步
                needSync = false;
            }
            // isGuest = true 访客
            boolean isGuest = onlineSession.getUserId() == null || onlineSession.getUserId() == 0L;

            // session 数据变更了 同步
            if (isGuest == false && onlineSession.isAttributeChanged()) {
                needSync = true;
            }

            if (needSync == false) {
                return;
            }
        }
        // 更新上次同步数据库时间
        onlineSession.setAttribute(LAST_SYNC_DB_TIMESTAMP, onlineSession.getLastAccessTime());
        this.update(onlineSession);
        AsyncManager.me().execute(AsyncFactory.syncSessionToRedis(onlineSession, redisShiroDAO.getShiroHashOperations()));
    }
}