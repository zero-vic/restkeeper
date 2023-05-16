package com.restkeeper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.restkeeper.entity.OrderHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderHistoryMapper extends BaseMapper<OrderHistoryEntity> {
}
