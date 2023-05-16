package com.restkeeper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restkeeper.entity.OrderDetailEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetailEntity> {
}
