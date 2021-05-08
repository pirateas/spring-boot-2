package com.yty.boot2.config;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangtianyu
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Resource
    private List<HandlerInterceptorAdapter> interceptorAdapters;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (CollectionUtils.isNotEmpty(interceptorAdapters)) {
            for (HandlerInterceptorAdapter interceptorAdapter : interceptorAdapters) {
                registry.addInterceptor(interceptorAdapter);
            }
        }
    }
}
