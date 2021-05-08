package com.yty.boot2.service;

import com.yty.boot2.common.cache.RedisManager;
import com.yty.boot2.config.PropertyConfig;
import com.yty.boot2.dao.TestMapper;
import com.yty.boot2.domain.entity.TestEntity;
import com.yty.boot2.mq.MessageProducer;
import com.yty.boot2.mq.support.MessageBean;
import com.yty.boot2.mq.support.MessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author yangtianyu
 */
@Service
@Transactional
public class TestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

    @Resource
    private TestMapper testMapper;

    @Resource
    private RedisManager<TestEntity> redisManager;

    @Resource
    private PropertyConfig propertyConfig;

    @Resource
    private MessageProducer messageProducer;

    @Transactional
    public void create(TestEntity entity) {
        entity.setCreateTime(new Date());
        testMapper.create(entity);
        MessageBean message = MessageBean.builder(MessageConstants.Topic.TEST).withBody(entity).build();
        messageProducer.sendInTransaction(message);
    }

    public TestEntity findById(Long id) {
        return redisManager.get("entity:" + id.toString(), () -> testMapper.findById(id), 60, TimeUnit.SECONDS);
    }
}
