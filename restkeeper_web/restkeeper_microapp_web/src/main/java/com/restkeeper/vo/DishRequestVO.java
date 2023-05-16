package com.restkeeper.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DishRequestVO {
    @ApiModelProperty(value = "桌台id")
    private String tableId;
    @ApiModelProperty(value = "门店id")
    private String storeId;
    @ApiModelProperty(value = "商户id")
    private String shopId;
    @ApiModelProperty(value = "菜品或者套餐id")
    private  String dishId;
    @ApiModelProperty(value = "名称")
    private  String dishName;
    @ApiModelProperty(value = "类型 1 菜品 2 套餐")
    private  int type;
    @ApiModelProperty(value = "单价")
    private Integer price;
    @ApiModelProperty(value = "份数")
    private int dishNumber; //1 加菜，-1减菜
    @ApiModelProperty(value = "口味")
    private String flavorRemark;
}
