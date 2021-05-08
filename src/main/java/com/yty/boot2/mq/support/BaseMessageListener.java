package com.yty.boot2.mq.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

/**
 * @author yangtianyu
 */
public abstract class BaseMessageListener implements RocketMQPushConsumerLifecycleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMessageListener.class);

    private static final int DEFAULT_CONSUME_THREAD = 20;

    @Value("${config.mq.topicPrefix}")
    private String topicPrefix;

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        // 设置consumer的消费策略
        // CONSUME_FROM_LAST_OFFSET 默认策略，从该队列最尾开始消费，即跳过历史消息
        // CONSUME_FROM_FIRST_OFFSET 从队列最开始开始消费，即历史消息（还储存在broker的）全部消费一遍
        // CONSUME_FROM_TIMESTAMP 从某个时间点开始消费，和setConsumeTimestamp()配合使用，默认是半个小时以前
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        // 设置每次消费消息的数量，默认每次消费1条
//        consumer.setConsumeMessageBatchMaxSize(5);

        consumer.setConsumeThreadMax(DEFAULT_CONSUME_THREAD);
        consumer.setConsumeThreadMin(DEFAULT_CONSUME_THREAD);

        // subscribe topic with namespace
        if (StringUtils.isNotBlank(topicPrefix)) {
            consumer.setNamespace(topicPrefix);
            try {
                RocketMQMessageListener rocketMQMessageListener = this.getClass().getAnnotation(RocketMQMessageListener.class);
                if (Objects.nonNull(rocketMQMessageListener)) {
                    String topic = rocketMQMessageListener.topic();
                    switch (rocketMQMessageListener.selectorType()) {
                        case TAG:
                            consumer.subscribe(topic, rocketMQMessageListener.selectorExpression());
                            break;
                        case SQL92:
                            consumer.subscribe(topic, MessageSelector.bySql(rocketMQMessageListener.selectorExpression()));
                            break;
                        default:
                            throw new IllegalArgumentException("Property 'selectorType' was wrong.");
                    }
                    consumer.unsubscribe(topic);
                }
            } catch (MQClientException e) {
                LOGGER.error("subscribe topic with namespace fail", e);
            }
        }
    }
}
