package com.yty.boot2.common.cache;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.support.NullValue;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * @author yangtianyu
 */
public class CacheValue<T> implements Serializable {

    private T data;
    private boolean cached = false;

    private CacheValue(T data) {
        if (data != null) {
            cached = true;
        }
        if (!NullValue.INSTANCE.equals(data)) {
            this.data = data;
        }
    }

    public static CacheValue of(Object data) {
        return new CacheValue(data);
    }

    public boolean cached() {
        return this.cached;
    }

    public T get() {
        if (!cached || data instanceof NullValue) {
            return null;
        }
        return data;
    }

    /**
     * @param value 要缓存的对象
     * @return 转换后的缓存对象
     */
    public static Object toCachedValue(Object value) {
        if (Objects.isNull(value)) {
            return NullValue.INSTANCE;
        }
        if (value instanceof String && StringUtils.isEmpty((String) value)) {
            return NullValue.INSTANCE;
        }
        if (value instanceof Collection && CollectionUtils.isEmpty((Collection) value)) {
            return NullValue.INSTANCE;
        }
        return value;
    }
}
