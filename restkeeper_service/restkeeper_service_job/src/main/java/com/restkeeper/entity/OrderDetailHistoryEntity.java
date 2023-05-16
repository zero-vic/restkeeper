package com.restkeeper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_his_order_detail")
public class OrderDetailHistoryEntity{
    @TableId(value="detail_id",type = IdType.ASSIGN_ID)
    private String detailId;

    @TableField("order_id")
    private String orderId;

    private String orderNumber;

    @TableField(value = "shop_id")
    protected String shopId;

    protected String storeId;

    private String tableId;

    private Integer detailStatus;

    private String flavorRemark;

    private String presentRemark;

    private String returnRemark;

    private String addRemark;

    private String dishId;

    private Integer dishType;

    private String dishName;

    private Integer dishPrice;

    private Integer dishNumber;

    private Integer dishAmount;

    private String dishRemark;

    private String dishCategoryName;

    @TableField(value = "last_update_time")
    protected LocalDateTime lastUpdateTime;

    @TableField(value = "is_deleted")
    protected Integer isDeleted;


}