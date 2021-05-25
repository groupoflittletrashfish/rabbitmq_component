package com.noname.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：liwuming
 * @date ：Created in 2021/5/24 16:30
 * @description ：
 * @modified By：
 * @version:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    /**
     * 消息唯一ID
     */
    private String messageId;

    /**
     * 消息主题,即4种交换机中的一种，通过路由绑定方式关联。其他交换器暂时不例举
     */
    private String topic;

    /**
     * 路由规则
     */
    private String routeKey = "";

    /**
     * 记录属性的Map
     */
    private Map<String, Object> attribute = new HashMap<>();

    /**
     * 延迟消息的参数配置
     */
    private int delayMills;

    /**
     * 消息类型,默认为confirm消息类型
     */
    private String messageType = MessageType.CONFIRM;

}
