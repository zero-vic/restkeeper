package com.restkeeper.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("t_printer_dish")
public class PrinterDish implements Serializable {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;
    private String dishId;
    private String printerId;
    protected String shopId;
    protected String storeId;
}
