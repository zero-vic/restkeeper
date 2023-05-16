package com.restkeeper.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.SellCalculation;

public interface ISellCalculationService extends IService<SellCalculation> {


    /**
     * 获取估清数目
     * @param dishId
     * @return
     */
    Integer getRemainderCount(String dishId);

    /**
     * 扣减沽清数
     * @param dishId
     * @param dishNumber
     */
    void decrease(String dishId, Integer dishNumber);

    /**
     * 增加沽清数
     * @param dishId
     * @param dishNum
     */
    void add(String dishId, int dishNum);

    /**
     * 加菜
     * @param dishId
     */
    void plusDish(String dishId);

    /**
     * 减菜
     * @param dishId
     */
    void reduceDish(String dishId);
}
