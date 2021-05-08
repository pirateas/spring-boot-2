package com.yty.boot2.mq.consumer;

import com.yty.boot2.controller.MQMessageRequest;
import com.yty.boot2.mq.support.BaseMessageListener;
import com.yty.boot2.mq.support.MessageConstants;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author yangtianyu
 */
@Component
@RocketMQMessageListener(topic = MessageConstants.Topic.TEST, selectorExpression = MessageConstants.Tag.TEST, consumerGroup = "${rocketmq.consumer.group}-" + MessageConstants.Tag.TEST)
public class MessageExtTagListener extends BaseMessageListener implements RocketMQListener<MQMessageRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageExtTagListener.class);

    @Override
    public void onMessage(MQMessageRequest message) {
        LOGGER.info("MessageExtConsumer received message, message: {}", message);
    }
}
