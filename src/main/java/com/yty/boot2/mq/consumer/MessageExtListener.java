package com.yty.boot2.mq.consumer;

import com.yty.boot2.mq.support.BaseMessageListener;
import com.yty.boot2.mq.support.MessageConstants;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * @author yangtianyu
 */
@Component
@RocketMQMessageListener(topic = MessageConstants.Topic.TEST, consumerGroup = "${rocketmq.consumer.group}")
public class MessageExtListener extends BaseMessageListener implements RocketMQListener<MessageExt> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageExtListener.class);

    @Override
    public void onMessage(MessageExt message) {
        LOGGER.info("MessageExtConsumer received message, message: {}", message);

        String body = new String(message.getBody(), Charset.forName("UTF-8"));
        LOGGER.info("MessageExtConsumer received message, body: {}", body);
    }
}
