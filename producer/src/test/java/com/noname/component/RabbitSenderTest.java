package com.noname.component;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitSenderTest {


    @Resource
    private RabbitSender rabbitSender;

    @SneakyThrows
    @Test
    public void send() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("attr1", "12345");
        properties.put("attr2", "abcde");

        rabbitSender.send("hello rabbitMQ", properties);

        Thread.sleep(10000);
    }
}