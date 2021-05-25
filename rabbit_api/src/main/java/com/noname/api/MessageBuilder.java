package com.noname.api;

import com.noname.exception.MessageRunTimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 设计模式：建造者模式, 有点在于在创建对象的时候可以额外做一些数据验证
 *
 * @author ：liwuming
 * @date ：Created in 2021/5/24 17:25
 * @description ：
 * @modified By：
 * @version:
 */
public class MessageBuilder {

    private MessageBuilder() {
    }

    private String messageId;
    private String topic;
    private String routeKey = "";
    private Map<String, Object> attribute = new HashMap<>();
    private int delayMills;
    private String messageType = MessageType.CONFIRM;

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public MessageBuilder withMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public MessageBuilder withTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder withRoutingKey(String routeKey) {
        this.routeKey = routeKey;
        return this;
    }

    public MessageBuilder withAttributes(Map<String, Object> attribute) {
        this.attribute = attribute;
        return this;
    }

    public MessageBuilder withAttribute(String key, Object value) {
        this.attribute.put(key, value);
        return this;
    }

    public MessageBuilder withDelayMills(int delayMills) {
        this.delayMills = delayMills;
        return this;
    }

    public MessageBuilder withMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public Message build() {
        if (messageId == null) {
            messageId = UUID.randomUUID().toString();
        }
        if (topic == null) {
            throw new MessageRunTimeException("this topic is null");
        }
        return new Message(messageId, topic, routeKey, attribute, delayMills, messageType);
    }
}
