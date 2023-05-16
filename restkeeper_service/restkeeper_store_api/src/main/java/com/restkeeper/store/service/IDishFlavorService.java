package com.restkeeper.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.DishFlavor;

import java.util.List;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/05/02
 * Description:
 * Version:V1.0
 */
public interface IDishFlavorService extends IService<DishFlavor> {
    /**
     * 更具caipinid查询口味
     * @param dishId
     * @return
     */
    List<DishFlavor> getFlavor(String dishId);
}
