package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.constant.SystemCode;
import com.restkeeper.store.entity.Dish;
import com.restkeeper.store.entity.DishFlavor;
import com.restkeeper.store.mapper.DishMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/02
 * Description:
 * Version:V1.0
 */
@Service(version = "1.0.0",protocol = "dubbo")
@org.springframework.stereotype.Service("dishService")
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {
    @Autowired
    @Qualifier("dishFlavorService")
    private IDishFlavorService dishFlavorService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Dish dish, List<DishFlavor> flavorList) {

        try{
            // 保存菜品
            this.save(dish);
            //保存口味
            flavorList.forEach((dishFlavor)->{
                dishFlavor.setDishId(dish.getId());
            });

            dishFlavorService.saveBatch(flavorList);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Dish dish, List<DishFlavor> flavorList) {
        try{
            this.updateById(dish);
            // 先删除口味信息
            QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(DishFlavor::getDishId,dish.getId());
            dishFlavorService.remove(queryWrapper);
            flavorList.forEach(flavor -> {
                flavor.setDishId(dish.getId());
            });
            return dishFlavorService.saveBatch(flavorList);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }



    }

    @Override
    public List<Map<String, Object>> findEnableDishListInfo(String categoryId, String name) {

        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Dish::getId,Dish::getName,Dish::getStatus,Dish::getPrice);

        if (StringUtils.isNotEmpty(categoryId)){
            queryWrapper.lambda().eq(Dish::getCategoryId,categoryId);
        }
        if (StringUtils.isNotEmpty(name)){
            queryWrapper.lambda().eq(Dish::getName,name);
        }

        queryWrapper.lambda().eq(Dish::getStatus, SystemCode.ENABLED);

        return this.listMaps(queryWrapper);
    }

    @Override
    public IPage<Dish> queryByCategory(String categoryId, long pageNum, long pageSize) {
        IPage<Dish> page = new Page<>(pageNum,pageSize);

        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Dish::getCategoryId,categoryId);
        return this.page(page,queryWrapper);
    }
}
