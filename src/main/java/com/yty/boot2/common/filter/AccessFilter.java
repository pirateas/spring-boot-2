package com.yty.boot2.common.filter;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.yty.boot2.common.filter.cache.CachedHttpServletRequestWrapper;
import com.yty.boot2.common.filter.cache.CachedHttpServletResponseWrapper;
import com.yty.boot2.common.trace.TraceIdGenerator;
import com.yty.boot2.common.util.RequestIpUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yangtianyu created on 2021/5/11
 */
@Component
public class AccessFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);

    private static final int MAX_CACHE_LEN = 2 * 1024 * 1024;
    private static final int INIT_CACHE_LEN = 512 * 1024;

    private List<String> excludeUris;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String traceID = request.getHeader(TraceIdGenerator.TRACE_ID);
        if (StringUtils.isBlank(traceID)) {
            traceID = TraceIdGenerator.generate();
        }
        MDC.put(TraceIdGenerator.TRACE_ID, traceID);
        String requestURI = request.getRequestURI();
        if (match(excludeUris, requestURI)) {
            try {
                filterChain.doFilter(request, response);
            } finally {
                MDC.remove(TraceIdGenerator.TRACE_ID);
            }
            return;
        }

        Stopwatch started = Stopwatch.createStarted();
        CachedHttpServletRequestWrapper httpServletRequestWrapper = new CachedHttpServletRequestWrapper(request,
                INIT_CACHE_LEN, MAX_CACHE_LEN);
        CachedHttpServletResponseWrapper httpServletResponseWrapper = new CachedHttpServletResponseWrapper(response,
                INIT_CACHE_LEN, MAX_CACHE_LEN);
        try {
            filterChain.doFilter(httpServletRequestWrapper, httpServletResponseWrapper);
        } finally {
            LOGGER.info(request.getHeader("Content-Type"));
            LOGGER.info("time={}ms, ip={}, uri={}, params={}, request={}, response={}", started.elapsed(TimeUnit.MILLISECONDS),
                    RequestIpUtils.getRealIP(request), requestURI, getParams(request), getRequest(httpServletRequestWrapper), getResponse(httpServletResponseWrapper));
            MDC.remove(TraceIdGenerator.TRACE_ID);
        }
    }

    private boolean match(List<String> patterns, String uri) {
        if (CollectionUtils.isEmpty(patterns)) {
            return false;
        }

        for (String url : patterns) {
            url = StringUtils.trimToEmpty(url);
            if (url.endsWith("*")) {
                String sub = url.substring(0, url.length() - 1);
                if (uri.startsWith(sub)) {
                    return true;
                }
            } else {
                if (uri.equals(url)) {
                    return true;
                }
            }
        }

        return false;
    }

    private String getParams(HttpServletRequest request) {
        // 注意这里返回的map不能更改，所以需要复制一份
        Map<String, String[]> params = request.getParameterMap();
        params = Maps.newHashMap(params);
        StringBuilder stringBuilder = new StringBuilder();
        boolean appendSeparator = false;
        if (MapUtils.isNotEmpty(params)) {
            for (Map.Entry<String, String[]> entry : params.entrySet()) {
                for (String v : entry.getValue()) {
                    if (appendSeparator) {
                        stringBuilder.append("&");
                    }
                    appendSeparator = true;
                    stringBuilder.append(entry.getKey()).append("=").append(StringUtils.trimToEmpty(v));
                }
            }
        }
        String paramString = stringBuilder.toString();
        if (StringUtils.equals(request.getContentType(), MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            try {
                paramString = URLDecoder.decode(paramString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return paramString;
    }

    private String getRequest(CachedHttpServletRequestWrapper httpServletRequestWrapper) {
        byte[] requestData = httpServletRequestWrapper.getCachedStream().getCached();
        return requestData == null ? StringUtils.EMPTY : new String(requestData).replaceAll("\n|\r", "");
    }

    private String getResponse(CachedHttpServletResponseWrapper httpServletResponseWrapper) {
        byte[] responseData = httpServletResponseWrapper.getCachedStream().getCached();
        return responseData == null ? StringUtils.EMPTY : new String(responseData).replaceAll("\n|\r", "");
    }

    public void setExcludeUris(List<String> excludeUris) {
        this.excludeUris = excludeUris;
    }
}
