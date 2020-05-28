package com.gsh.framework.shiro.cache;

import com.gsh.framework.redis.ShiroKeyConstants;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用redis操作实现shiro缓存接口
 * 
 * @author
 *
 * @param <K>
 * @param <V>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedisCache<K, V> implements Cache<K, V> {

    private ValueOperations<String, V> valueOperations;

    private int expire = -1;

    public RedisCache(String prefix, RedisTemplate redisTemplate, int expire) {
        this.valueOperations = redisTemplate.opsForValue();

        if (StringUtils.isNotBlank(prefix)) {
            this.keyPrefix = prefix;
        }

        if (expire > 0) {
            this.expire = expire;
        }
    }

    private String keyPrefix = ShiroKeyConstants.REDIS_CACHE_KEY_PREFIX;

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public ValueOperations<String, V> getValueOperations() {
        return valueOperations;
    }

    public void setValueOperations(ValueOperations<String, V> valueOperations) {
        this.valueOperations = valueOperations;
    }

    @Override
    public V get(K key) throws CacheException {
        String strKey = getStringRedisKey(key);
        return (V) valueOperations.get(strKey);
    }

    /**
     * 将shiro的缓存保存到redis中
     */
    @Override
    public V put(K key, V value) throws CacheException {
        String strKey = getStringRedisKey(key);
        V obj = (V) valueOperations.get(strKey);

        if (this.expire > 0) {
            valueOperations.set(strKey, value, expire, TimeUnit.MINUTES);
        } else {
            valueOperations.set(strKey, value);
        }

        return obj;

    }

    @Override
    public V remove(K key) throws CacheException {
        String strKey = getStringRedisKey(key);
        V obj = (V) valueOperations.get(strKey);
        valueOperations.getOperations().delete(strKey);
        return obj;
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() throws CacheException {
        valueOperations.getOperations().delete(valueOperations.getOperations().keys(keyPrefix + "*"));
    }

    /**
     * 缓存的个数
     */
    @Override
    public int size() {
        return valueOperations.getOperations().keys(keyPrefix + "*").size();
    }

    /**
     * 获取所有的key
     */
    @Override
    public Set<K> keys() {
        Set<String> keys = valueOperations.getOperations().keys(keyPrefix + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        } else {
            return (Set<K>) keys;
        }
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection<V> values() {
        Set<String> keys = valueOperations.getOperations().keys(keyPrefix + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        } else {
            List<V> values = new ArrayList<V>(keys.size());
            for (String key : keys) {
                values.add(valueOperations.get(key));
            }
            return Collections.unmodifiableList(values);
        }
    }

    private String getStringRedisKey(K key) {
        String redisKey;
        if (key instanceof PrincipalCollection) {
            redisKey = getRedisKeyFromPrincipalIdField((PrincipalCollection) key);
        } else {
            redisKey = key.toString();
        }
        return redisKey;
    }

    private String getRedisKeyFromPrincipalIdField(PrincipalCollection key) {
        Object principalObject = key.getPrimaryPrincipal();
        if (principalObject instanceof String) {
            return principalObject.toString();
        } else if (principalObject instanceof SysUser) {
            return this.keyPrefix + ShiroUtils.getUniqueKey("");
        } else if (principalObject instanceof AuthenticationToken) {
            AuthenticationToken token = (AuthenticationToken) principalObject;
            return this.keyPrefix + token.getPrincipal();
        }

        return key.toString();
    }
}