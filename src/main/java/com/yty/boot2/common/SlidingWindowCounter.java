package com.yty.boot2.common;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tianyu.yang created on 2021/4/20
 * @version $Id$
 */
@Component
public class SlidingWindowCounter {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public boolean isActionAllowed(String key, int periodSecond, long maxCount) {
        long currentTimeMillis = System.currentTimeMillis();
        redisTemplate.multi();
        BoundZSetOperations<String, String> boundZSetOperations = redisTemplate.boundZSetOps(key);
        boundZSetOperations.removeRangeByScore(0, currentTimeMillis - periodSecond * 1000);
        boundZSetOperations.add(currentTimeMillis + RandomStringUtils.randomAlphabetic(6), currentTimeMillis);
        boundZSetOperations.size();
        List<Object> results = redisTemplate.exec();
        Long size = (Long) Iterables.getLast(results);
        System.out.println(size);
        boundZSetOperations.expire(periodSecond, TimeUnit.SECONDS);
        return size <= maxCount;
    }
}
