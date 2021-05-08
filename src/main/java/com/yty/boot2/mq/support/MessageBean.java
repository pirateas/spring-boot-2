package com.yty.boot2.mq.support;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author tianyu.yang created on 2021/2/19
 */
public class MessageBean implements Serializable {

    private static final String NULL_BODY = " ";

    private String topic;
    private String tags;
    private MessageDelayLevel delayLevel;
    private Object body;
    private Map<String, String> properties = new HashMap<>();

    private MessageBean() {
    }

    public String getTopic() {
        return topic;
    }

    MessageBean setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getTags() {
        return tags;
    }

    MessageBean setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public MessageDelayLevel getDelayLevel() {
        return delayLevel;
    }

    MessageBean setDelayLevel(MessageDelayLevel delayLevel) {
        this.delayLevel = delayLevel;
        return this;
    }

    public Object getBody() {
        return body;
    }

    MessageBean setBody(Object body) {
        this.body = body;
        return this;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    MessageBean setProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public static MessageBeanBuilder builder(String topic) {
        Preconditions.checkArgument(StringUtils.isNotBlank(topic), "topic can't be empty.");
        return new MessageBeanBuilder(topic);
    }

    public static final class MessageBeanBuilder {
        private String topic;
        private String tags;
        private MessageDelayLevel delayLevel;
        private Object body;
        private Map<String, String> properties = new HashMap<>();

        private MessageBeanBuilder(String topic) {
            this.topic = topic;
        }

        public MessageBeanBuilder withTags(String tags) {
            this.tags = tags;
            return this;
        }

        public MessageBeanBuilder withDelayLevel(MessageDelayLevel delayLevel) {
            this.delayLevel = delayLevel;
            return this;
        }

        public MessageBeanBuilder withBody(Object body) {
            this.body = body;
            return this;
        }

        public MessageBeanBuilder addPropertie(String key, String value) {
            this.properties.put(key, value);
            return this;
        }

        public MessageBean build() {
            MessageBean mQMessage = new MessageBean();
            mQMessage.setTopic(topic);
            mQMessage.setTags(tags);
            mQMessage.setDelayLevel(delayLevel);
            mQMessage.setBody(Objects.nonNull(body) ? body : NULL_BODY);
            mQMessage.setProperties(properties);
            return mQMessage;
        }
    }
}
