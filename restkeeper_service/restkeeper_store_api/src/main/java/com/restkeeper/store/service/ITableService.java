package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.restkeeper.store.entity.Table;

public interface ITableService extends IService<Table> {
    /**
     * 新增桌台
     * @param table
     * @return
     */
    boolean add(Table table);

    /**
     * 分页查询根据区域ID查询坐台列表
     * @param areaId
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Table> queryPageByAreaId(String areaId, int pageNum, int pageSize);

    /**
     * 根据区域id和状态查询桌台数量
     * @param areaId
     * @param status
     * @return
     */
    Integer countTableByStatus(String areaId,Integer status);
}
