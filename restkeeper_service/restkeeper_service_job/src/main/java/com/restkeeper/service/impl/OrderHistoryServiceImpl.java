package com.restkeeper.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.restkeeper.entity.*;
import com.restkeeper.mapper.*;
import com.restkeeper.service.OrderHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderHistoryMapper orderHistoryMapper;
    @Autowired
    private OrderDetailHistoryMapper orderDetailHistoryMapper;

    @Autowired
    private OrderDetailMealMapper orderDetailMealMapper;
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void exportToHistory() {
        LocalDate nowDate = LocalDate.now();

        //查询出小于今天的订单记录
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().lt(OrderEntity::getLastUpdateTime,nowDate);
        List<OrderEntity> orderEntityList = orderMapper.selectList(wrapper);

        //遍历集合，嵌入到历史订单表。同时删除原有记录
        orderEntityList.forEach(orderEntity -> {

            //订单主表
            OrderHistoryEntity orderHistoryEntity = new OrderHistoryEntity();
            BeanUtils.copyProperties(orderEntity,orderHistoryEntity);
            orderHistoryMapper.insert(orderHistoryEntity);

            //查询出关联的订单明细（order_detail    order_detail_meal）
            QueryWrapper<OrderDetailEntity> detailWrapper = new QueryWrapper<>();
            detailWrapper.lambda().eq(OrderDetailEntity::getOrderId,orderEntity.getOrderId());
            List<OrderDetailEntity> orderDetailEntityList = orderDetailMapper.selectList(detailWrapper);

            orderDetailEntityList.forEach(orderDetailEntity -> {

                if (orderDetailEntity.getDishType() == 1){
                    //普通菜品
                    //存入历史表
                    OrderDetailHistoryEntity detailHistoryEntity = new OrderDetailHistoryEntity();
                    BeanUtils.copyProperties(orderDetailEntity,detailHistoryEntity);
                    orderDetailHistoryMapper.insert(detailHistoryEntity);

                    orderDetailMapper.deleteById(orderDetailEntity.getDetailId());
                }

                if (orderDetailEntity.getDishType() == 2){
                    //套餐。 获取当前套餐下的菜品信息
                    QueryWrapper<OrderDetailMealEntity> mealWrapper = new QueryWrapper<>();

                    mealWrapper.lambda().eq(OrderDetailMealEntity::getOrderId,orderDetailEntity.getOrderId());

                    List<OrderDetailMealEntity> detailMealEntityList = orderDetailMealMapper.selectList(mealWrapper);

                    for (OrderDetailMealEntity detailMealEntity : detailMealEntityList) {
                        //存入历史表
                        OrderDetailHistoryEntity detailHistoryEntity = new OrderDetailHistoryEntity();
                        BeanUtils.copyProperties(detailMealEntity,detailHistoryEntity);
                        orderDetailHistoryMapper.insert(detailHistoryEntity);
                        orderDetailMealMapper.deleteById(detailMealEntity.getDetailId());
                    }
                    orderDetailMapper.deleteById(orderDetailEntity.getDetailId());
                }

            });

            orderMapper.deleteById(orderEntity.getOrderId());

        });

    }
}
