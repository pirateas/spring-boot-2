package com.yty.boot2.mq.consumer;

import com.yty.boot2.mq.support.BaseMessageListener;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * @author yangtianyu
 */
@Component
//@RocketMQMessageListener(topic = MessageConstants.Topic.TEST_1, consumerGroup = "${rocketmq.consumer.group}-1")
public class Test1MessageListener extends BaseMessageListener implements RocketMQListener<MessageExt> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Test1MessageListener.class);

    @Override
    public void onMessage(MessageExt message) {
        LOGGER.info("received message: {}", message);

        String body = new String(message.getBody(), Charset.forName("UTF-8"));
        LOGGER.info("received body: {}", body);
    }
}
