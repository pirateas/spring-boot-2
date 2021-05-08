package com.yty.boot2.common.cache;

import com.google.common.base.Joiner;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author yangtianyu
 */
@Component
@SuppressWarnings("unchecked")
public class RedisManager<T> {

    private static final Joiner KEY_PREFIX_JOINER = Joiner.on(":");

    @Value("${config.redis.keyPrefix}")
    private String keyPrefix;

    @Resource
    private RedisTemplate redisTemplate;

    public T save(String key, T value) {
        redisTemplate.opsForValue().set(convertKey(key), CacheValue.toCachedValue(value));
        return value;
    }

    public T save(String key, T value, int aliveTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(convertKey(key), CacheValue.toCachedValue(value), aliveTime, timeUnit);
        return value;
    }

    public CacheValue<T> get(String key) {
        return CacheValue.of(redisTemplate.opsForValue().get(convertKey(key)));
    }

    public T get(String key, Supplier<T> supplier) {
        CacheValue<T> cacheValue = get(key);
        if (cacheValue.cached()) {
            return cacheValue.get();
        }
        T value = supplier.get();
        redisTemplate.opsForValue().set(convertKey(key), CacheValue.toCachedValue(value));
        return value;
    }

    public T get(String key, Supplier<T> supplier, int aliveTime, TimeUnit timeUnit) {
        CacheValue<T> cacheValue = get(key);
        if (cacheValue.cached()) {
            return cacheValue.get();
        }
        T value = supplier.get();
        redisTemplate.opsForValue().set(convertKey(key), CacheValue.toCachedValue(value), aliveTime, timeUnit);
        return value;
    }

    public void delete(String key) {
        redisTemplate.delete(convertKey(key));
    }

    public T saveHash(String key, String hashKey, T value) {
        redisTemplate.opsForHash().put(convertKey(key), hashKey, CacheValue.toCachedValue(value));
        return value;
    }

    public void saveHash(String key, Map map) {
        redisTemplate.opsForHash().putAll(convertKey(key), map);
    }

    public CacheValue<T> getHash(String key, String hashKey) {
        return CacheValue.of(redisTemplate.opsForHash().get(convertKey(key), hashKey));
    }

    public Set<String> getHashKeys(String key) {
        return redisTemplate.opsForHash().keys(convertKey(key));
    }

    public Map getHashEntries(String key) {
        Map map = redisTemplate.opsForHash().entries(convertKey(key));
        if (MapUtils.isEmpty(map)) {
            return null;
        }
        return map;
    }

    public void deleteHash(String key, String hashKey) {
        redisTemplate.opsForHash().delete(convertKey(key), hashKey);
    }

    public void deleteHash(String key, List<String> hashKeys) {
        if (hashKeys != null && !hashKeys.isEmpty()) {
            redisTemplate.opsForHash().delete(convertKey(key), hashKeys.toArray());
        }
    }

    public void addSet(String key, Object... values) {
        redisTemplate.opsForSet().add(convertKey(key), values);
    }

    public void removeSet(String key, Object... values) {
        redisTemplate.opsForSet().remove(convertKey(key), values);
    }

    public boolean isMember(String key, Object values) {
        return redisTemplate.opsForSet().isMember(convertKey(key), values);
    }

    public Set<Object> randomValues(String key, int count) {
        return redisTemplate.opsForSet().distinctRandomMembers(convertKey(key), count);
    }

    public Long sizeSet(String key) {
        return redisTemplate.opsForSet().size(convertKey(key));
    }

    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(convertKey(key), timeout, unit);
    }

    private String convertKey(String key) {
        if (StringUtils.isBlank(keyPrefix)) {
         return key;
        }
        return KEY_PREFIX_JOINER.join(keyPrefix, key);
    }
}
