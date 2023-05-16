package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.Dish;
import com.restkeeper.store.entity.DishFlavor;

import java.util.List;
import java.util.Map;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/02
 * Description:
 * Version:V1.0
 */
public interface IDishService extends IService<Dish> {
    /**
     * 保存菜品
     * 保存菜品时候需要维护 菜品和分类，菜品口味关系，缩略图，商品码等关系
     * @param dish
     * @param flavorList
     * @return
     */
    boolean save(Dish dish, List<DishFlavor> flavorList);

    /**
     * 修改菜品
     * @param dish
     * @param flavorList
     * @return
     */
    boolean update(Dish dish, List<DishFlavor> flavorList);

    /**
     * 根据分类信息与菜品名称查询菜品列表
     * @param categoryId
     * @param name
     * @return
     */
    List<Map<String,Object>> findEnableDishListInfo(String categoryId, String name);

    /**
     * 根据分类信息与菜品名称查询菜品列表 分页
     * @param categoryId
     * @param page
     * @param pageSize
     * @return
     */
    IPage<Dish> queryByCategory(String categoryId, long page, long pageSize);
}
