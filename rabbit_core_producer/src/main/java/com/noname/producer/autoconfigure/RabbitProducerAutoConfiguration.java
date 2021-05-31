package com.noname.producer.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 该类是自动装配的类，即在springboot启动后自动加载该类。需要配置配置文件，
 * resourcesx下方的META-INF下的spring.factories文件
 *
 * @author ：liwuming
 * @date ：Created in 2021/5/25 14:11
 * @description ：
 * @modified By：
 * @version:
 */


@Configuration
@ComponentScan({"com.noname.producer.*"})
public class RabbitProducerAutoConfiguration {
}
