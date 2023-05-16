package com.restkeeper.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShopCartVO implements Serializable {
    private List<DishShopCartVO> dishList;
    private int seatNumber;
    private String tableId;
}

