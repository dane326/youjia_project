package com.gsh.framework.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.gsh.framework.redis.dao.RedisShiroDAO;


/**
 * 
 * @author Administrator
 *
 */
public class RedisCacheManager implements CacheManager {
    @Autowired
    private RedisShiroDAO redisShiroDAO;
    
    // expire time in minutes
    private int expire = 30;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new RedisCache<K, V>(name, redisShiroDAO.getShiroRedisTemplate(), expire);
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
    
}