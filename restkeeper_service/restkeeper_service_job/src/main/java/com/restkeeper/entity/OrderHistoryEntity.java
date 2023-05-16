package com.restkeeper.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "t_his_order")
public class OrderHistoryEntity{
    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id",type = IdType.ASSIGN_ID)
    private String orderId;

    private String orderNumber;

    private String tableId;

    private Integer payType;

    private Integer payStatus;

    private Integer payAmount;

    private Integer totalAmount;

    private Integer smallAmount;

    private Integer presentAmount;

    private Integer freeAmount;

    private Integer personNumbers;

    private String remark;

    private LocalDateTime createTime;

    private String operatorName;

    private String buyId;

    private int orderSource; //订单来源 0 收银端 1 H5

    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime lastUpdateTime;

    @TableField(value = "is_deleted")
    protected Integer isDeleted;

    @TableField(value = "shop_id")
    protected String shopId;

    protected String storeId;
}
