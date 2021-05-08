package com.yty.boot2.config;

import com.yty.boot2.config.transaction.CustomTransactionManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author tianyu.yang created on 2021/4/28
 * @version $Id$
 */
@Configuration
public class TransactionManagerConfiguration {

    @Resource
    private DataSource dataSource;

    @Bean(name="transactionManager")
    public CustomTransactionManager transactionManager(ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        CustomTransactionManager transactionManager = new CustomTransactionManager(dataSource);
        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
        return transactionManager;
    }
}
