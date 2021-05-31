package com.noname.producer.broker;

import com.noname.api.Message;
import com.noname.api.MessageType;
import com.noname.producer.constant.BrokerMessageConst;
import com.noname.producer.constant.BrokerMessageStatus;
import com.noname.producer.entity.BrokerMessage;
import com.noname.producer.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author ：liwuming
 * @date ：Created in 2021/5/25 14:47
 * @description ：
 * @modified By：
 * @version:
 */

@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {

    @Resource
    private RabbitTemplateContainer rabbitTemplateContainer;
    @Resource
    private MessageStoreService messageStoreService;


    /**
     * 发送一个快速消息，只要快就行，不用保证稳定性
     *
     * @param message
     */
    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    /**
     * 发送消息的核心方法
     *
     * @param message
     */
    private void sendKernel(Message message) {
        AsyncBaseQueue.submit(() -> {
            //字符串格式化，即messageId#当前时间戳，%s也就是占位符
            CorrelationData correlationData = new CorrelationData(String.format("%s#%s", message.getMessageId(), System.currentTimeMillis()));
            String topic = message.getTopic();
            String routingKey = message.getRouteKey();

            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getRabbitTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq,messageId:{}", message.getMessageId());
        });
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {
        message.setMessageType(MessageType.RELIANT);

        //1.先要将消息入库，并标记为发送中
        BrokerMessage brokerMessage = new BrokerMessage();
        brokerMessage.setMessageId(message.getMessageId());
        brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
        Date now = new Date();
        //记录下一次重试时间需要定义
        brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
        brokerMessage.setCreateTime(now);
        brokerMessage.setMessage(message);
        messageStoreService.insert(brokerMessage);

        //执行发送消息逻辑
        sendKernel(message);
    }

    @Override
    public void sendMessages() {

    }
}
