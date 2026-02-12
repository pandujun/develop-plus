package io.github.pandujun.develop.plus.web.bean;

import io.github.pandujun.develop.plus.web.configuration.RedisConfiguration;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RedisNoPrefixClient {

    private final RedisConfiguration.NoPrefixStringRedisTemplate noPrefixStringRedisTemplate;

    public RedisNoPrefixClient(RedisConfiguration.NoPrefixStringRedisTemplate noPrefixStringRedisTemplate) {
        this.noPrefixStringRedisTemplate = noPrefixStringRedisTemplate;
    }

    // ===== 基础操作 =====
    public void set(String key, String value) {
        noPrefixStringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeoutSeconds) {
        noPrefixStringRedisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        noPrefixStringRedisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String get(String key) {
        return (String) noPrefixStringRedisTemplate.opsForValue().get(key);
    }

    public Boolean delete(String key) {
        return noPrefixStringRedisTemplate.delete(key);
    }

    public Long delete(Collection<String> keys) {
        return noPrefixStringRedisTemplate.delete(keys);
    }

    public Boolean expire(String key, long timeoutSeconds) {
        return noPrefixStringRedisTemplate.expire(key, timeoutSeconds, TimeUnit.SECONDS);
    }

    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return noPrefixStringRedisTemplate.expire(key, timeout, unit);
    }

    public boolean exists(String key) {
        return noPrefixStringRedisTemplate.hasKey(key);
    }

    public Long getExpire(String key) {
        return noPrefixStringRedisTemplate.getExpire(key);
    }

    public Long increment(String key) {
        return noPrefixStringRedisTemplate.opsForValue().increment(key);
    }

    public Long increment(String key, long delta) {
        return noPrefixStringRedisTemplate.opsForValue().increment(key, delta);
    }

    // ===== 集合操作 =====
    public Long sAdd(String key, String... values) {
        return noPrefixStringRedisTemplate.opsForSet().add(key, values);
    }

    public Long sAddMore(String key, Collection<String> values) {
        return noPrefixStringRedisTemplate.opsForSet().add(key, values.toArray(new String[0]));
    }

    public Long sSize(String key) {
        return noPrefixStringRedisTemplate.opsForSet().size(key);
    }

    public boolean sIsMember(String key, String value) {
        Boolean flag = noPrefixStringRedisTemplate.opsForSet().isMember(key, value);
        return Objects.nonNull(flag) && flag;
    }

    public Long sRemove(String key, Object... values) {
        return noPrefixStringRedisTemplate.opsForSet().remove(key, values);
    }

    public Set<String> sMembers(String key) {
        Set<Object> members = noPrefixStringRedisTemplate.opsForSet().members(key);
        if (members == null) {
            return Collections.emptySet();
        }
        return members.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    public void hAdd(String key, String hashKey, String value) {
        noPrefixStringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void hAdd(String key, String hashKey, String value, long timeoutSeconds) {
        noPrefixStringRedisTemplate.opsForHash().put(key, hashKey, value);
        noPrefixStringRedisTemplate.expire(key, timeoutSeconds, TimeUnit.SECONDS);
    }

    public void hAdd(String key, String hashKey, String value, long timeout, TimeUnit unit) {
        noPrefixStringRedisTemplate.opsForHash().put(key, hashKey, value);
        noPrefixStringRedisTemplate.expire(key, timeout, unit);
    }

    public void hAddAll(String key, Map<String, String> map) {
        noPrefixStringRedisTemplate.opsForHash().putAll(key, map);
    }

    public String hGet(String key, String hashKey) {
        return (String) noPrefixStringRedisTemplate.opsForHash().get(key, hashKey);
    }

    public Map<String, String> hGetAll(String key) {
        Map<Object, Object> map = noPrefixStringRedisTemplate.opsForHash().entries(key);
        return map.entrySet().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> entry.getValue().toString()));
    }

    public Long hDelete(String key, Object... hashKeys) {
        return noPrefixStringRedisTemplate.opsForHash().delete(key, hashKeys);
    }

    public Set<String> hKeys(String key) {
        Set<Object> keys = noPrefixStringRedisTemplate.opsForHash().keys(key);
        return keys.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    public Long hSize(String key) {
        return noPrefixStringRedisTemplate.opsForHash().size(key);
    }

    public Long hIncr(String key, String hashKey) {
        return noPrefixStringRedisTemplate.opsForHash().increment(key, hashKey, 1);
    }

    public Long hIncr(String key, String hashKey, long delta) {
        return noPrefixStringRedisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public Long lPush(String key, String value) {
        return noPrefixStringRedisTemplate.opsForList().leftPush(key, value);
    }

    public String rPop(String key) {
        return (String) noPrefixStringRedisTemplate.opsForList().rightPop(key);
    }

}

