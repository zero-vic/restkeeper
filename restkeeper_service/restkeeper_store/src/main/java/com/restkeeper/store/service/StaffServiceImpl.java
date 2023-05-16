package com.restkeeper.store.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.constant.MqConst;
import com.restkeeper.constant.SmsObject;
import com.restkeeper.constant.SystemCode;
import com.restkeeper.mq.service.RabbitService;
import com.restkeeper.store.mapper.StaffMapper;
import com.restkeeper.store.entity.Staff;
import com.restkeeper.utils.JWTUtil;
import com.restkeeper.utils.MD5CryptUtil;
import com.restkeeper.utils.Result;
import com.restkeeper.utils.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service(version = "1.0.0",protocol = "dubbo")
@RefreshScope
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements IStaffService {


    @Autowired
    @Qualifier("rabbitService")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private RabbitService rabbitService;
    @Value("${sms.operator.signName}")
    private String signName;

    @Value("${sms.operator.templateCode}")
    private String templateCode;

    @Value("${gateway.secret}")
    private String secret;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addStaff(Staff staff) {

        String password = staff.getPassword();
        if (StringUtils.isEmpty(password)){
            password = RandomStringUtils.randomNumeric(8);
        }
        staff.setPassword(Md5Crypt.md5Crypt(password.getBytes()));

        try {
            this.save(staff);

            //发送短信
            this.sendMessage(staff.getPhone(),staff.getShopId(),staff.getIdNumber());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Result login(String shopId, String loginName, String loginPass) {
        Result result = new Result();
        QueryWrapper<Staff> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Staff::getShopId,shopId).eq(Staff::getStaffName,loginName);
//        Staff staff = this.getOne(queryWrapper);
        Staff staff = this.getBaseMapper().login(shopId, loginName);
        if(staff == null){
            result.setStatus(ResultCode.error);
            result.setDesc("员工信息不存在");
            return result;
        }
        String salts = MD5CryptUtil.getSalts(staff.getPassword());
        if(!Md5Crypt.md5Crypt(loginPass.getBytes(),salts).equals(staff.getPassword())){
            result.setStatus(ResultCode.error);
            result.setDesc("密码不正确");
            return result;
        }
        //生成令牌
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("shopId", shopId);
        tokenMap.put("storeId", staff.getStoreId());
        tokenMap.put("loginUserId", staff.getStaffId());
        tokenMap.put("loginUserName", loginName);
        tokenMap.put("userType", SystemCode.USER_TYPE_STAFF);

        String tokenInfo = "";
        try {
            tokenInfo = JWTUtil.createJWTByObj(tokenMap,secret);
        } catch (IOException e) {
            log.error("加密失败{}", e.getMessage());
            result.setStatus(ResultCode.error);
            result.setDesc("加密失败");
            return result;
        }

        result.setStatus(ResultCode.success);
        result.setDesc("ok");
        result.setData(staff);
        result.setToken(tokenInfo);
        return result;

    }

    /**
     *  发送短信
     */

    private void sendMessage(String phone, String shopId, String pwd) {
        SmsObject smsObject = new SmsObject();
        smsObject.setPhoneNumber(phone);
        smsObject.setSignName(signName);
        smsObject.setTemplateCode(templateCode);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shopId", shopId);
        jsonObject.put("password", pwd);
        smsObject.setTemplateJsonParam(jsonObject.toJSONString());
        rabbitService.sendMessage(MqConst.ACCOUNT_SMS_EXCHANGE,MqConst.ACCOUNT_SMS_ROUTING_KEY, JSON.toJSONString(smsObject));
//        rabbitTemplate.convertAndSend(RabbitMQConfig.ACCOUNT_QUEUE, JSON.toJSONString(smsObject));
    }
}
