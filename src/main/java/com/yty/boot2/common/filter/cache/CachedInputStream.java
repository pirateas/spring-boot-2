package com.yty.boot2.common.filter.cache;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 可以缓存从流中读取的数据的代理流类，用于日志记录
 *
 * @author yangtianyu created on 2021/5/11
 */
public class CachedInputStream extends ServletInputStream implements CachedStream {
    private ByteArrayOutputStream cachedOutputStream;
    private HttpServletRequest request;
    private int maxCacheSize;

    public CachedInputStream(HttpServletRequest request, int initCacheSize, int maxCacheSize) {
        CachedStreamUtils.checkCacheSizeParam(initCacheSize, maxCacheSize);
        this.request = request;
        this.cachedOutputStream = new ByteArrayOutputStream(initCacheSize);
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    public int read() throws IOException {
        int b = request.getInputStream().read();
        if (b != -1 && cachedOutputStream.size() < maxCacheSize) {
            CachedStreamUtils.safeWrite(cachedOutputStream, b);
        }
        return b;
    }

    @Override
    public byte[] getCached() {
        return cachedOutputStream.toByteArray();
    }

    @Override
    public void close() throws IOException {
        super.close();
        cachedOutputStream.close();
    }

    @Override
    public boolean isFinished() {
        try {
            return this.request.getInputStream().isFinished();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isReady() {
        try {
            return this.request.getInputStream().isReady();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        try {
            this.request.getInputStream().setReadListener(readListener);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
