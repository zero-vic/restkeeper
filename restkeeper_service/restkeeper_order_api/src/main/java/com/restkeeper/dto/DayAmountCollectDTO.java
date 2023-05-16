package com.restkeeper.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 每日销售数据汇总
 */
@Data
public class DayAmountCollectDTO{
    /**
     * 销售额数据
     */
    private Integer totalAmount;
    /**
     * 销售笔数
     */
    private Integer totalCount;
    /**
     * 日期
     */
    private LocalDate date;
}