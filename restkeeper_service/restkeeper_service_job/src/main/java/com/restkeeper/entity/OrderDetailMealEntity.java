package com.restkeeper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_order_detail_meal")
public class OrderDetailMealEntity {

    @ApiModelProperty(value = "主键")
    @TableId(value="detail_id",type = IdType.ASSIGN_ID)
    private String detailId;

    @ApiModelProperty(value = "订单id")
    @TableField("order_id")
    private String orderId;

    @ApiModelProperty(value = "订单流水号")
    private String orderNumber;

    @ApiModelProperty(value = "所属桌台")
    private String tableId;

    @ApiModelProperty(value = "状态 1 赠菜 2 退菜 ")
    private Integer detailStatus;

    @ApiModelProperty(value = "菜品备注 ")
    private String dishRemark;

    @ApiModelProperty(value = "菜品id")
    private String dishId;

    @ApiModelProperty(value = "类型 1 菜品 2 套餐")
    private Integer dishType;

    @ApiModelProperty(value = "名称")
    private String dishName;

    @ApiModelProperty(value = "单价")
    private Integer dishPrice;

    @ApiModelProperty(value = "份数")
    private Integer dishNumber;

    @ApiModelProperty(value = "口味备注")
    private String flavorRemark;

    @ApiModelProperty(value = "赠菜备注")
    private String presentRemark;

    @ApiModelProperty(value = "退菜备注")
    private String returnRemark;

    @ApiModelProperty(value = "金额")
    private Integer dishAmount;

    @ApiModelProperty(value = "菜品分类名称")
    private String dishCategoryName;

    @TableField(value = "last_update_time")
    protected LocalDateTime lastUpdateTime;

    @TableField(value = "is_deleted")
    protected Integer isDeleted;

    @TableField(value = "shop_id")
    protected String shopId;

    protected String storeId;
}
