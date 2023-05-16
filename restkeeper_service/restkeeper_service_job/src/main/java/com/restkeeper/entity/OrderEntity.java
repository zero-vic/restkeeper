package com.restkeeper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "t_order")
public class OrderEntity implements Serializable {
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

    private String orderRemark;

    private LocalDateTime createTime;

    private String operatorName;

    private String buyId;

    private int orderSource; //订单来源 0 收银端 1 H5

    @TableField(value = "last_update_time")
    protected LocalDateTime lastUpdateTime;

    @TableField(value = "is_deleted")
    protected Integer isDeleted;

    @TableField(value = "shop_id")
    protected String shopId;

    protected String storeId;

    @TableField(exist = false)
    private List<OrderDetailEntity> orderDetails;

    /**
     * 交易笔数
     */
    @TableField(exist = false)
    private int payCount;
}
