package com.yty.boot2.mq.consumer;

import com.yty.boot2.mq.support.BaseMessageListener;
import com.yty.boot2.mq.support.MessageConstants;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author yangtianyu
 */
@Component
@RocketMQMessageListener(topic = MessageConstants.Topic.TEST, consumerGroup = "${rocketmq.consumer.group}")
public class TestMessageListener extends BaseMessageListener implements RocketMQListener<MessageExt> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestMessageListener.class);

    @Override
    public void onMessage(MessageExt message) {
        LOGGER.info("received message: {}", message);

        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        LOGGER.info("received body: {}", body);
    }
}
