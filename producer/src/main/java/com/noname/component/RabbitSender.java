package com.noname.component;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

/**
 * 自定义一个组件，用于发送消息
 *
 * @author ：liwuming
 * @date ：Created in 2021/5/21 15:22
 * @description ：
 * @modified By：
 * @version:
 */
@Component
public class RabbitSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /*
        此处是直接new出来以后实现了，也可以直接在类上继承接口去实现
        确认消息的回调监听接口，用于确认消息是否被broker收到
        correlationData：一般用于数据的唯一标识
        ack：是否落盘成功
        cause：失败异常
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        System.out.println("消息是否被确认" + ack);
        System.out.println("唯一标识码：" + correlationData.getId());
    };


    /**
     * 对外发送消息的方法
     *
     * @param message    具体的消息内容
     * @param properties 消息的一些附加属性
     */
    public void send(Object message, Map<String, Object> properties) {
        //附加属性封装成MQ需要的对象
        MessageHeaders mhs = new MessageHeaders(properties);
        //消息实体封装成MQ需要的对象
        Message<Object> msg = MessageBuilder.createMessage(message, mhs);
        //设置回调函数
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //在发送函数中使用，在发送完消息以后将会执行该代码
        MessagePostProcessor mpp = message1 -> {
            System.out.println("发送完消息后执行该代码...");
            return message1;
        };
        //在发送函数中使用，表示数据的唯一标识
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //发送消息
        rabbitTemplate.convertAndSend("exchange-1", "springboot.rabbit", msg, mpp, correlationData);

    }
}
