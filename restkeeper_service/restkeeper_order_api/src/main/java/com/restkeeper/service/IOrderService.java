package com.restkeeper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.dto.*;
import com.restkeeper.entity.OrderEntity;

import java.time.LocalDate;
import java.util.List;

public interface IOrderService extends IService<OrderEntity> {

    /**
     * 下单
     * @param orderEntity
     * @return
     */
    String addOrder(OrderEntity orderEntity);


    /**
     * 退菜
     * @param detailDTO
     * @return
     */
    public boolean returnDish(DetailDTO detailDTO);


    /**
     * 结账
     * @param orderEntity
     * @return
     */
    boolean pay(OrderEntity orderEntity);

    /**
     * 挂账
     * @param orderEntity
     * @param creditDTO
     * @return
     */
    boolean pay(OrderEntity orderEntity, CreditDTO creditDTO);

    /**
     * 换桌
     * @param orderId
     * @param targetTableId
     * @return
     */
    boolean changeTable(String orderId,String targetTableId);

    /**
     * 根据日期获取销售汇总数据
     * @param start
     * @param end
     * @return
     */
    CurrentAmountCollectDTO getCurrentCollect(LocalDate start, LocalDate end);

    /**
     * 统计24小时销售数据
     * @param start
     * @param end
     * @param type 统计类型 1:销售额;2:销售数量
     * @return
     */
    List<CurrentHourCollectDTO> getCurrentHourCollect(LocalDate start, LocalDate end, Integer type);

    /**
     * 获取收款方式构成汇总数据
     * @param start
     * @param end
     * @return
     */
    List<PayTypeCollectDTO> getPayTypeCollect(LocalDate start, LocalDate end);

    /**
     * 优惠金额汇总方法 赠菜、免单、抹零
     * @param start
     * @param end
     * @return
     */
    PrivilegeDTO getPrivilegeCollect(LocalDate start,LocalDate end);

    /**
     * 小程序下单
     * @param orderEntity
     * @return
     */
    String addMicroOrder(OrderEntity orderEntity);
}
