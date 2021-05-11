package com.yty.boot2.controller;

import com.yty.boot2.mq.MessageProducer;
import com.yty.boot2.mq.support.MessageBean;
import com.yty.boot2.mq.support.MessageConstants;
import com.yty.boot2.mq.support.MessageConsumerManager;
import com.yty.boot2.mq.support.MessageDelayLevel;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yangtianyu.
 */
@RestController
@RequestMapping(path = "/mq")
public class MQController {

    @Resource
    private MessageProducer messageProducer;

    @Resource
    private MessageConsumerManager messageConsumerManager;

    @PostMapping(path = "/suspend")
    public void suspend() {
        messageConsumerManager.suspend();
    }

    @PostMapping(path = "/resume")
    public void resume() {
        messageConsumerManager.resume();
    }

    @PostMapping(path = "/send")
    public SendResult send(@RequestBody MQMessageRequest request) {
        MessageBean message = MessageBean.builder(MessageConstants.Topic.TEST)
                .withTags(request.getTag())
                .withBody(request)
                .addPropertie("testKey", "testValue")
                .build();
        return messageProducer.send(message);
    }

    @PostMapping(path = "/asyncSend")
    public void asyncSend(@RequestBody MQMessageRequest request) {
        MessageBean message = MessageBean.builder(MessageConstants.Topic.TEST).withBody(request).build();
        messageProducer.asyncSend(message);
    }

    @PostMapping(path = "/sendByHash")
    public SendResult sendByHash(@RequestBody MQMessageRequest request) {
        MessageBean message = MessageBean.builder(MessageConstants.Topic.TEST).withTags(request.getTag()).withBody(request).build();
        return messageProducer.sendOrderly(message, request.getHashKey());
    }

    @PostMapping(path = "/sendWithSelector")
    public SendResult sendWithSelector(@RequestBody MQMessageRequest request) {
        MessageBean message = MessageBean.builder(MessageConstants.Topic.TEST).withTags(request.getTag()).withBody(request).build();
        return messageProducer.sendWithSelector(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                return mqs.get(0);
            }
        }, null);
    }

    @PostMapping(path = "/sendByTransaction")
    public void sendByTransaction(@RequestBody MQMessageRequest request) {
        MessageBean message = MessageBean.builder(MessageConstants.Topic.TEST).withTags(request.getTag()).withBody(request).build();
        messageProducer.sendInTransaction(message);
    }

    @PostMapping(path = "/sendDelay")
    public SendResult sendDelay(@RequestBody MQMessageRequest request) {
        MessageBean message = MessageBean.builder(MessageConstants.Topic.TEST).withTags(request.getTag()).withBody(request).withDelayLevel(MessageDelayLevel.LEVEL_3).build();
        return messageProducer.send(message);
    }
}