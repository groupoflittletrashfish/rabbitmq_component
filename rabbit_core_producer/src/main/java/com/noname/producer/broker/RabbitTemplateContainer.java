package com.noname.producer.broker;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.noname.api.Message;
import com.noname.api.MessageType;
import com.noname.common.covert.GenericMessageConverter;
import com.noname.common.covert.RabbitMessageConverter;
import com.noname.common.serializer.SerializerFactory;
import com.noname.common.serializer.impl.JacksonSerializerFactory;
import com.noname.exception.MessageRunTimeException;
import com.noname.producer.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 由于RabbitTemplate是单例模式的，所以如果出现大量请求发送不同的topic等类似的情况，
 * 就需要不断的更改内部的参数，这样对性能的损耗是比较大的，所以需要做池化处理，来让每个请求使用不同的实例对象
 *
 * @author ：liwuming
 * @date ：Created in 2021/5/25 16:40
 * @description ：
 * @modified By：
 * @version:
 */

@Component
@Slf4j
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {
    private Map<String /* TOPIC*/, RabbitTemplate> rabbitMap = Maps.newConcurrentMap();
    private Splitter splitter = Splitter.on("#");

    @Resource
    private ConnectionFactory connectionFactory;
    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;
    @Resource
    private MessageStoreService messageStoreService;


    public RabbitTemplate getRabbitTemplate(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message);
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
        if (rabbitTemplate != null) {
            return rabbitTemplate;
        }
        log.info("#RabbitTemplateContainer.getRabbitTemplate# topic:{} is not exist", topic);

        RabbitTemplate newRabbitTemplate = new RabbitTemplate(connectionFactory);
        newRabbitTemplate.setExchange(topic);
        newRabbitTemplate.setRoutingKey(message.getRouteKey());
        newRabbitTemplate.setRetryTemplate(new RetryTemplate());

        //※设置序列化，非常重要，即发送的消息对象是自定义的，在消费端的时候也要使用对应的反序列化
        GenericMessageConverter gmc = new GenericMessageConverter(serializerFactory.create());
        RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
        newRabbitTemplate.setMessageConverter(rmc);

        String messageType = message.getMessageType();
        if (!messageType.equals(MessageType.RAPID)) {
            newRabbitTemplate.setConfirmCallback(this);
        }
        rabbitMap.put(topic, newRabbitTemplate);
        return newRabbitTemplate;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        //注意correlationData存储的内容是发送消息时候写入的，之前传入的值是message.getId()#时间戳
        List<String> strings = splitter.splitToList(correlationData.getId());
        String messageId = strings.get(0);
        Long sendTime = Long.parseLong(strings.get(1));

        if (ack) {
            //当Broker返回ACK成功时，要更新记录的状态
            this.messageStoreService.success(messageId);
            log.info("send message is ok,confirm messageId:{},sendTime:{}", messageId, sendTime);
        } else {
            log.error("send message is Fail,confirm messageId:{},sendTime:{}", messageId, sendTime);
        }
    }
}
