package com.yty.boot2;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Producer, using RocketMQTemplate sends a variety of messages
 * https://github.com/apache/rocketmq-spring
 *
 *  @author yangtianyu
 */
//@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String nameServer = "localhost:9876";
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");
        consumer.setNamespace("local");
        consumer.setVipChannelEnabled(false);
        consumer.setInstanceName(RocketMQUtil.getInstanceName(nameServer));
        consumer.setNamesrvAddr(nameServer);

        consumer.subscribe("test-topic", "*");
        consumer.subscribe("test-topic-1", "*");

        consumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt messageExt : msgs) {
                try {
                    LOGGER.info(messageExt.toString());
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
    }
}
