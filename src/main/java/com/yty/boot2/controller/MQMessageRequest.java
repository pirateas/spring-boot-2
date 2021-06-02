package com.yty.boot2.controller;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author tianyu.yang created on 2021/2/7
 * @version $Id$
 */
public class MQMessageRequest implements Serializable {

    private String topic;
    private String hashKey;
    private String message;
    private String tag;

    public String getTopic() {
        return topic;
    }

    public MQMessageRequest setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MQMessageRequest setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public MQMessageRequest setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getHashKey() {
        return hashKey;
    }

    public MQMessageRequest setHashKey(String hashKey) {
        this.hashKey = hashKey;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
