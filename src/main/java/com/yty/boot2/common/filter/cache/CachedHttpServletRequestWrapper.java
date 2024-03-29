package com.yty.boot2.common.filter.cache;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @author yangtianyu created on 2021/5/11
 */
public class CachedHttpServletRequestWrapper extends HttpServletRequestWrapper implements CachedStreamEntity {

    private CachedInputStream cachedInputStream;

    public CachedHttpServletRequestWrapper(HttpServletRequest httpServletRequest, int initCacheSize, int maxCacheSize)
            throws IOException {
        super(httpServletRequest);
        this.cachedInputStream = new CachedInputStream(httpServletRequest, initCacheSize, maxCacheSize);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return cachedInputStream;
    }

    @Override
    public CachedStream getCachedStream() {
        return cachedInputStream;
    }

    @Override
    public void flushStream() {
        //do nothing
    }
}
