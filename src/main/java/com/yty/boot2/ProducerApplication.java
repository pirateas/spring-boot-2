package com.yty.boot2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Producer, using RocketMQTemplate sends a variety of messages
 * https://github.com/apache/rocketmq-spring
 *
 *  @author yangtianyu
 */
@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
