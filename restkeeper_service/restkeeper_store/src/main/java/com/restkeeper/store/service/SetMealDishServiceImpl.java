package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.restkeeper.store.entity.Dish;
import com.restkeeper.store.entity.SetMealDish;
import com.restkeeper.store.mapper.SetMealDishMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/02
 * Description:
 * Version:V1.0
 */
@Service(version = "1.0.0",protocol = "dubbo")
@org.springframework.stereotype.Service("setMealDishService")
public class SetMealDishServiceImpl extends ServiceImpl<SetMealDishMapper, SetMealDish> implements ISetMealDishService {

    @Autowired
    @Qualifier("dishService")
    private IDishService dishService;
    @Override
    public List<Dish> getAllDishBySetMealId(String setMealId) {
        List<SetMealDish> dishList = this.getBaseMapper().selectDishes(setMealId);
        List<String> dishIds = Lists.newArrayList();
        dishList.forEach(setMealDish -> {
            dishIds.add(setMealDish.getDishId());
        });

        return dishService.listByIds(dishIds);
    }

    @Override
    public Integer getDishCopiesInSetMeal(String dishId, String setMealId) {
        QueryWrapper<SetMealDish> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SetMealDish::getDishId,dishId).eq(SetMealDish::getSetMealId,setMealId);
        SetMealDish setMealDish = this.getOne(wrapper);
        return setMealDish==null?0:setMealDish.getDishCopies();
    }
}
