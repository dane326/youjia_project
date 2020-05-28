package com.gsh.framework.redis.dao;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

/**
 * redis
 * 
 * @author gsh
 */
@Component
public class RedisShiroDAO {
    @SuppressWarnings("rawtypes")
//    @Resource(name = "shiroRedisTemplate")
    @Autowired(required = false)
    private RedisTemplate shiroRedisTemplate;

    @SuppressWarnings("rawtypes")
    public RedisTemplate getShiroRedisTemplate() {
        return shiroRedisTemplate;
    }
    
    @SuppressWarnings("unchecked")
    public ValueOperations<String, Session> getShiroSessionValueOperations(){
        return this.shiroRedisTemplate.opsForValue();
    }
    
    @SuppressWarnings("unchecked")
    public ValueOperations<String, String> getShiroValueOperations(){
        return this.shiroRedisTemplate.opsForValue();
    }
    
    @SuppressWarnings("unchecked")
    public HashOperations<String, String, Object> getShiroHashOperations(){
        return this.shiroRedisTemplate.opsForHash();
    }
    
    @SuppressWarnings("unchecked")
    public ZSetOperations<String, String> getShiroZSetOperations(){
        return this.shiroRedisTemplate.opsForZSet();
    }
    
    @SuppressWarnings("unchecked")
    public SetOperations<String, String> getShiroSetOperations(){
        return this.shiroRedisTemplate.opsForSet();
    }
}
