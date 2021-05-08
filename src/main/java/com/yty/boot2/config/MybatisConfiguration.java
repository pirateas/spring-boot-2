package com.yty.boot2.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yangtianyu
 */
@Configuration
@MapperScan("com.yty.boot2.**.dao")
@EnableTransactionManagement(proxyTargetClass = true)
public class MybatisConfiguration {
}
