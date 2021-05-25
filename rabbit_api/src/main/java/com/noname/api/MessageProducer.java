package com.noname.api;

import com.noname.exception.MessageRunTimeException;

import java.util.List;

/**
 * 发送端，接口申明
 */
public interface MessageProducer {

    /**
     * 消息发送，带有回调函数
     * @param message
     * @param sendCallback
     * @throws MessageRunTimeException
     */
    void send(Message message, SendCallback sendCallback) throws MessageRunTimeException;

    void send(Message message) throws MessageRunTimeException;

    void send(List<Message> messages) throws MessageRunTimeException;
}
