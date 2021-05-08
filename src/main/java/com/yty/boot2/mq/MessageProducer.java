package com.yty.boot2.mq;

import com.yty.boot2.mq.support.MessageBean;
import com.yty.boot2.mq.support.MessageConstants;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author yangtianyu
 */
@Component
public class MessageProducer {

    @Value("${config.mq.topicPrefix}")
    private String topicPrefix;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    private MessageQueueSelector messageQueueSelector = new SelectMessageQueueByHash();

    private MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();

    @PostConstruct
    private void initNamespace() {
        rocketMQTemplate.getProducer().setNamespace(topicPrefix);
    }

    private DefaultMQProducer getMqProducer() {
        return rocketMQTemplate.getProducer();
    }

    public SendResult sendWithSelector(MessageBean message, MessageQueueSelector selector, Object arg) {
        if (Objects.isNull(selector)) {
            selector = messageQueueSelector;
        }
        org.apache.rocketmq.common.message.Message rocketMessage = new Message();
        rocketMessage.setTopic(message.getTopic());
        if (StringUtils.isNotBlank(message.getTags())) {
            rocketMessage.setTags(message.getTags());
        }
        if (MapUtils.isNotEmpty(message.getProperties())) {
            message.getProperties().forEach(rocketMessage::putUserProperty);
        }
        rocketMessage.setBody(convertBody(message.getBody()));
        try {
            // 同步的方式
            SendResult sendResult = getMqProducer().send(rocketMessage, selector, arg);
            LOGGER.info("send result={}", sendResult);
            return sendResult;
        } catch (Exception e) {
            LOGGER.error("send by selector error:", e);
            throw new MessagingException(e.getMessage(), e);
        }
    }

    public SendResult send(MessageBean message) {
        int delayLevel = 0;
        if (Objects.nonNull(message.getDelayLevel())) {
            delayLevel = message.getDelayLevel().getLevel();
        }
        SendResult sendResult = rocketMQTemplate.syncSend(convertDestination(message), buildMessage(message),
                rocketMQTemplate.getProducer().getSendMsgTimeout(), delayLevel);
        LOGGER.info("SendResult={}", sendResult);
        return sendResult;
    }

    public SendResult sendOrderly(MessageBean message, String hashKey) {
        SendResult sendResult = rocketMQTemplate.syncSendOrderly(convertDestination(message), buildMessage(message), hashKey);
        LOGGER.info("sendOrderly SendResult={}", sendResult);
        return sendResult;
    }

    public void sendInTransaction(MessageBean message) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            applicationContext.publishEvent(message);
        }
        send(message);
//        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(convertDestination(message), buildMessage(message), null);
//        LOGGER.info("TransactionSendResult={}", sendResult);
//        return sendResult;
    }

    public void asyncSend(MessageBean message) {
        rocketMQTemplate.asyncSend(convertDestination(message), buildMessage(message), new SendCallback() {
            @Override
            public void onSuccess(SendResult result) {
                LOGGER.info("asyncSend onSuccess SendResult={}", result);
            }

            @Override
            public void onException(Throwable throwable) {
                LOGGER.info("asyncSend onException Throwable={}", throwable);
            }
        });
    }

    private String convertDestination(MessageBean message) {
        String destination = MessageConstants.Topic.TEST;
        if (StringUtils.isNotBlank(message.getTags())) {
            destination +=  ":" + message.getTags();
        }
        return destination;
    }

    private org.springframework.messaging.Message buildMessage(MessageBean message) {
        MessageBuilder<Object> messageBuilder = MessageBuilder.withPayload(message.getBody());
        if (MapUtils.isNotEmpty(message.getProperties())) {
            message.getProperties().forEach(messageBuilder::setHeader);
        }
        return messageBuilder.build();
    }

    private byte[] convertBody(Object body) {
        org.springframework.messaging.Message<?> message = messageConverter.toMessage(body, null, null);
        return (byte[]) message.getPayload();
    }
}
