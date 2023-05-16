package com.restkeeper.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DishVO {

    @ApiModelProperty(value = "菜品或者套餐id")
    private  String dishId;
    @ApiModelProperty(value = "名称")
    private  String dishName;
    @ApiModelProperty(value = "类型 1 菜品 2 套餐")
    private  Integer type;
    @ApiModelProperty(value = "单价")
    private  Integer price;
    @ApiModelProperty(value = "份数")
    private Integer dishNumber;
    @ApiModelProperty(value = "口味")
    private List<String> flavorList;
    @ApiModelProperty(value = "剩余数目")
    private Integer remainder;
    @ApiModelProperty(value = "商品图片url")
    private String imageUrl;
    @ApiModelProperty(value = "商品描述")
    private String desc;
}
