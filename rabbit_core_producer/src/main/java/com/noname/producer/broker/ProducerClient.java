package com.noname.producer.broker;

import com.google.common.base.Preconditions;
import com.noname.api.Message;
import com.noname.api.MessageProducer;
import com.noname.api.MessageType;
import com.noname.api.SendCallback;
import com.noname.exception.MessageRunTimeException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 发送消息的实现
 *
 * @author ：liwuming
 * @date ：Created in 2021/5/25 14:21
 * @description ：
 * @modified By：
 * @version:
 */

@Component
public class ProducerClient implements MessageProducer {

    @Resource
    private RabbitBroker rabbitBroker;

    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        switch (message.getMessageType()) {
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                break;
            case MessageType.RELIANT:
                break;
            default:
                break;
        }
    }

    @Override
    public void send(Message message) throws MessageRunTimeException {

    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {

    }
}
