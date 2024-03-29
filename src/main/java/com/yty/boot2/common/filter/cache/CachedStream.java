package com.yty.boot2.common.filter.cache;

import java.io.Closeable;

/**
 * 可以在内存中缓存为字节数组的流，主要用于记录流读取或者写入的数据
 *
 * @author yangtianyu created on 2021/5/11
 */
public interface CachedStream extends Closeable {
    /**
     * 返回缓存的字节数据
     *
     * @return
     */
    byte[] getCached();
}
