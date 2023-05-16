package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.store.entity.DishFlavor;
import com.restkeeper.store.mapper.DishFlavorMapper;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/02
 * Description:
 * Version:V1.0
 */
@org.springframework.stereotype.Service("dishFlavorService")
@Service(version = "1.0.0",protocol = "dubbo")
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements IDishFlavorService {
    @Override
    public List<DishFlavor> getFlavor(String dishId) {
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DishFlavor::getDishId,dishId);

        return this.list(queryWrapper);
    }
}
