package com.restkeeper.mq.service;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/26
 * Description:
 * Version:V1.0
 */
@Component
public class RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     *  发送消息
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息
     */
    public boolean sendMessage(String exchange, String routingKey, Object message) {

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }

}
