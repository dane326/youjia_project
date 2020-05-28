/**
 * 
 */
package com.gsh.framework.redis.service;

import com.google.common.collect.Sets;
import com.gsh.common.utils.StringUtils;
import com.gsh.framework.redis.ShiroKeyConstants;
import com.gsh.framework.redis.dao.RedisShiroDAO;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysUser;
import com.gsh.system.domain.SysUserOnline;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Cason
 * 
 * @Date May 17, 2012
 */
@Component("redisUserOnlineService")
public class RedisUserOnlineService {
    private static final Logger logger = LoggerFactory.getLogger(RedisUserOnlineService.class);

    @Autowired
    private RedisShiroDAO redisShiroDAO;

    /**
     * 从redis中删信息
     * 
     * @param uniqueKey factoryCode + "::" + sessionId
     */
    @SuppressWarnings("unchecked")
    public void removeUser(String uniqueKey, String sessionId) {
        if (StringUtils.isNoneBlank(uniqueKey)) {
            String[] uniqueKeys = uniqueKey.split("::", -1);
            // 删除在线信息
            redisShiroDAO.getShiroHashOperations().getOperations().delete(ShiroKeyConstants.REDIS_SHIRO_ONLINE_IFNO + uniqueKey);
            // 从在线列表中删除用户
            redisShiroDAO.getShiroSetOperations().remove(ShiroKeyConstants.REDIS_SHIRO_ONLINE_LIST + uniqueKeys[0], sessionId);
            // 删除用户权限
            if (redisShiroDAO.getShiroRedisTemplate().hasKey(ShiroKeyConstants.REDIS_CACHE_AUTHOR_KEY_PREFIX + uniqueKey)) {
                redisShiroDAO.getShiroValueOperations().getOperations().delete(ShiroKeyConstants.REDIS_CACHE_AUTHOR_KEY_PREFIX + uniqueKey);
            }
        }

        if (StringUtils.isNoneBlank(sessionId)) {
            // 删除sessionid与用户关系
            redisShiroDAO.getShiroValueOperations().getOperations().delete(ShiroKeyConstants.REDIS_SHIRO_SESSION_USER + sessionId);

            // 删除session
            if (redisShiroDAO.getShiroRedisTemplate().hasKey(ShiroKeyConstants.REDIS_SESSION_KEY + sessionId)) {
                redisShiroDAO.getShiroRedisTemplate().delete(ShiroKeyConstants.REDIS_SESSION_KEY + sessionId);
            }
        }
    }

    /**
     * 从redis中删信息
     * 
     * @param user
     */
    @SuppressWarnings("unchecked")
    public void removeUser(SysUser user, String sessionId) {
        if (StringUtils.isNoneBlank(sessionId) && user != null) {
            String factoryCode = ShiroUtils.getFactoryCode(user);
            String uniqueKey = factoryCode + "::" + sessionId;
            // 删除在线信息
            redisShiroDAO.getShiroHashOperations().getOperations().delete(ShiroKeyConstants.REDIS_SHIRO_ONLINE_IFNO + uniqueKey);
            // 从在线列表中删除用户
            redisShiroDAO.getShiroSetOperations().remove(ShiroKeyConstants.REDIS_SHIRO_ONLINE_LIST + factoryCode, sessionId);

            // 删除用户权限
            if (redisShiroDAO.getShiroRedisTemplate().hasKey(ShiroKeyConstants.REDIS_CACHE_AUTHOR_KEY_PREFIX + uniqueKey)) {
                redisShiroDAO.getShiroValueOperations().getOperations().delete(ShiroKeyConstants.REDIS_CACHE_AUTHOR_KEY_PREFIX + uniqueKey);
            }
        }

        if (StringUtils.isNoneBlank(sessionId)) {
            // 删除sessionid与用户关系
            redisShiroDAO.getShiroValueOperations().getOperations().delete(ShiroKeyConstants.REDIS_SHIRO_SESSION_USER + sessionId);

            // 删除session
            if (redisShiroDAO.getShiroRedisTemplate().hasKey(ShiroKeyConstants.REDIS_SESSION_KEY + sessionId)) {
                redisShiroDAO.getShiroRedisTemplate().delete(ShiroKeyConstants.REDIS_SESSION_KEY + sessionId);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public String getSessionId(SysUser user) {
        String uniqueKey = ShiroKeyConstants.REDIS_SHIRO_ONLINE_IFNO + ShiroUtils.getUniqueKey("");
        if (redisShiroDAO.getShiroRedisTemplate().hasKey(uniqueKey)) {
            return (String) redisShiroDAO.getShiroHashOperations().get(uniqueKey, "sessionId");
        }
        return "";
    }

    /**
     * 根据sessionIdKey获取
     * 
     * @param sessionId
     * @return factoryCode + "::" + sessionId
     */
    public String getUniqueKey(String sessionId) {
        return redisShiroDAO.getShiroValueOperations().get(ShiroKeyConstants.REDIS_SHIRO_SESSION_USER + sessionId);
    }

    /**
     * 在线列表中添加用户(指定factoryCode)
     * 
     * @param user
     */
    public void addOnlineUser(SysUser user, Session session) {
        String factoryCode = ShiroUtils.getFactoryCode(user);
        String sessionId = session.getId().toString();
        // 添加sessionid与user对应关系
        redisShiroDAO.getShiroValueOperations().set(ShiroKeyConstants.REDIS_SHIRO_SESSION_USER + sessionId, factoryCode + "::" + sessionId);
        redisShiroDAO.getShiroSetOperations().add(ShiroKeyConstants.REDIS_SHIRO_ONLINE_LIST + factoryCode, sessionId);
    }

    /**
     * 统计在线用户人数(指定factoryCode)
     * 
     * @return
     */
    public Long getOnlineUserCount(String factoryCode) {
        return redisShiroDAO.getShiroSetOperations().size(ShiroKeyConstants.REDIS_SHIRO_ONLINE_LIST + factoryCode);
    }

    /**
     * 获取在线用户(指定factoryCode)
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<SysUserOnline> getOnlineUsers(SysUserOnline userOnline) {
		List<SysUserOnline> onlineUsers = new ArrayList<SysUserOnline>();
		Set<String> factoryCodes = Sets.newHashSet();
		if (userOnline.getFactoryCode() == null) {
			factoryCodes = (Set<String>) redisShiroDAO.getShiroRedisTemplate().execute(new RedisCallback<Set<String>>() {
				@Override
				public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
					Set<String> codes = Sets.newHashSet();
					// 放在try中自动释放cursor
					try (Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(ShiroKeyConstants.REDIS_SHIRO_ONLINE_LIST + "*").count(500).build())) {
						while (cursor.hasNext()) {
							codes.add(new String(cursor.next()).split("::", -1)[1]);
						}
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}

					return codes;
				}
			});
		} else {
			factoryCodes.add(userOnline.getFactoryCode() + "");
		}
		for (String factoryCode : factoryCodes) {
			String onlineListStr = ShiroKeyConstants.REDIS_SHIRO_ONLINE_LIST + factoryCode;
			Set<String> sessionIds = redisShiroDAO.getShiroSetOperations().members(onlineListStr);
			for (String sessionId : sessionIds) {
				SysUserOnline onlineUser = this.getOnelineByKey(ShiroKeyConstants.REDIS_SHIRO_ONLINE_IFNO + factoryCode + "::" + sessionId);
				if (onlineUser != null) {
					if (StringUtils.isBlank(userOnline.getLoginName()) || StringUtils.nvl(onlineUser.getLoginName(), "").indexOf(userOnline.getLoginName()) > -1) {
						if (StringUtils.isBlank(userOnline.getDeptName()) || StringUtils.nvl(onlineUser.getDeptName(), "").indexOf(userOnline.getDeptName()) > -1) {
							if (StringUtils.isBlank(userOnline.getIpaddr()) || StringUtils.nvl(onlineUser.getIpaddr(), "").indexOf(userOnline.getIpaddr()) > -1) {
								onlineUsers.add(onlineUser);
							}
						}
					}
				} else {
					// 如果redis不存在该在线信息，从set中删除
					redisShiroDAO.getShiroSetOperations().remove(onlineListStr, sessionId);
				}
			}
		}

		return onlineUsers;
	}

    /**
     * 强制退出
     */
    public void forceLogout(String sessionId) {
        // 根据sessionId根据用户信息, 删除用户信息
        this.removeUser(this.getUniqueKey(sessionId), sessionId);
    }

    /**
     * 根据key获取在线用户信息
     * 
     * @param key
     * @return
     */
    public SysUserOnline getOnelineByKey(String key) {
        if (redisShiroDAO.getShiroHashOperations().getOperations().hasKey(key)) {
            return (SysUserOnline) createHashMapper().fromHash(redisShiroDAO.getShiroHashOperations().entries(key));
        } else {
            return null;
        }
    }

    private HashMapper<Object, String, Object> createHashMapper() {
        return new Jackson2HashMapper(false);
    }

    /**
     * 根据sessionid获取SysUserOnline
     * 
     * @param sessionId
     * @return
     */
    public SysUserOnline selectOnlineById(String sessionId) {
        String uniqueKey = getUniqueKey(sessionId);
        SysUserOnline onlineUser = this.getOnelineByKey(ShiroKeyConstants.REDIS_SHIRO_ONLINE_IFNO + uniqueKey);
        return onlineUser;
    }

    /**
     * 扫描前缀为REDIS_SHIRO_SESSION_USER的key
     */
    @SuppressWarnings("unchecked")
    public void scanRedisSession() {
        redisShiroDAO.getShiroRedisTemplate().execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                // 放在try中自动释放cursor
                try (Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(ShiroKeyConstants.REDIS_SHIRO_SESSION_USER + "*").count(500).build())) {
                    while (cursor.hasNext()) {
                        String sessionId = new String(cursor.next()).split("::", -1)[1];
                        // 如果不存在session，就根据sessionId根据用户信息, 删除用户信息
                        if (!redisShiroDAO.getShiroRedisTemplate().hasKey(ShiroKeyConstants.REDIS_SESSION_KEY + sessionId)) {
                            logger.debug("过期键:" + sessionId);
                            removeUser(getUniqueKey(sessionId), sessionId);
                        }
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }

                return new HashSet<>();
            }
        });
    }
}
