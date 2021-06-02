package com.yty.boot2.mq.consumer;

import com.yty.boot2.controller.MQMessageRequest;
import com.yty.boot2.mq.support.BaseMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yangtianyu
 */
//@Component
//@RocketMQMessageListener(topic = MessageConstants.Topic.TEST, selectorExpression = MessageConstants.Tag.TEST, consumerGroup = "${rocketmq.consumer.group}-" + MessageConstants.Tag.TEST)
public class TagMessageListener extends BaseMessageListener implements RocketMQListener<MQMessageRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagMessageListener.class);

    @Override
    public void onMessage(MQMessageRequest message) {
        LOGGER.info("MessageExtConsumer received message, message: {}", message);
    }
}
