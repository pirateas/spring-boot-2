package com.yty.boot2.common.filter.cache;

/**
 * @author yangtianyu created on 2021/5/11
 */
public interface CachedStreamEntity {
    /**
     * 获取cache的stream
     *
     * @return
     */
    CachedStream getCachedStream();

    /**
     * 刷出
     */
    void flushStream();
}
