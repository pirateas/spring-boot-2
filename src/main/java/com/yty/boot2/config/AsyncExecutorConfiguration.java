package com.yty.boot2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yangtianyu
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncExecutorConfiguration implements AsyncConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncExecutorConfiguration.class);

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setBeanName("asyncExecutor");
        //线程池大小
        executor.setCorePoolSize(1);
        //线程池最大线程数
        executor.setMaxPoolSize(10);
        executor.setKeepAliveSeconds(60);
        //最大等待任务数
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("async-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (e, method, params) -> LOGGER.error("异步方法执行失败: method={},message={}", method.getName(), e.getMessage(), e);
    }
}
