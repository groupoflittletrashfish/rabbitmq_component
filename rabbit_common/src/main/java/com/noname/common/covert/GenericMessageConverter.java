package com.noname.common.covert;

import com.google.common.base.Preconditions;
import com.noname.common.serializer.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * 序列化函数，在发送端使用自定义对象时候，消费端要使用对应的反序列化方式来解析出该自定义对象
 *
 * @author ：liwuming
 * @date ：Created in 2021/5/26 11:39
 * @description ：
 * @modified By：
 * @version:
 */
public class GenericMessageConverter implements MessageConverter {

    private Serializer serializer;

    public GenericMessageConverter(Serializer serializer) {
        Preconditions.checkNotNull(serializer);
        this.serializer = serializer;
    }


    /**
     * 将自定义的message对象转换为amqp的message
     *
     * @param o
     * @param messageProperties
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(this.serializer.serializeRaw(o), messageProperties);
    }

    /**
     * 将amqp封装的message对象转换为自定义的对象
     *
     * @param message
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return this.serializer.deserialize(message.getBody());
    }
}
