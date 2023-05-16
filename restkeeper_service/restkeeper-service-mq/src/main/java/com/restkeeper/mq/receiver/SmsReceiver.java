package com.restkeeper.mq.receiver;

import com.alibaba.alicloud.sms.ISmsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.rabbitmq.client.Channel;
import com.restkeeper.constant.MqConst;
import com.restkeeper.constant.SmsObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
@Slf4j
public class SmsReceiver {

    @Autowired
    private ISmsService smsService;

    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConst.ACCOUNT_SMS_QUEUE,durable = "true"),
    exchange = @Exchange(value = MqConst.ACCOUNT_SMS_EXCHANGE,type = ExchangeTypes.DIRECT,durable = "true"),
    key = {MqConst.ACCOUNT_SMS_ROUTING_KEY}))
    public void sendSms(String obj, Message message, Channel channel){
        if(obj!=null){
            log.info("发送短信监听类接收到消息："+obj);
            SmsObject smsObject = JSON.parseObject(obj, SmsObject.class);
            SendSmsResponse sendSmsResponse = this.sendSms(smsObject.getPhoneNumber(),smsObject.getSignName(),smsObject.getTemplateCode(),smsObject.getTemplateJsonParam());
            log.info(JSON.toJSONString(sendSmsResponse));

        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    //发送短信
    private SendSmsResponse sendSms(String phoneNumber, String signName, String templateCode, String templateJsonParam) {

        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        // 可选:模板中的变量替换JSON串,如模板内容为"【企业级分布式应用服务】,您的验证码为${code}"时,此处的值为
//        request.setTemplateParam(templateJsonParam);
        JSONObject json = new JSONObject();
        json.put("code","124549");
        request.setTemplateParam(json.toJSONString());
        SendSmsResponse sendSmsResponse ;
        try {
            sendSmsResponse = smsService.sendSmsRequest(request);
        }
        catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
            sendSmsResponse = new SendSmsResponse();
        }
        return sendSmsResponse;

    }
}
