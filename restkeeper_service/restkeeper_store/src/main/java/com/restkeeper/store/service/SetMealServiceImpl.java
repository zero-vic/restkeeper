package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.store.entity.SetMeal;
import com.restkeeper.store.entity.SetMealDish;
import com.restkeeper.store.mapper.SetMealMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/02
 * Description:
 * Version:V1.0
 */
@Service(version = "1.0.0",protocol = "dubbo")
@org.springframework.stereotype.Service("setMealService")
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, SetMeal> implements ISetMealService {

    @Autowired
    @Qualifier("setMealDishService")
    private ISetMealDishService setMealDishService;
    @Override
    public IPage<SetMeal> queryPage(int pageNum, int pageSize, String name) {
        IPage<SetMeal> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SetMeal> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(name)){
            queryWrapper.lambda().like(SetMeal::getName,name);
        }

        return this.page(page,queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(SetMeal setMeal, List<SetMealDish> setMealDishes) {
        try {
            this.save(setMeal);

            setMealDishes.forEach(s -> {
                s.setSetMealId(setMeal.getId());
                s.setIndex(0);
            });
            return setMealDishService.saveBatch(setMealDishes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SetMeal setMeal, List<SetMealDish> setMealDishes) {

        try {
            //修改套餐基础信息
            this.updateById(setMeal);

            //删除原有的菜品关联关系
            if (!CollectionUtils.isEmpty(setMealDishes)){

                QueryWrapper<SetMealDish> queryWrapper =new QueryWrapper<>();
                queryWrapper.lambda().eq(SetMealDish::getSetMealId,setMeal.getId());
                setMealDishService.remove(queryWrapper);

                //重建菜品的关联关系
                setMealDishes.forEach((setMealDish)->{
                    setMealDish.setSetMealId(setMeal.getId());
                });

                setMealDishService.saveBatch(setMealDishes);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IPage<SetMeal> queryByCategory(String categoryId, long pageNum, long pageSize) {
        IPage<SetMeal> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SetMeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SetMeal::getCategoryId,categoryId);
        return this.page(page, queryWrapper);
    }

}
