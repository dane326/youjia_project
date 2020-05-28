package com.gsh.framework.shiro.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gsh.framework.redis.service.RedisUserOnlineService;

/**
 * 定时任务调度
 * 
 * @author gsh
 */
@Component("redisSessionTask")
public class RedisSessionTask {

    @Value("${gsh.framworkCacheMode}")
    private String cacheMode;

    @Autowired
    private RedisUserOnlineService redisUserOnlineService;

    /**
     * 扫描前缀为REDIS_SHIRO_SESSION_USER的key
     */
    public void ryNoParams() {
        if("redis".equals(cacheMode)) {
            redisUserOnlineService.scanRedisSession();
        }
    }
}
