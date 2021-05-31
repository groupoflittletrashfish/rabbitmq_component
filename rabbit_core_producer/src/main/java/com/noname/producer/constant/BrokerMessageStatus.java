package com.noname.producer.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BrokerMessageStatus {

    SENDING("0"),
    SEND_OK("1"),
    SEND_FAIL("2");

    private String code;
}
