package com.restkeeper.constant;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/26
 * Description: Mq的常量类
 * Version:V1.0
 */
public class MqConst {
    /**
     * 账号短信下发队列
     */
    public static final String ACCOUNT_SMS_QUEUE = "account_sms_queue";
    /**
     *账号短信下发队列routingkey
     */
    public static  final String ACCOUNT_SMS_ROUTING_KEY = "account_sms_routing_key";
    /**
     * 账号短信下发队列 交换机
     */
    public static final String ACCOUNT_SMS_EXCHANGE = "account_sms_exchange";
}
