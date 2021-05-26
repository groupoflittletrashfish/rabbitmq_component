package com.noname.common.serializer.impl;

import com.noname.api.Message;
import com.noname.common.serializer.Serializer;
import com.noname.common.serializer.SerializerFactory;

public class JacksonSerializerFactory implements SerializerFactory {

    public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
		/*
			创建一个实例化的序列化对象，该对象中实现了序列化和反序列化方法。序列化的作用是
			发送的消息可以使用自己的对象，而非amqp提供的对象。那么对应的消费者解析的时候需要反序列化得到
			对应的我们自定义的对象。
		 */
        return JacksonSerializer.createParametricType(Message.class);
    }

}
