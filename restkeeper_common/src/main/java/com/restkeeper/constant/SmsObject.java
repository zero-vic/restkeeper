package com.restkeeper.constant;

import lombok.Data;

import java.io.Serializable;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/26
 * Description:
 * Version:V1.0
 */
@Data
public class SmsObject implements Serializable {
    //网络传输对象必须序列化
    private static final long serialVersionUID = -6986749569115643762L;

    private String phoneNumber;

    private String signName;

    private String templateCode;

    private String templateJsonParam;
}
