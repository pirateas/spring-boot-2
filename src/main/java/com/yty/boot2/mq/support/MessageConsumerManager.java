package com.yty.boot2.mq.support;

import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author yangtianyu created on 2021/5/10
 */
@Component
public class MessageConsumerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumerManager.class);

    @Resource
    private ApplicationContext applicationContext;

    public void suspend() {
        Map<String, DefaultRocketMQListenerContainer> consumerMap = applicationContext.getBeansOfType(DefaultRocketMQListenerContainer.class);
        consumerMap.forEach((name, container) -> {
            if (container.isRunning()) {
                try {
                    container.getConsumer().suspend();
                    LOGGER.info("suspend mq consumer: topic={},consumerGroup={}", container.getTopic(), container.getConsumerGroup());
                } catch (Exception e) {
                    LOGGER.error("suspend mq consumer fail: topic={},consumerGroup={}", container.getTopic(), container.getConsumerGroup(), e);
                }
            }
        });
    }

    public void resume() {
        Map<String, DefaultRocketMQListenerContainer> consumerMap = applicationContext.getBeansOfType(DefaultRocketMQListenerContainer.class);
        consumerMap.forEach((name, container) -> {
            if (container.isRunning()) {
                try {
                    container.getConsumer().resume();
                    LOGGER.info("resume mq consumer: topic={},consumerGroup={}", container.getTopic(), container.getConsumerGroup());
                } catch (Exception e) {
                    LOGGER.error("resume mq consumer fail: topic={},consumerGroup={}", container.getTopic(), container.getConsumerGroup(), e);
                }
            }
        });
    }
}
