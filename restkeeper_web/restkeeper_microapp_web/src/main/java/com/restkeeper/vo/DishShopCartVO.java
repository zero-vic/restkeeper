package com.restkeeper.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DishShopCartVO implements Serializable {
    private String id;
    private String name;
    private int number;
    private int price;
    private int type;
    private String flavorRemark;
}

