package com.restkeeper.micapp;

import com.restkeeper.constant.SystemCode;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/11
 * Description:
 * Version:V1.0
 */
@Component
public class RabbitMqConfig {

    /**
     * 定义H5点餐交换机
     * @return
     */
    @Bean
    public TopicExchange vendoutExchange(){
        return new TopicExchange(SystemCode.MICROSOFT_EXCHANGE_NAME,true,false);
    }
}
