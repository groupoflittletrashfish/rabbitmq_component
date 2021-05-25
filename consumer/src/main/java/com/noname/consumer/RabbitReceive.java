package com.noname.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


/**
 * @author ：liwuming
 * @date ：Created in 2021/5/21 17:32
 * @description ：
 * @modified By：
 * @version:
 */

@Component
public class RabbitReceive {


    /**
     * QueueBinding：交换机与队列绑定
     * durable：是否需要持久化
     * ignoreDeclarationExceptions：是否忽略异常
     * key：路由规则
     *
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-1", durable = "true"),
            exchange = @Exchange(name = "exchange-1", durable = "true", type = "topic",
                    ignoreDeclarationExceptions = "true"),
            key = "springboot.*"
    ))
    public void onMessage(Message message, Channel channel) throws Exception {
        //业务处理
        System.out.println(message.getPayload());

        //处理成功后需要手动确认（配置了手动确认模式）。需要一个参数deliverTag，该参数是发送消息的时候rabbitMQ自动生成的
        Long deliverTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //一定要手动确认一下
        channel.basicAck(deliverTag, false);
    }

}
