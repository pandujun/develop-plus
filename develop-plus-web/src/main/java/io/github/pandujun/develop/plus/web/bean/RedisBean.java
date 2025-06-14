package io.github.pandujun.develop.plus.web.bean;

import io.github.pandujun.develop.plus.core.constant.NumberConstant;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis工具类
 * 使用方法：直接调取方法使用
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/10/31 14:36
 */
public class RedisBean {

    private final StringRedisTemplate redisTemplate;

    public RedisBean(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据key获取缓存数据
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 缓存一个key
     */
    public void cache(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存一个key
     */
    public void cache(String key, String value, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 删除一个key
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     */
    public Long deleteMore(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 设置一个key的过期时间
     */
    public Boolean expire(String key, Long time, TimeUnit timeUnit) {
        return redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 获取一个key的剩余时间
     */
    public Long getExpireTime(String key) {
        return redisTemplate.getExpire(key);
    }

    //========================== HASH ==============================

    /**
     * 缓存一个hash key
     */
    public void cacheHashValue(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 缓存一个hash key，并设置过期时间
     */
    public void cacheHashValue(String key, String hashKey, String value, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        this.expire(key, time, timeUnit);
    }

    /**
     *  缓存一个hash key 自增
     */
    public Long cacheHashValueIncr(String key, String hashKey, long value) {
        return redisTemplate.opsForHash().increment(key, hashKey, value);
    }

    /**
     * 根据key和hashKey获取具体值
     */
    public String getHashValue(String key, String hashKey) {
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 根据key获取所有的hashKey
     */
    public Set<String> getHashKey(String key) {
        Set<Object> hashKeys = redisTemplate.opsForHash().keys(key);
        if (!CollectionUtils.isEmpty(hashKeys)) {
            return hashKeys.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * 根据key和hashKey，删除缓存
     */
    public Long deleteHashValue(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    //========================== SET ==============================

    /**
     * 缓存一个set
     */
    public Long cacheSet(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 缓存一个set，并设置过期时间
     */
    public Boolean cacheSet(String key, String value, Long time, TimeUnit timeUnit) {
        Long addFlag = redisTemplate.opsForSet().add(key, value);
        if (addFlag != null && addFlag > NumberConstant.ZERO_NUM) {
            return this.expire(key, time, timeUnit);
        }
        return false;
    }

    /**
     * 获取set对应的数据
     */
    public Set<String> getSetValue(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 删除set缓存当中的数据
     */
    public Long deleteSetValue(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }
}
