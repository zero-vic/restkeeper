package com.restkeeper.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_printer")
public class Printer extends BaseStoreEntity implements Serializable {
    /**
     * 打印机id
     */
    @TableId(value = "printer_id",type = IdType.ASSIGN_ID)
    private String printerId;
    /**
     * 打印机名称
     */
    private String printerName;
    /**
     * 打印机厂家编号
     */
    private String machineCode;
    /**
     * 打印机硬件版本号
     */
    private String hardwareVersion;
    /**
     * 打印机软件版本号
     */
    private String softwareVersion;
    /**
     * 打印机状态
     */
    private int state;
    /**
     * 打印机区域属性，1:后厨打印机；2:收银区打印
     */
    private int areaType;
    /**
     * 打印份数
     */
    private int printerNumber;
    /**
     * 是否支持打印制作菜单打印
     */
    private boolean enableMadeMenu;
    /**
     * 是否支持转菜单打印
     */
    private boolean enableChangeMenu;
    /**
     * 是否支持转台单打印
     */
    private boolean enableChangeTable;
    /**
     * 是否支持退菜打印
     */
    private boolean enableReturnDish;
    /**
     * 是否支持预付单打印
     */
    private boolean enableBeforehand;
    /**
     * 是否支持结账单打印
     */
    private boolean enableBill;
    /**
     * 是否支持客单打印
     */
    private boolean enableCustomer;
}
