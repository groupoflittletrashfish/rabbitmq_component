package com.noname.producer.service;

import com.noname.producer.constant.BrokerMessageStatus;
import com.noname.producer.entity.BrokerMessage;
import com.noname.producer.mapper.BrokerMessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 由于是示例项目，此处不写接口
 *
 * @author ：liwuming
 * @date ：Created in 2021/5/27 14:33
 * @description ：
 * @modified By：
 * @version:
 */

@Service
public class MessageStoreService {

    @Resource
    private BrokerMessageMapper brokerMessageMapper;

    public int insert(BrokerMessage brokerMessage) {
        return brokerMessageMapper.insert(brokerMessage);
    }

    public void success(String messageId) {
        this.brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_OK.getCode(), new Date());
    }

    public void failture(String messageId) {
        this.brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_FAIL.getCode(), new Date());
    }
}
