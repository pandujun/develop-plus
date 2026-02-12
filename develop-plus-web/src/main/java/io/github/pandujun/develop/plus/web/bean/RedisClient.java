package io.github.pandujun.develop.plus.web.bean;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
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
public class RedisClient {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    // ===== 基础操作 =====
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeoutSeconds) {
        stringRedisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    public Long delete(Collection<String> keys) {
        return stringRedisTemplate.delete(keys);
    }

    public Boolean expire(String key, long timeoutSeconds) {
        return stringRedisTemplate.expire(key, timeoutSeconds, TimeUnit.SECONDS);
    }

    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return stringRedisTemplate.expire(key, timeout, unit);
    }

    public boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    public Long increment(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    // ===== 集合操作 =====
    public Long sAdd(String key, String... values) {
        return stringRedisTemplate.opsForSet().add(key, values);
    }

    public Long sAddMore(String key, Collection<String> values) {
        return stringRedisTemplate.opsForSet().add(key, values.toArray(new String[0]));
    }

    public Long sSize(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    public boolean sIsMember(String key, String value) {
        Boolean flag = stringRedisTemplate.opsForSet().isMember(key, value);
        return Objects.nonNull(flag) && flag;
    }

    public Long sRemove(String key, Object... values) {
        return stringRedisTemplate.opsForSet().remove(key, values);
    }

    public Set<String> sMembers(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    public void hAdd(String key, String hashKey, String value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void hAdd(String key, String hashKey, String value, long timeoutSeconds) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
        stringRedisTemplate.expire(key, timeoutSeconds, TimeUnit.SECONDS);
    }

    public void hAdd(String key, String hashKey, String value, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
        stringRedisTemplate.expire(key, timeout, unit);
    }

    public void hAddAll(String key, Map<String, String> map) {
        stringRedisTemplate.opsForHash().putAll(key, map);
    }

    public String hGet(String key, String hashKey) {
        return (String) stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    public Map<String, String> hGetAll(String key) {
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
        return map.entrySet().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> entry.getValue().toString()));
    }

    public Long hDelete(String key, Object... hashKeys) {
        return stringRedisTemplate.opsForHash().delete(key, hashKeys);
    }

    public Set<String> hKeys(String key) {
        Set<Object> keys = stringRedisTemplate.opsForHash().keys(key);
        return keys.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    public Long hSize(String key) {
        return stringRedisTemplate.opsForHash().size(key);
    }

    public Long hIncr(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, 1);
    }

    public Long hIncr(String key, String hashKey, long delta) {
        return stringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public Long lPush(String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    public String rPop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }
}
