package com.gsh.framework.shiro.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gsh.common.utils.spring.SpringUtils;
import com.gsh.framework.redis.ShiroKeyConstants;
import com.gsh.framework.redis.service.RedisUserOnlineService;

/**
 * 通过redis的channel来监听过期事件
 * 
 * @author gsh
 */
public class RedisMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

    public void handleMessage(Object message, String channel) {
        if (channel.endsWith(":expired")) {
            String sessionId = String.valueOf(message);
            if (sessionId.startsWith(ShiroKeyConstants.REDIS_SESSION_KEY)) {
                logger.debug("session过期 " + sessionId);
                sessionId = sessionId.split("::", -1)[1];
                RedisUserOnlineService redisUserOnlineService = (RedisUserOnlineService) SpringUtils.getBean("redisUserOnlineService");
                // 根据sessionId根据用户信息, 删除用户信息
                redisUserOnlineService.removeUser(redisUserOnlineService.getUniqueKey(sessionId), sessionId);
            }
        }
    }
}