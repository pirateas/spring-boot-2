package com.yty.boot2.mq.support;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yangtianyu
 */
public abstract class BaseMessageListener implements RocketMQPushConsumerLifecycleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMessageListener.class);

    private static final int DEFAULT_CONSUME_THREAD = 20;

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
    }
}
