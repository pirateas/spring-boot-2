/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yty.boot2.mq.transaction;

import com.yty.boot2.mq.MessageProducer;
import com.yty.boot2.mq.support.MessageBean;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TestTransactionListener
 */
@Component
@RocketMQTransactionListener
public class TransactionMessageListener implements RocketMQLocalTransactionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionMessageListener.class);

    private ConcurrentHashMap<String, TransactionStatus> localTrans = new ConcurrentHashMap<>();

    @Resource
    private MessageProducer messageProducer;

    @TransactionalEventListener
    public void onApplicationEvent(MessageBean message) {
        messageProducer.send(message);
    }

    @Transactional
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
        if (transactionStatus.isCompleted()) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        String transactionId = getTransactionId(message);
        localTrans.put(transactionId, transactionStatus);
        LOGGER.info("executeLocalTransaction, msgTransactionId={}, TransactionState={}", transactionId, RocketMQLocalTransactionState.UNKNOWN);
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        RocketMQLocalTransactionState retState = RocketMQLocalTransactionState.COMMIT;
        LOGGER.info("checkLocalTransaction, TransactionState={}", retState);
        String transactionId = getTransactionId(message);
//        RocketMQLocalTransactionState retState;
//        CustomTransactionStatus transactionStatus = (CustomTransactionStatus) localTrans.get(transactionId);
//        if (Objects.isNull(transactionStatus)) {
//            retState = RocketMQLocalTransactionState.ROLLBACK;
//        } else {
//            if (transactionStatus.isCompleted()) {
//                if (transactionStatus.isRollback()) {
//                    retState = RocketMQLocalTransactionState.ROLLBACK;
//                } else {
//                    retState = RocketMQLocalTransactionState.COMMIT;
//                }
//                localTrans.remove(transactionId);
//            } else {
//                retState = RocketMQLocalTransactionState.UNKNOWN;
//            }
//        }
        LOGGER.info("checkLocalTransaction, msgTransactionId={}, TransactionState={}", transactionId, retState);
        return retState;
    }

    private String getTransactionId(Message message) {
        return (String) message.getHeaders().get(RocketMQUtil.toRocketHeaderKey(RocketMQHeaders.TRANSACTION_ID));
    }
}
