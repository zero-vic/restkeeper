package com.restkeeper.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.Dish;
import com.restkeeper.store.entity.SetMealDish;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/02
 * Description:
 * Version:V1.0
 */
public interface ISetMealDishService extends IService<SetMealDish> {
    /**
     * 通过dishId获取菜品信息
     * @param setMealId
     * @return
     */
    List<Dish> getAllDishBySetMealId(String setMealId);

    /**
     *
     * @param setMealId
     * @param dishId
     * @return
     */
    Integer getDishCopiesInSetMeal(String dishId, String setMealId);
}
