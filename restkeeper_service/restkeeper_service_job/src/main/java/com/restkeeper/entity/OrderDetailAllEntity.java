package com.restkeeper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "v_order_detail_all")
public class OrderDetailAllEntity{
    @TableId(value="detail_id",type = IdType.ASSIGN_ID)
    private String detailId;

    @TableField("order_id")
    private String orderId;

    private String orderNumber;

    private Integer detailStatus;

    private Integer dishType;

    private String dishName;

    private Integer dishPrice;

    private Integer dishNumber;

    private String dishCategoryName;

    @TableField(value = "last_update_time")
    protected LocalDateTime lastUpdateTime;


    @TableField(value = "shop_id")
    protected String shopId;

    protected String storeId;
}
