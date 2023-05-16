package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.SetMeal;
import com.restkeeper.store.entity.SetMealDish;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/02
 * Description: 套餐借口
 * Version:V1.0
 */
public interface ISetMealService extends IService<SetMeal> {
    /**
     * 更具套餐名分页查询套餐
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    IPage<SetMeal> queryPage(int pageNum, int pageSize, String name);

    /**
     *  保存套餐信息
     * @param setMeal
     * @param setMealDishes
     * @return
     */
    boolean add(SetMeal setMeal, List<SetMealDish> setMealDishes);

    /**
     * 修改套餐
     * @param setMeal
     * @param setMealDishes
     * @return
     */
    boolean update(SetMeal setMeal, List<SetMealDish> setMealDishes);

    /**
     * 根据 l累呗查询套餐
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<SetMeal> queryByCategory(String categoryId, long pageNum, long pageSize);
}
