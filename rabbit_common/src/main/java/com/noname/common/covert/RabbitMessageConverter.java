package com.noname.common.covert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.messaging.MessageHeaders;

/**
 * 此处使用装饰者模式，需要继承同一个接口后再引入被装饰的类，在该类基础上进行额外的操作
 *
 * @author ：liwuming
 * @date ：Created in 2021/5/26 14:06
 * @description ：
 * @modified By：
 * @version:
 */
public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter delegate;
    private final String defaultExpire = String.valueOf(24 * 60 * 60 * 1000);

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.delegate = genericMessageConverter;
    }


    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        messageProperties.setExpiration(defaultExpire);
        return this.delegate.toMessage(o, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        com.noname.api.Message msg = (com.noname.api.Message) this.delegate.fromMessage(message);
        return msg;
    }
}
