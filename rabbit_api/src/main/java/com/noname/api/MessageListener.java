package com.noname.api;

/**
 * 消费者监听申明
 */
public interface MessageListener {

    void onMessage(Message message);
}
