package com.yty.boot2.common.filter.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author yangtianyu created on 2021/5/11
 */
public class CachedStreamUtils {
    private final static Logger logger = LoggerFactory.getLogger(CachedStreamUtils.class);

    /**
     * 检查缓存的参数是否有效，无效跑出IllegalArgumentException
     * @param initCacheSize
     * @param maxCacheSize
     */
    public static void checkCacheSizeParam(int initCacheSize, int maxCacheSize) {
        if(initCacheSize <= 0){
            throw new IllegalArgumentException("init cache size is invalid!");
        }

        if(maxCacheSize <= 0){
            throw new IllegalArgumentException("max cache size is valid!");
        }
        if(initCacheSize > maxCacheSize){
            throw new IllegalArgumentException("init cache is large than max cache size!!");
        }
    }

    /**
     * 安全写入流中，如果遇到异常会吞掉，不抛异常
     */
    public static void safeWrite(OutputStream out, int val){
        try {
            out.write(val);
        } catch (IOException e) {
            logger.warn("write stream exception", e);
             //ignore
        }
    }
}
