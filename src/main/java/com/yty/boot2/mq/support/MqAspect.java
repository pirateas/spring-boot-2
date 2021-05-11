package com.yty.boot2.mq.support;

import com.yty.boot2.common.trace.TraceIdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author yangtianyu created on 2021/5/10
 */
@Component
@Aspect
public class MqAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqAspect.class);

    @Around("execution(void org.apache.rocketmq.spring.core.RocketMQListener.onMessage(java.lang.Object))")
    public Object onMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object arg = args[0];
        String traceId = null;
        if (arg instanceof MessageExt) {
            traceId = ((MessageExt) arg).getUserProperty(TraceIdGenerator.TRACE_ID);
        }
        if (StringUtils.isBlank(traceId)) {
            traceId = TraceIdGenerator.generate();
        }
        MDC.put(TraceIdGenerator.TRACE_ID, traceId);
        Object proceed;
        try {
            proceed = joinPoint.proceed();
            return proceed;
        } catch (Exception e) {
            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            throw e;
        } finally {
            MDC.remove(TraceIdGenerator.TRACE_ID);
        }
    }

}
