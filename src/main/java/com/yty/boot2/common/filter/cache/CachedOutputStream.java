package com.yty.boot2.common.filter.cache;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 可以缓存写入流中的数据的代理流类，用于日志记录
 *
 * @author yangtianyu created on 2021/5/11
 */
public class CachedOutputStream extends ServletOutputStream implements CachedStream {
    private ByteArrayOutputStream cachedOutputStream;
    private ServletOutputStream srcOutputStream;
    private int maxCacheSize;

    public CachedOutputStream(ServletOutputStream srcOutputStream, int initCacheSize, int maxCacheSize) {
        CachedStreamUtils.checkCacheSizeParam(initCacheSize, maxCacheSize);
        this.srcOutputStream = srcOutputStream;
        this.cachedOutputStream = new ByteArrayOutputStream(initCacheSize);
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    public byte[] getCached() {
        return cachedOutputStream.toByteArray();
    }

    @Override
    public void write(int b) throws IOException {
        this.srcOutputStream.write(b);
        if (this.cachedOutputStream.size() < maxCacheSize) {
            CachedStreamUtils.safeWrite(cachedOutputStream, b);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.cachedOutputStream.close();
    }

    @Override
    public boolean isReady() {
        return this.srcOutputStream.isReady();
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        this.srcOutputStream.setWriteListener(writeListener);
    }
}
