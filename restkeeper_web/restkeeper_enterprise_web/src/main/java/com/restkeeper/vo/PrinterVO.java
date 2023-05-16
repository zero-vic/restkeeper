package com.restkeeper.vo;

import lombok.Data;

import java.util.List;

@Data
public class PrinterVO{
    private String printerId;
    private String printerName;
    private List<String> dishIdList;
    private int printNumber;
    private String machineCode;
    private int areaType;
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
