package com.yty.boot2.controller;

import com.yty.boot2.common.SlidingWindowCounter;
import com.yty.boot2.common.cache.RedisManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by yangtianyu.
 */
@RestController
@RequestMapping(path = "/redis")
public class RedisController {

    private static final String KEY = "test:key:aaa";

    @Resource
    private RedisManager redisManager;

    @Resource
    private SlidingWindowCounter slidingWindowCounter;

    @PostMapping(path = "/set")
    public Object set(@RequestBody MQMessageRequest message) {
        return redisManager.save(KEY, message);
    }

    @PostMapping(path = "/get")
    public Object get() {
        return ApiResponse.ofSuccess(redisManager.get(KEY));
    }

    @PostMapping(path = "/delete")
    public void delete() {
        redisManager.delete(KEY);
    }

    @PostMapping(path = "/test")
    public boolean test() {
        return slidingWindowCounter.isActionAllowed("test:allowed", 10, 5);
    }
}