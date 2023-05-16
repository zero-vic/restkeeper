package com.restkeeper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 登录类型枚举类
 * @author: guod
 * @date: 2022-06-27 10:09
 */
@AllArgsConstructor
public enum GrantTypeEnum {

    OPERATOR("operator", "operatorStrategy"),
    SHOP("shop", "shopStrategy"),
    STOREMANAGER("store", "storeManagerStrategy"),
    STAFF("staff", "staffStrategy");


    @Getter
    private final String type;
    @Getter
    private final String value;

    public static String getValueByType(String type) {
        for (GrantTypeEnum grantTypeEnum : values()) {
            if (grantTypeEnum.getType().equals(type)) {
                return grantTypeEnum.getValue();
            }
        }
        return null;
    }
}
