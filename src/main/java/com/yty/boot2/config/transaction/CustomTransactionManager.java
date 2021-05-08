package com.yty.boot2.config.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;

/**
 * @author tianyu.yang created on 2021/4/28
 * @version $Id$
 */
public class CustomTransactionManager extends DataSourceTransactionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomTransactionManager.class);

    public CustomTransactionManager() {
    }

    public CustomTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected Object doSuspend(Object transaction) {
        Object resource = super.doSuspend(transaction);
        LOGGER.info("doSuspend");
        return resource;
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);
        LOGGER.info("doBegin");
    }

    @Override
    protected void doResume(Object transaction, Object suspendedResources) {
        super.doResume(transaction, suspendedResources);
        LOGGER.info("doResume");
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        super.doRollback(status);
        LOGGER.info("doRollback");
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        LOGGER.info("doCleanupAfterCompletion");
    }

    @Override
    protected void prepareForCommit(DefaultTransactionStatus status) {
        LOGGER.info("prepareForCommit");
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        super.doCommit(status);
        LOGGER.info("doCommit");
    }
}
