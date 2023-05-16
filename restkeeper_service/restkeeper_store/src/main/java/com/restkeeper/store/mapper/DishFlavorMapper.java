package com.restkeeper.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restkeeper.store.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 口味接口
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor>{
    /**
     * 更据dishId查询口味列表
     * @param dishId
     * @return
     */
    @Select("select * from t_dish_flavor where dish_id = #{dishId} order by last_update_time desc")
    public List<DishFlavor> selectFlavors(@Param("dishId") String dishId);
  
}
